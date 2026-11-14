package com.campominado.app.game

import kotlin.random.Random

sealed interface RevealResult {
    data object NoChange : RevealResult
    data object Safe : RevealResult
    data object HitMine : RevealResult
}

class MinesweeperEngine(
    private val rows: Int,
    private val columns: Int,
    private val mines: Int,
    private val random: Random = Random.Default
) {
    constructor(difficulty: Difficulty, random: Random = Random.Default) : this(
        rows = difficulty.rows,
        columns = difficulty.columns,
        mines = difficulty.mines,
        random = random
    )

    init {
        require(rows > 0 && columns > 0) { "O tabuleiro precisa ter linhas e colunas positivas." }
        require(mines > 0 && mines < rows * columns) { "A quantidade de minas é inválida." }
    }

    private data class Cell(
        var isMine: Boolean = false,
        var isRevealed: Boolean = false,
        var isFlagged: Boolean = false,
        var adjacentMines: Int = 0
    )

    private val cells = Array(rows) { Array(columns) { Cell() } }
    private var minesPlaced = false
    private var revealedSafeCells = 0

    val totalSafeCells: Int get() = rows * columns - mines
    val revealedSafeCount: Int get() = revealedSafeCells
    val flaggedCount: Int get() = cells.sumOf { row -> row.count { it.isFlagged } }

    fun reveal(row: Int, column: Int): RevealResult {
        if (!isInside(row, column)) return RevealResult.NoChange
        val cell = cells[row][column]
        if (cell.isRevealed || cell.isFlagged) return RevealResult.NoChange
        if (!minesPlaced) placeMines(row, column)

        if (cell.isMine) {
            cell.isRevealed = true
            return RevealResult.HitMine
        }

        revealSafeArea(row, column)
        return RevealResult.Safe
    }

    fun toggleFlag(row: Int, column: Int): Boolean {
        if (!isInside(row, column)) return false
        val cell = cells[row][column]
        if (cell.isRevealed) return false
        cell.isFlagged = !cell.isFlagged
        return true
    }

    fun revealAllMines() {
        cells.forEach { row -> row.forEach { cell -> if (cell.isMine) cell.isRevealed = true } }
    }

    fun toUiBoard(): List<List<CellUiState>> =
        cells.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->
                CellUiState(
                    row = rowIndex,
                    column = columnIndex,
                    isRevealed = cell.isRevealed,
                    isMine = cell.isMine,
                    adjacentMines = cell.adjacentMines,
                    isFlagged = cell.isFlagged
                )
            }
        }

    private fun placeMines(firstRow: Int, firstColumn: Int) {
        val positions = buildList {
            for (row in 0 until rows) {
                for (column in 0 until columns) {
                    if (row != firstRow || column != firstColumn) add(row to column)
                }
            }
        }
        positions.shuffled(random).take(mines).forEach { (row, column) ->
            cells[row][column].isMine = true
        }
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                cells[row][column].adjacentMines = countAdjacentMines(row, column)
            }
        }
        minesPlaced = true
    }

    private fun revealSafeArea(startRow: Int, startColumn: Int) {
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(startRow to startColumn)
        while (queue.isNotEmpty()) {
            val (row, column) = queue.removeFirst()
            if (!isInside(row, column)) continue
            val cell = cells[row][column]
            if (cell.isRevealed || cell.isFlagged || cell.isMine) continue
            cell.isRevealed = true
            revealedSafeCells++
            if (cell.adjacentMines == 0) {
                forEachNeighbor(row, column) { nextRow, nextColumn ->
                    val neighbor = cells[nextRow][nextColumn]
                    if (!neighbor.isRevealed && !neighbor.isFlagged && !neighbor.isMine) {
                        queue.add(nextRow to nextColumn)
                    }
                }
            }
        }
    }

    private fun countAdjacentMines(row: Int, column: Int): Int {
        var count = 0
        forEachNeighbor(row, column) { nextRow, nextColumn ->
            if (cells[nextRow][nextColumn].isMine) count++
        }
        return count
    }

    private fun forEachNeighbor(row: Int, column: Int, action: (Int, Int) -> Unit) {
        for (rowOffset in -1..1) {
            for (columnOffset in -1..1) {
                if (rowOffset == 0 && columnOffset == 0) continue
                val nextRow = row + rowOffset
                val nextColumn = column + columnOffset
                if (isInside(nextRow, nextColumn)) action(nextRow, nextColumn)
            }
        }
    }

    private fun isInside(row: Int, column: Int): Boolean =
        row in 0 until rows && column in 0 until columns
}
