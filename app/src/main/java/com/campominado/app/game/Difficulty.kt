package com.campominado.app.game

enum class Difficulty(
    val displayName: String,
    val rows: Int,
    val columns: Int,
    val mines: Int
) {
    EASY("Fácil", 8, 8, 10),
    MEDIUM("Médio", 10, 10, 15),
    HARD("Difícil", 12, 12, 25)
}
