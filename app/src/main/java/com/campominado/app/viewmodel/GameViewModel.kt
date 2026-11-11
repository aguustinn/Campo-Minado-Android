package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campominado.app.game.Difficulty
import com.campominado.app.game.GameStatus
import com.campominado.app.game.GameUiState
import com.campominado.app.game.MinesweeperEngine
import com.campominado.app.game.RevealResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val resultSaver: PartidaResultSaver
) : ViewModel() {
    private var engine = MinesweeperEngine(Difficulty.EASY)
    private var timerJob: Job? = null
    private var resultAlreadySaved = false

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        startNewGame()
    }

    fun setPlayerName(name: String) {
        _uiState.update { it.copy(playerName = name.trim().ifBlank { "Jogador" }) }
    }

    fun selectDifficulty(difficulty: Difficulty) {
        if (_uiState.value.status == GameStatus.PLAYING) return
        startNewGame(difficulty)
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

    fun startNewGame(difficulty: Difficulty = _uiState.value.difficulty) {
        stopTimer()
        engine = MinesweeperEngine(difficulty)
        resultAlreadySaved = false
        _uiState.update {
            it.copy(
                board = engine.toUiBoard(),
                difficulty = difficulty,
                status = GameStatus.READY,
                elapsedSeconds = 0L,
                remainingMines = difficulty.mines,
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
            it.copy(remainingMines = it.difficulty.mines - engine.flaggedCount)
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
                        jogador = snapshot.playerName,
                        dificuldade = snapshot.difficulty.displayName,
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
