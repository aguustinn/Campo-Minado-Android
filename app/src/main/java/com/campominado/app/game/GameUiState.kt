package com.campominado.app.game

/**
 * Estado completo da tela de jogo.
 * O GameViewModel atualizará este objeto por meio de StateFlow.
 */
data class GameUiState(
    val board: List<List<CellUiState>> = emptyList(),
    val difficulty: Difficulty = Difficulty.EASY,
    val status: GameStatus = GameStatus.READY,
    val elapsedSeconds: Long = 0L,
    val remainingMines: Int = Difficulty.EASY.mines,
    val revealedSafeCells: Int = 0,
    val playerName: String = "Jogador",
    val message: String? = null,
    val isSavingResult: Boolean = false,
    val errorMessage: String? = null
) {
    val isGameFinished: Boolean
        get() = status == GameStatus.WON || status == GameStatus.LOST
}
