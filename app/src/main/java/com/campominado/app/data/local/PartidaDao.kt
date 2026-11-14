package com.campominado.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Insert
    suspend fun inserir(partida: PartidaEntity): Long

    @Query("SELECT * FROM partidas ORDER BY data DESC")
    fun listarPartidas(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM partidas WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): PartidaEntity?

    @Update
    suspend fun atualizar(partida: PartidaEntity)

    @Delete
    suspend fun excluir(partida: PartidaEntity)
}
