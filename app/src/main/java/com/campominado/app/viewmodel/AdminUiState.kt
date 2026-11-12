package com.campominado.app.viewmodel

import com.campominado.app.data.repository.NivelDados

data class AdminUiState(
    val niveis: List<NivelDados> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null
)