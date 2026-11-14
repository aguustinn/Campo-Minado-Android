
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore

private data class Dica(
    val titulo: String,
    val descricao: String,
    val emoji: String
)

@Composable
fun TipsScreen() {
    var dicas by remember { mutableStateOf<List<Dica>>(emptyList()) }
    var erro by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("dicas")
            .get()
            .addOnSuccessListener { resultado ->
                dicas = resultado.documents.map { documento ->
                    Dica(
                        titulo = documento.getString("titulo") ?: "",
                        descricao = documento.getString("descricao") ?: "",
                        emoji = documento.getString("emoji") ?: "💡"
                    )
                }
            }
            .addOnFailureListener {
                erro = true
            }
    }

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
