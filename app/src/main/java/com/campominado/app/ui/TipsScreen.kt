package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

private data class Dica(
    val titulo: String,
    val descricao: String,
    val emoji: String
)

@Composable
fun TipsScreen() {
    var dicas by remember { mutableStateOf<List<Dica>>(emptyList()) }
    var carregando by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf<String?>(null) }

    fun carregarDicas() {
        carregando = true
        erro = null

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
                carregando = false
            }
            .addOnFailureListener { exception ->
                erro = exception.localizedMessage ?: "Não foi possível carregar as dicas."
                carregando = false
            }
    }

    LaunchedEffect(Unit) {
        carregarDicas()
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

        when {
            carregando -> {
                CircularProgressIndicator(color = Color(0xFF65D6A6))
                Text(
                    text = "Carregando dicas...",
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            erro != null -> {
                Text(
                    text = "Erro ao carregar as dicas: $erro",
                    color = Color(0xFFFF8A80),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { carregarDicas() }) {
                    Text("Tentar novamente")
                }
            }

            dicas.isEmpty() -> {
                Text(
                    text = "Nenhuma dica encontrada na coleção 'dicas' do Firestore.",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { carregarDicas() }) {
                    Text("Atualizar")
                }
            }

            else -> {
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
                            Column(modifier = Modifier.padding(16.dp)) {
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
    }
}
