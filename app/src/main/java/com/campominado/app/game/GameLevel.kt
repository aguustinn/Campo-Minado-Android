package com.campominado.app.game

data class GameLevel(
    val id: String,
    val name: String,
    val rows: Int,
    val columns: Int,
    val mines: Int
) {
    companion object {
        fun defaults(): List<GameLevel> = Difficulty.values().map { difficulty ->
            GameLevel(
                id = "default_${difficulty.name}",
                name = difficulty.displayName,
                rows = difficulty.rows,
                columns = difficulty.columns,
                mines = difficulty.mines
            )
        }
    }
}
