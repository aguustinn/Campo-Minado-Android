package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.campominado.app.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().background(Color(0xFF101820)).padding(20.dp)) {
        Text("HISTÓRICO DE PARTIDAS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF65D6A6))
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = state.dificuldade == null, onClick = { viewModel.filtrarPorDificuldade(null) }, label = { Text("Todas") })
            state.dificuldadesDisponiveis.forEach { nivel ->
                FilterChip(selected = state.dificuldade == nivel, onClick = { viewModel.filtrarPorDificuldade(nivel) }, label = { Text(nivel) })
            }
        }
        Text("Ranking local", color = Color(0xFF65D6A6), fontWeight = FontWeight.Bold)
        state.ranking.take(5).forEachIndexed { index, entry ->
            Text("${index + 1}. ${entry.first} — ${entry.second} vitórias", color = Color.White)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = state.ordenarPorTempo, onCheckedChange = viewModel::ordenarPorTempo)
            Text("Ordenar por menor tempo", color = Color.White)
        }
        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null -> Text(state.errorMessage!!, color = Color(0xFFFF8A80))
            state.partidas.isEmpty() -> Text("Nenhuma partida registrada ainda.", color = Color.White)
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.partidas, key = { it.id }) { partida ->
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2833))) {
                        Column(Modifier.padding(16.dp)) {
                            Text("${partida.jogador} • ${partida.dificuldade}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text("Tempo: ${partida.tempo / 60}:${(partida.tempo % 60).toString().padStart(2, '0')}", color = Color(0xFFB9C7D2))
                            Text("Resultado: ${partida.resultado}", color = if (partida.resultado == "Vitória") Color(0xFF65D6A6) else Color(0xFFFF8A80), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
