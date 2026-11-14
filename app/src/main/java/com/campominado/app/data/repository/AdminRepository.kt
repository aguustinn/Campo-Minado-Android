package com.campominado.app.data.repository

import com.campominado.app.data.local.NivelDao
import com.campominado.app.data.local.NivelEntity
import com.campominado.app.data.remote.Dica
import com.campominado.app.data.remote.FirestoreDicaDataSource
import kotlinx.coroutines.flow.Flow

class AdminRepository(
    private val nivelDao: NivelDao,
    private val dicaDataSource: FirestoreDicaDataSource = FirestoreDicaDataSource()
) {
    fun listarNiveis(): Flow<List<NivelEntity>> = nivelDao.listarNiveis()
    suspend fun buscarNivel(id: Int): Result<NivelEntity?> = runCatching { nivelDao.buscarPorId(id) }
    suspend fun inserir(nivel: NivelEntity): Result<Long> = runCatching { nivelDao.inserir(nivel) }
    suspend fun atualizar(nivel: NivelEntity): Result<Unit> = runCatching { nivelDao.atualizar(nivel) }
    suspend fun excluir(nivel: NivelEntity): Result<Unit> = runCatching { nivelDao.excluir(nivel) }
    suspend fun listarDicas(): Result<List<Dica>> = runCatching { dicaDataSource.listar() }
}
