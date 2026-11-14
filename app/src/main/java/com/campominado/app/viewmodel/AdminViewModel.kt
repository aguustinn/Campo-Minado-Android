package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campominado.app.data.local.NivelEntity
import com.campominado.app.data.remote.Dica
import com.campominado.app.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class AdminUiState(
    val niveis: List<NivelEntity> = emptyList(),
    val dicas: List<Dica> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.listarNiveis()
                .catch { erro -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = erro.message ?: "Erro ao carregar níveis.") }
                .collect { niveis -> _uiState.value = _uiState.value.copy(niveis = niveis, isLoading = false) }
        }
    }

    fun salvarNivel(id: Int?, nome: String, linhas: Int, colunas: Int, minas: Int) {
        viewModelScope.launch {
            val nivel = NivelEntity(id = id ?: 0, nome = nome, linhas = linhas, colunas = colunas, minas = minas)
            val resultado = runCatching {
                if (id == null) repository.inserir(nivel).getOrThrow() else repository.atualizar(nivel).getOrThrow()
            }
            _uiState.value = _uiState.value.copy(errorMessage = resultado.exceptionOrNull()?.message)
        }
    }

    fun excluirNivel(nivel: NivelEntity) {
        viewModelScope.launch {
            val resultado = repository.excluir(nivel)
            _uiState.value = _uiState.value.copy(errorMessage = resultado.exceptionOrNull()?.message)
        }
    }

    fun carregarDicas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.listarDicas()
                .onSuccess { dicas -> _uiState.value = _uiState.value.copy(dicas = dicas, isLoading = false) }
                .onFailure { erro -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = erro.message ?: "Não foi possível carregar as dicas. Verifique sua conexão.") }
        }
    }
}

