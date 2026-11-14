package com.campominado.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NivelDao {
    @Insert
    suspend fun inserir(nivel: NivelEntity): Long

    @Query("SELECT * FROM niveis ORDER BY nome")
    fun listarNiveis(): Flow<List<NivelEntity>>

    @Update
    suspend fun atualizar(nivel: NivelEntity)

    @Delete
    suspend fun excluir(nivel: NivelEntity)
}
