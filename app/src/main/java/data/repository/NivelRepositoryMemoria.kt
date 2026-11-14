package com.campominado.app.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NivelRepositoryMemoria : NivelRepository {

    private val niveis = MutableStateFlow(
        listOf(
            NivelDados(1, "Fácil", "8x8", 10),
            NivelDados(2, "Médio", "12x12", 25),
            NivelDados(3, "Difícil", "16x16", 40)
        )
    )

    private var proximoId = 4

    override fun observarNiveis(): Flow<List<NivelDados>> {
        return niveis.asStateFlow()
    }

    override suspend fun salvarNivel(nivel: NivelDados) {
        if (nivel.id == 0) {
            val novoNivel = nivel.copy(id = proximoId++)
            niveis.update { lista -> lista + novoNivel }
        } else {
            niveis.update { lista ->
                lista.map { existente ->
                    if (existente.id == nivel.id) nivel else existente
                }
            }
        }
    }

    override suspend fun excluirNivel(nivel: NivelDados) {
        niveis.update { lista ->
            lista.filter { it.id != nivel.id }
        }
    }
}