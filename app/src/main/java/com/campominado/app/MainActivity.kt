package com.campominado.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.campominado.app.game.CellUiState
import com.campominado.app.game.Difficulty
import com.campominado.app.game.GameStatus
import com.campominado.app.game.GameUiState
import com.campominado.app.viewmodel.GameViewModel
import com.campominado.app.viewmodel.PartidaResultSaver
import com.campominado.app.viewmodel.PartidaResultado

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFF65D6A6),
                background = Color(0xFF101820),
                surface = Color(0xFF1B2833)
            )) {
                val gameViewModel: GameViewModel = viewModel(
                    factory = GameViewModelFactory
                )
                MinesweeperScreen(gameViewModel)
            }
        }
    }
}

private object GameViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val noOpSaver = object : PartidaResultSaver {
            override suspend fun salvarResultado(resultado: PartidaResultado) {
                // Integração com Room será adicionada pelo módulo responsável pelo banco.
            }
        }
        return GameViewModel(noOpSaver) as T
    }
}

@Composable
private fun MinesweeperScreen(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF101820)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CAMPO MINADO",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoPanel("MINAS", state.remainingMines.toString())
                InfoPanel("TEMPO", formatTime(state.elapsedSeconds))
            }

            Spacer(Modifier.height(16.dp))
            DifficultySelector(state.difficulty, viewModel)
            Spacer(Modifier.height(16.dp))

            Text(
                text = when (state.status) {
                    GameStatus.READY -> "Escolha uma casa para começar"
                    GameStatus.PLAYING -> "Boa sorte!"
                    GameStatus.WON -> "Você venceu!"
                    GameStatus.LOST -> "Fim de jogo!"
                },
                color = Color(0xFFB9C7D2),
                fontSize = 15.sp
            )

            Spacer(Modifier.height(12.dp))
            GameBoard(state, viewModel)

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.startNewGame() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF168A62))
            ) {
                Text("Nova partida")
            }

            state.errorMessage?.let {
                Text(it, color = Color(0xFFFF8A80), modifier = Modifier.padding(top = 8.dp))
            }
            if (state.isSavingResult) {
                Text("Salvando resultado...", color = Color.LightGray)
            }
        }
    }
}

@Composable
private fun InfoPanel(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(Color(0xFF1B2833), RoundedCornerShape(10.dp))
            .padding(horizontal = 22.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Color(0xFF9FB0BC), fontSize = 11.sp)
        Text(value, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DifficultySelector(selected: Difficulty, viewModel: GameViewModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Difficulty.values().forEach { difficulty ->
            Button(
                onClick = { viewModel.selectDifficulty(difficulty) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == difficulty) Color(0xFF168A62) else Color(0xFF334653)
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
            ) {
                Text(difficulty.displayName, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun GameBoard(state: GameUiState, viewModel: GameViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.board.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                row.forEach { cell ->
                    CellButton(
                        cell = cell,
                        enabled = !state.isGameFinished,
                        onReveal = { viewModel.revealCell(cell.row, cell.column) },
                        onFlag = { viewModel.toggleFlag(cell.row, cell.column) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CellButton(
    cell: CellUiState,
    enabled: Boolean,
    onReveal: () -> Unit,
    onFlag: () -> Unit
) {
    val background = when {
        cell.isRevealed && cell.isMine -> Color(0xFFB64040)
        cell.isRevealed -> Color(0xFFDCE5EA)
        else -> Color(0xFF496273)
    }
    val label = when {
        cell.isFlagged -> "🚩"
        !cell.isRevealed -> ""
        cell.isMine -> "💣"
        cell.adjacentMines == 0 -> ""
        else -> cell.adjacentMines.toString()
    }
    val numberColor = when (cell.adjacentMines) {
        1 -> Color(0xFF1976D2)
        2 -> Color(0xFF2E7D32)
        3 -> Color(0xFFD32F2F)
        else -> Color(0xFF263238)
    }

    Surface(
        modifier = Modifier
            .size(37.dp)
            .combinedClickable(
                enabled = enabled,
                onClick = onReveal,
                onLongClick = onFlag
            ),
        shape = RoundedCornerShape(4.dp),
        color = background
    ) {
        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (cell.isRevealed && !cell.isMine) numberColor else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
