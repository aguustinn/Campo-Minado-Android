package com.campominado.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.campominado.app.data.repository.AdminRepository

class AdminViewModelFactory(
    private val repository: AdminRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            "ViewModel não reconhecido: ${modelClass.name}"
        }
        return AdminViewModel(repository) as T
    }
}
