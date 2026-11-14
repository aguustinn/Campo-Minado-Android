package com.campominado.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Insert
    suspend fun inserir(partida: PartidaEntity): Long

    @Query("SELECT * FROM partidas ORDER BY data DESC")
    fun listarPartidas(): Flow<List<PartidaEntity>>
}
