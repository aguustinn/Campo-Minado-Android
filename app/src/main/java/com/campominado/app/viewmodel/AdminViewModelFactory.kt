package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.campominado.app.data.repository.NivelRepository

class AdminViewModelFactory(
    private val repository: NivelRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        require(modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            "ViewModel não reconhecido: ${modelClass.name}"
        }

        return AdminViewModel(repository) as T
    }
}