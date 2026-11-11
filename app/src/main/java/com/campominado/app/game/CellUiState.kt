package com.campominado.app.game
data class CellUiState(
    val row: Int,
    val column: Int,
    val isRevealed: Boolean = false,
    val isMine: Boolean = false,
    val adjacentMines: Int = 0,
    val isFlagged: Boolean = false
)
