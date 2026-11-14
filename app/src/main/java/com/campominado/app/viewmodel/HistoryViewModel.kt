package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campominado.app.data.local.PartidaEntity
import com.campominado.app.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class HistoryUiState(
    val partidas: List<PartidaEntity> = emptyList(),
    val ranking: List<Pair<String, Int>> = emptyList(),
    val dificuldade: String? = null,
    val dificuldadesDisponiveis: List<String> = listOf("Fácil", "Médio", "Difícil"),
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
            combine(repository.listarPartidas(), filtros) { partidas, filtro ->
                val filtradas = partidas
                    .filter { filtro.dificuldade == null || it.dificuldade == filtro.dificuldade }
                    .sortedBy { if (filtro.ordenarPorTempo) it.tempo else -it.data }
                val ranking = partidas
                    .filter { it.resultado.equals("Vitória", ignoreCase = true) }
                    .groupingBy { it.jogador }
                    .eachCount()
                    .toList()
                    .sortedByDescending { it.second }
                val dificuldades = (listOf("Fácil", "Médio", "Difícil") + partidas.map { it.dificuldade })
                    .distinct()
                    .sorted()
                filtro.copy(
                    partidas = filtradas,
                    ranking = ranking,
                    dificuldadesDisponiveis = dificuldades,
                    isLoading = false
                )
            }.catch { erro ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = erro.message ?: "Erro ao carregar histórico."
                )
            }.collect { _uiState.value = it }
        }
    }

    fun filtrarPorDificuldade(dificuldade: String?) {
        filtros.value = filtros.value.copy(dificuldade = dificuldade)
    }

    fun ordenarPorTempo(ativo: Boolean) {
        filtros.value = filtros.value.copy(ordenarPorTempo = ativo)
    }
}
