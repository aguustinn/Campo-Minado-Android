package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campominado.app.data.local.NivelEntity
import com.campominado.app.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class AdminUiState(
    val niveis: List<NivelEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.listarNiveis()
                .catch { erro ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = erro.message ?: "Erro ao carregar níveis."
                    )
                }
                .collect { niveis ->
                    _uiState.value = _uiState.value.copy(niveis = niveis, isLoading = false)
                }
        }
    }

    fun limparMensagens() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }

    fun salvarNivel(id: Int?, nome: String, tamanho: String, minasTexto: String) {
        val partes = tamanho.lowercase().split("x")
        val linhas = partes.getOrNull(0)?.trim()?.toIntOrNull()
        val colunas = partes.getOrNull(1)?.trim()?.toIntOrNull()
        val minas = minasTexto.toIntOrNull()

        if (nome.isBlank() || partes.size != 2 || linhas == null || colunas == null ||
            linhas <= 0 || colunas <= 0 || minas == null || minas <= 0 ||
            minas >= linhas.toLong() * colunas
        ) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Informe nome, tamanho (ex.: 8x8) e quantidade de minas válidos.",
                successMessage = null
            )
            return
        }

        viewModelScope.launch {
            runCatching {
                val nivel = NivelEntity(
                    id = id ?: 0,
                    nome = nome.trim(),
                    linhas = linhas,
                    colunas = colunas,
                    minas = minas
                )
                if (id == null) repository.inserir(nivel) else repository.atualizar(nivel)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    errorMessage = null,
                    successMessage = "Nível salvo com sucesso."
                )
            }.onFailure { erro ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = erro.message ?: "Erro ao salvar nível.",
                    successMessage = null
                )
            }
        }
    }

    fun excluirNivel(nivel: NivelEntity) {
        viewModelScope.launch {
            runCatching { repository.excluir(nivel) }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null,
                        successMessage = "Nível excluído com sucesso."
                    )
                }
                .onFailure { erro ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = erro.message ?: "Erro ao excluir nível.",
                        successMessage = null
                    )
                }
        }
    }
}
