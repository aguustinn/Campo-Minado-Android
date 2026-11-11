package com.campominado.app.viewmodel

/**
 * Contrato para integrar o resultado da partida ao Repository/Room.
 * O responsável pelo banco implementará esta interface.
 */
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
