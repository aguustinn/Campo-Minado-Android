package com.campominado.app.game

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MinesweeperEngineTest {

    @Test
    fun primeiroCliqueNuncaRevelaUmaMina() {
        val engine = MinesweeperEngine(rows = 1, columns = 2, mines = 1, random = Random(0))

        val result = engine.reveal(0, 0)

        assertEquals(RevealResult.Safe, result)
        assertFalse(engine.toUiBoard()[0][0].isMine)
        assertTrue(engine.toUiBoard()[0][0].isRevealed)
    }

    @Test
    fun segundoCliqueRevelaMinaNoTabuleiroDeUmaLinha() {
        val engine = MinesweeperEngine(rows = 1, columns = 2, mines = 1, random = Random(0))
        engine.reveal(0, 0)

        assertEquals(RevealResult.HitMine, engine.reveal(0, 1))
        assertTrue(engine.toUiBoard()[0][1].isMine)
    }

    @Test
    fun bandeiraPodeSerAdicionadaERemovida() {
        val engine = MinesweeperEngine(rows = 2, columns = 2, mines = 1, random = Random(0))

        assertTrue(engine.toggleFlag(0, 0))
        assertTrue(engine.toUiBoard()[0][0].isFlagged)
        assertEquals(1, engine.flaggedCount)

        assertTrue(engine.toggleFlag(0, 0))
        assertFalse(engine.toUiBoard()[0][0].isFlagged)
        assertEquals(0, engine.flaggedCount)
    }

    @Test
    fun naoPermiteMarcarCasaRevelada() {
        val engine = MinesweeperEngine(rows = 1, columns = 2, mines = 1, random = Random(0))
        engine.reveal(0, 0)

        assertFalse(engine.toggleFlag(0, 0))
    }

    @Test
    fun coordenadasInvalidasNaoAlteramOJogo() {
        val engine = MinesweeperEngine(rows = 2, columns = 2, mines = 1, random = Random(0))

        assertEquals(RevealResult.NoChange, engine.reveal(-1, 0))
        assertFalse(engine.toggleFlag(2, 2))
        assertEquals(0, engine.flaggedCount)
    }

    @Test
    fun vitoriaQuandoTodasAsCasasSegurasSaoReveladas() {
        val engine = MinesweeperEngine(rows = 1, columns = 2, mines = 1, random = Random(0))

        assertEquals(RevealResult.Safe, engine.reveal(0, 0))
        assertEquals(engine.totalSafeCells, engine.revealedSafeCount)
    }

    @Test
    fun tabuleiroRespeitaDimensoesDaDificuldade() {
        val board = MinesweeperEngine(Difficulty.EASY).toUiBoard()

        assertEquals(8, board.size)
        assertEquals(8, board.first().size)
    }
}
