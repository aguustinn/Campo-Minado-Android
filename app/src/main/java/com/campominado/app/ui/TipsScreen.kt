
package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class Dica(
    val titulo: String,
    val descricao: String,
    val emoji: String
)

@Composable
fun TipsScreen() {
    val dicas = listOf(
        Dica(
            "Comece pelos cantos",
            "Os cantos podem ser bons pontos para começar a explorar o tabuleiro."
            , "🎯"),
        Dica(
            "Observe os números",
            "Cada número indica quantas minas existem nas oito casas ao redor dele."
            , "🔢"),
        Dica(
            "Use as bandeiras",
            "Marque com uma bandeira as casas que você acredita que escondem minas."
            , "🚩"),
        Dica(
            "Não tenha pressa",
            "Analise os números e as casas vizinhas antes de revelar uma nova posição."
            , "🧠"),
        Dica(
            "Combine informações",
            "Use os números de casas próximas em conjunto para descobrir posições seguras."
            , "💡")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(20.dp)
    ) {
        Text(
            text = "DICAS E ESTRATÉGIAS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF65D6A6)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aprenda a jogar Campo Minado!",
            color = Color.LightGray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dicas) { dica ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B2833)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${dica.emoji}  ${dica.titulo}",
                            color = Color(0xFF65D6A6),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dica.descricao,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
