package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campominado.app.data.local.PartidaEntity
import com.campominado.app.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class HistoryUiState(
    val partidas: List<PartidaEntity> = emptyList(),
    val ranking: List<Pair<String, Int>> = emptyList(),
    val dificuldade: String? = null,
    val ordenarPorTempo: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class HistoryViewModel(private val repository: GameRepository) : ViewModel() {
    private val filtros = MutableStateFlow(HistoryUiState())
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.listarPartidas(), filtros) { partidas, estado ->
                val filtradas = partidas
                    .filter { partida -> estado.dificuldade == null || partida.dificuldade == estado.dificuldade }
                    .sortedBy { partida -> if (estado.ordenarPorTempo) partida.tempo else -partida.data }
                val ranking = partidas
                    .filter { partida -> partida.resultado.equals("Vitória", ignoreCase = true) }
                    .groupingBy { partida -> partida.jogador }
                    .eachCount()
                    .toList()
                    .sortedByDescending { (_, vitorias) -> vitorias }
                    .map { (jogador, vitorias) -> jogador to vitorias }
                estado.copy(partidas = filtradas, ranking = ranking, isLoading = false)
            }
                .catch { erro -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = erro.message ?: "Erro ao carregar histórico.") }
                .collect { _uiState.value = it }
        }
    }

    fun filtrarPorDificuldade(dificuldade: String?) {
        filtros.value = filtros.value.copy(dificuldade = dificuldade)
    }

    fun ordenarPorTempo(ativo: Boolean) {
        filtros.value = filtros.value.copy(ordenarPorTempo = ativo)
    }
}
