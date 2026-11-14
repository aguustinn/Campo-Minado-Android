package com.campominado.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.campominado.app.data.local.AppDatabase
import com.campominado.app.data.remote.FirestoreDicaDataSource
import com.campominado.app.data.repository.AdminRepository
import com.campominado.app.data.repository.GameRepository

class AppViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val database = AppDatabase.getInstance(context.applicationContext)
    private val gameRepository = GameRepository(database.partidaDao())
    private val adminRepository = AdminRepository(database.nivelDao(), FirestoreDicaDataSource())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(gameRepository)
        modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(gameRepository)
        modelClass.isAssignableFrom(AdminViewModel::class.java) -> AdminViewModel(adminRepository)
        else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    } as T
}
