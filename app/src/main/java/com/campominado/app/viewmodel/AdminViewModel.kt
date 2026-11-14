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
    fun salvarNivel(
        id: Int? = null,
        nome: String,
        tamanho: String,
        minasTexto: String
    ) {
        val nomeValidado = nome.trim()
        val tamanhoValidado = tamanho.trim()
        val quantidadeMinas = minasTexto.toIntOrNull()

        if (nomeValidado.isBlank()) {
            mostrarErro("Informe o nome do nível.")
            return
        }

        val dimensoes = tamanhoValidado.lowercase().split("x")
        val linhas = dimensoes.getOrNull(0)?.trim()?.toIntOrNull()
        val colunas = dimensoes.getOrNull(1)?.trim()?.toIntOrNull()

        if (dimensoes.size != 2 ||
            linhas == null || colunas == null ||
            linhas <= 0 || colunas <= 0
        ) {
            mostrarErro("Informe o tamanho no formato 8x8.")
            return
        }

        if (quantidadeMinas == null ||
            quantidadeMinas <= 0 ||
            quantidadeMinas >= linhas.toLong() * colunas
        ) {
            mostrarErro("Quantidade de minas inválida.")
            return
        }

        viewModelScope.launch {
            try {
                nivelRepository.salvarNivel(
                    NivelDados(
                        id = id ?: 0,
                        nome = nomeValidado,
                        tamanho = "${linhas}x${colunas}",
                        minas = quantidadeMinas
                    )
                )

                _uiState.value = _uiState.value.copy(
                    errorMessage = null,
                    successMessage = "Nível salvo com sucesso."
                )
            } catch (erro: Exception) {
                mostrarErro(erro.message ?: "Erro ao salvar o nível.")
            }
        }
    }
    fun excluirNivel(nivel: NivelDados) {
        viewModelScope.launch {
            try {
                nivelRepository.excluirNivel(nivel)

                _uiState.value = _uiState.value.copy(
                    errorMessage = null,
                    successMessage = "Nível excluído com sucesso."
                )

            } catch (erro: Exception) {
                mostrarErro(
                    erro.message ?: "Erro ao excluir o nível."
                )
            }
        }
    }
}