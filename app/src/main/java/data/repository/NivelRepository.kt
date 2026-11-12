package com.campominado.app.data.repository

import kotlinx.coroutines.flow.Flow

data class NivelDados(
    val id: Int = 0,
    val nome: String,
    val tamanho: String,
    val minas: Int
)

interface NivelRepository {

    fun observarNiveis(): Flow<List<NivelDados>>

    suspend fun salvarNivel(nivel: NivelDados)

    suspend fun excluirNivel(nivel: NivelDados)
}