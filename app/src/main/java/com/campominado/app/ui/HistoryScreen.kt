
package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class PartidaExemplo(
    val dificuldade: String,
    val tempo: String,
    val resultado: String
)

@Composable
fun HistoryScreen() {

    val partidas = listOf(
        PartidaExemplo("Fácil", "02:35", "Vitória"),
        PartidaExemplo("Médio", "04:18", "Derrota"),
        PartidaExemplo("Difícil", "06:42", "Vitória")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(20.dp)
    ) {
        Text(
            text = "HISTÓRICO DE PARTIDAS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF65D6A6)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Confira suas partidas anteriores",
            fontSize = 14.sp,
            color = Color(0xFFB9C7D2)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (partidas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma partida registrada ainda.",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(partidas) { partida ->
                    PartidaCard(partida)
                }
            }
        }
    }
}

@Composable
private fun PartidaCard(partida: PartidaExemplo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B2833)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Dificuldade: ${partida.dificuldade}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tempo: ${partida.tempo}",
                color = Color(0xFFB9C7D2)
            )

            Text(
                text = "Resultado: ${partida.resultado}",
                color = if (partida.resultado == "Vitória")
                    Color(0xFF65D6A6)
                else
                    Color(0xFFFF8A80),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
