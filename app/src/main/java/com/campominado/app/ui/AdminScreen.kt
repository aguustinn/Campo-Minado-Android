package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.campominado.app.data.local.NivelEntity
import com.campominado.app.viewmodel.AdminViewModel

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var nome by remember { mutableStateOf("") }
    var linhas by remember { mutableStateOf("8") }
    var colunas by remember { mutableStateOf("8") }
    var minas by remember { mutableStateOf("10") }
    var editandoId by remember { mutableStateOf<Int?>(null) }
    Column(Modifier.fillMaxSize().background(Color(0xFF101820)).padding(20.dp)) {
        Text("ADMINISTRAÇÃO DE NÍVEIS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF65D6A6))
        OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(linhas, { linhas = it }, label = { Text("Linhas") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(colunas, { colunas = it }, label = { Text("Colunas") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(minas, { minas = it }, label = { Text("Minas") }, modifier = Modifier.weight(1f), singleLine = true)
        }
        Button(onClick = {
            val l = linhas.toIntOrNull(); val c = colunas.toIntOrNull(); val m = minas.toIntOrNull()
            if (nome.isNotBlank() && l != null && c != null && m != null && l > 0 && c > 0 && m in 1 until l * c) {
                viewModel.salvarNivel(editandoId, nome.trim(), l, c, m)
                nome = ""; linhas = "8"; colunas = "8"; minas = "10"; editandoId = null
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (editandoId == null) "Cadastrar nível" else "Salvar alterações") }
        state.errorMessage?.let { Text(it, color = Color(0xFFFF8A80)) }
        Spacer(Modifier.height(14.dp))
        Text("Níveis cadastrados", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 19.sp)
        if (state.isLoading) CircularProgressIndicator()
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.niveis, key = { it.id }) { nivel ->
                NivelCard(nivel,
                    onEdit = { nome = nivel.nome; linhas = nivel.linhas.toString(); colunas = nivel.colunas.toString(); minas = nivel.minas.toString(); editandoId = nivel.id },
                    onDelete = { viewModel.excluirNivel(nivel) })
            }
        }
    }
}

@Composable
private fun NivelCard(nivel: NivelEntity, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2833))) {
        Column(Modifier.padding(12.dp)) {
            Text(nivel.nome, color = Color.White, fontWeight = FontWeight.Bold)
            Text("${nivel.linhas}x${nivel.colunas} • ${nivel.minas} minas", color = Color(0xFFB9C7D2))
            Row {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete) { Text("Excluir", color = Color(0xFFFF8A80)) }
            }
        }
    }
}
