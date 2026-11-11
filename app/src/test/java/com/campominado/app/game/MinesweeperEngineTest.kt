package com.campominado.app.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class MinesweeperEngineTest {

    @Test
    fun firstRevealedCellIsNeverMine() {
        val engine = MinesweeperEngine(Difficulty.EASY, Random(10))

        val result = engine.reveal(0, 0)
        val firstCell = engine.toUiBoard()[0][0]

        assertEquals(RevealResult.Safe, result)
        assertFalse(firstCell.isMine)
        assertTrue(firstCell.isRevealed)
    }

    @Test
    fun boardHasConfiguredDimensions() {
        val engine = MinesweeperEngine(Difficulty.EASY)
        val board = engine.toUiBoard()

        assertEquals(8, board.size)
        assertEquals(8, board.first().size)
    }

    @Test
    fun flagCanBeAddedAndRemoved() {
        val engine = MinesweeperEngine(Difficulty.EASY)

        assertTrue(engine.toggleFlag(0, 0))
        assertTrue(engine.toUiBoard()[0][0].isFlagged)
        assertTrue(engine.toggleFlag(0, 0))
        assertFalse(engine.toUiBoard()[0][0].isFlagged)
    }

    @Test
    fun cannotFlagOutsideBoard() {
        val engine = MinesweeperEngine(Difficulty.EASY)

        assertFalse(engine.toggleFlag(-1, 0))
        assertFalse(engine.toggleFlag(0, 8))
    }
}
