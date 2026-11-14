package com.campominado.app.data.repository

import com.campominado.app.data.local.NivelDao
import com.campominado.app.data.local.NivelEntity
import kotlinx.coroutines.flow.Flow

class AdminRepository(private val nivelDao: NivelDao) {
    fun listarNiveis(): Flow<List<NivelEntity>> = nivelDao.listarNiveis()
    suspend fun inserir(nivel: NivelEntity) = nivelDao.inserir(nivel)
    suspend fun atualizar(nivel: NivelEntity) = nivelDao.atualizar(nivel)
    suspend fun excluir(nivel: NivelEntity) = nivelDao.excluir(nivel)
}
