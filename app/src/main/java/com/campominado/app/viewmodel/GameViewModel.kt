package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campominado.app.data.repository.AdminRepository
import com.campominado.app.game.GameLevel
import com.campominado.app.game.GameStatus
import com.campominado.app.game.GameUiState
import com.campominado.app.game.MinesweeperEngine
import com.campominado.app.game.RevealResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val resultSaver: PartidaResultSaver,
    private val adminRepository: AdminRepository
) : ViewModel() {
    private var engine = MinesweeperEngine(8, 8, 10)
    private var timerJob: Job? = null
    private var resultAlreadySaved = false

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        startNewGame()
        viewModelScope.launch {
            adminRepository.listarNiveis()
                .catch { erro ->
                    _uiState.update {
                        it.copy(errorMessage = erro.message ?: "Não foi possível carregar os níveis cadastrados.")
                    }
                }
                .collect { niveis ->
                    val personalizados = niveis.map { nivel ->
                        GameLevel(
                            id = "custom_${nivel.id}",
                            name = nivel.nome,
                            rows = nivel.linhas,
                            columns = nivel.colunas,
                            mines = nivel.minas
                        )
                    }
                    val disponiveis = GameLevel.defaults() + personalizados
                    val nivelAtualAindaExiste = disponiveis.any { it.id == _uiState.value.selectedLevel.id }
                    _uiState.update { it.copy(availableLevels = disponiveis) }
                    if (!nivelAtualAindaExiste) {
                        startNewGame(GameLevel.defaults().first())
                    }
                }
        }
    }

    fun setPlayerName(name: String) {
        _uiState.update { it.copy(playerName = name) }
    }

    fun selectLevel(level: GameLevel) {
        if (_uiState.value.status == GameStatus.PLAYING) return
        if (_uiState.value.availableLevels.any { it.id == level.id }) {
            startNewGame(level)
        }
    }

    fun revealCell(row: Int, column: Int) {
        val current = _uiState.value
        if (current.isGameFinished) return

        val result = engine.reveal(row, column)
        if (result == RevealResult.NoChange) return

        when (result) {
            RevealResult.HitMine -> {
                stopTimer()
                engine.revealAllMines()
                _uiState.update {
                    it.copy(
                        board = engine.toUiBoard(),
                        status = GameStatus.LOST,
                        message = "Você encontrou uma mina! Fim de jogo."
                    )
                }
                saveResult(GameStatus.LOST)
            }

            RevealResult.Safe -> {
                if (current.status == GameStatus.READY) startTimer()
                val won = engine.revealedSafeCount == engine.totalSafeCells
                _uiState.update {
                    it.copy(
                        board = engine.toUiBoard(),
                        status = if (won) GameStatus.WON else GameStatus.PLAYING,
                        revealedSafeCells = engine.revealedSafeCount,
                        message = if (won) "Parabéns! Você venceu!" else null
                    )
                }
                if (won) {
                    stopTimer()
                    saveResult(GameStatus.WON)
                }
            }

            RevealResult.NoChange -> Unit
        }
        updateRemainingMines()
    }

    fun toggleFlag(row: Int, column: Int) {
        if (_uiState.value.isGameFinished) return
        if (engine.toggleFlag(row, column)) {
            _uiState.update { it.copy(board = engine.toUiBoard()) }
            updateRemainingMines()
        }
    }

    fun startNewGame(level: GameLevel = _uiState.value.selectedLevel) {
        stopTimer()
        engine = MinesweeperEngine(level.rows, level.columns, level.mines)
        resultAlreadySaved = false
        _uiState.update {
            it.copy(
                board = engine.toUiBoard(),
                selectedLevel = level,
                status = GameStatus.READY,
                elapsedSeconds = 0L,
                remainingMines = level.mines,
                revealedSafeCells = 0,
                message = null,
                isSavingResult = false,
                errorMessage = null
            )
        }
    }

    private fun startTimer() {
        if (timerJob != null) return
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1_000)
                if (_uiState.value.status != GameStatus.PLAYING) break
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun updateRemainingMines() {
        _uiState.update {
            it.copy(remainingMines = it.selectedLevel.mines - engine.flaggedCount)
        }
    }

    private fun saveResult(status: GameStatus) {
        if (resultAlreadySaved) return
        resultAlreadySaved = true
        val snapshot = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingResult = true, errorMessage = null) }
            try {
                resultSaver.salvarResultado(
                    PartidaResultado(
                        jogador = snapshot.playerName.trim().ifBlank { "Jogador" },
                        dificuldade = snapshot.selectedLevel.name,
                        tempoSegundos = snapshot.elapsedSeconds,
                        resultado = if (status == GameStatus.WON) "Vitória" else "Derrota"
                    )
                )
                _uiState.update { it.copy(isSavingResult = false) }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isSavingResult = false,
                        errorMessage = exception.message ?: "Não foi possível salvar a partida."
                    )
                }
            }
        }
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }
}
