package com.campominado.app.viewmodel

import com.campominado.app.data.local.NivelEntity

data class AdminUiState(
    val niveis: List<NivelEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
