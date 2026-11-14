package com.campominado.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jogador: String,
    val dificuldade: String,
    val tempo: Long,
    val resultado: String,
    val data: Long
)
