package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.campominado.app.data.repository.NivelRepository
import com.campominado.app.data.repository.NivelDados
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AdminViewModel(
    private val nivelRepository: NivelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())

    val uiState: StateFlow<AdminUiState> = _uiState
    init {
        observarNiveis()
    }

    fun mostrarErro(mensagem: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = mensagem,
            successMessage = null
        )
    }

    fun limparMensagens() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    private fun observarNiveis() {
        viewModelScope.launch {
            nivelRepository.observarNiveis()
                .catch { erro ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = erro.message
                            ?: "Erro ao carregar os níveis."
                    )
                }
                .collect { listaDeNiveis ->
                    _uiState.value = _uiState.value.copy(
                        niveis = listaDeNiveis,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }
}