package com.campominado.app.viewmodel

interface PartidaResultSaver {
    suspend fun salvarResultado(resultado: PartidaResultado)
}

data class PartidaResultado(
    val jogador: String,
    val dificuldade: String,
    val tempoSegundos: Long,
    val resultado: String,
    val dataMillis: Long = System.currentTimeMillis()
)
