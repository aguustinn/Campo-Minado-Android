package com.campominado.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "niveis")
data class NivelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val linhas: Int,
    val colunas: Int,
    val minas: Int
)
