package com.campominado.app.game

data class GameUiState(
    val board: List<List<CellUiState>> = emptyList(),
    val selectedLevel: GameLevel = GameLevel.defaults().first(),
    val availableLevels: List<GameLevel> = GameLevel.defaults(),
    val status: GameStatus = GameStatus.READY,
    val elapsedSeconds: Long = 0L,
    val remainingMines: Int = GameLevel.defaults().first().mines,
    val revealedSafeCells: Int = 0,
    val playerName: String = "Jogador",
    val message: String? = null,
    val isSavingResult: Boolean = false,
    val errorMessage: String? = null
) {
    val isGameFinished: Boolean
        get() = status == GameStatus.WON || status == GameStatus.LOST
}
