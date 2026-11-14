package com.campominado.app.data.repository

import com.campominado.app.data.local.PartidaDao
import com.campominado.app.data.local.PartidaEntity
import com.campominado.app.viewmodel.PartidaResultSaver
import com.campominado.app.viewmodel.PartidaResultado
import kotlinx.coroutines.flow.Flow

class GameRepository(private val partidaDao: PartidaDao) : PartidaResultSaver {
    fun listarPartidas(): Flow<List<PartidaEntity>> = partidaDao.listarPartidas()
    suspend fun buscarPartida(id: Int): Result<PartidaEntity?> = runCatching { partidaDao.buscarPorId(id) }
    suspend fun inserir(partida: PartidaEntity): Result<Long> = runCatching { partidaDao.inserir(partida) }
    suspend fun atualizar(partida: PartidaEntity): Result<Unit> = runCatching { partidaDao.atualizar(partida) }
    suspend fun excluir(partida: PartidaEntity): Result<Unit> = runCatching { partidaDao.excluir(partida) }

    override suspend fun salvarResultado(resultado: PartidaResultado) {
        partidaDao.inserir(PartidaEntity(
            jogador = resultado.jogador,
            dificuldade = resultado.dificuldade,
            tempo = resultado.tempoSegundos,
            resultado = resultado.resultado,
            data = resultado.dataMillis
        ))
    }
}
