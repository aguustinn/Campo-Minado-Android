package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.campominado.app.viewmodel.AdminViewModel

@Composable
fun TipsScreen(viewModel: AdminViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) { viewModel.carregarDicas() }
    Column(Modifier.fillMaxSize().background(Color(0xFF101820)).padding(20.dp)) {
        Text("DICAS E ESTRATÉGIAS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF65D6A6))
        Spacer(Modifier.height(16.dp))
        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null -> Text("Não foi possível carregar as dicas. Verifique sua conexão.", color = Color(0xFFFF8A80))
            state.dicas.isEmpty() -> Text("Nenhuma dica disponível.", color = Color.White)
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.dicas, key = { it.id }) { dica ->
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2833))) {
                        Column(Modifier.padding(16.dp)) {
                            Text(dica.titulo, color = Color(0xFF65D6A6), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(dica.descricao, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
