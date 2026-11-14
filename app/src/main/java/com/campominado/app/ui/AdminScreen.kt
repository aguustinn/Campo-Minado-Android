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
fun AdminScreen(adminViewModel: AdminViewModel) {
    val uiState by adminViewModel.uiState.collectAsStateWithLifecycle()
    var nome by remember { mutableStateOf("") }
    var tamanho by remember { mutableStateOf("8x8") }
    var minas by remember { mutableStateOf("10") }
    var nivelEmEdicao by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage == "Nível salvo com sucesso.") {
            nome = ""
            tamanho = "8x8"
            minas = "10"
            nivelEmEdicao = null
            adminViewModel.limparMensagens()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF101820)).padding(20.dp)
    ) {
        Text("ADMINISTRAÇÃO", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color(0xFF65D6A6))
        Spacer(Modifier.height(20.dp))
        Text(if (nivelEmEdicao == null) "Cadastrar nível" else "Editar nível", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(nome, { nome = it }, label = { Text("Nome do nível") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(tamanho, { tamanho = it }, label = { Text("Tamanho do tabuleiro (ex: 8x8)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(minas, { minas = it }, label = { Text("Quantidade de minas") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { adminViewModel.salvarNivel(nivelEmEdicao, nome, tamanho, minas) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF168A62))
        ) { Text(if (nivelEmEdicao == null) "Cadastrar nível" else "Salvar alterações") }

        uiState.errorMessage?.let { Text(it, color = Color(0xFFFF8A80), modifier = Modifier.padding(top = 8.dp)) }
        uiState.successMessage?.let { Text(it, color = Color(0xFF65D6A6), modifier = Modifier.padding(top = 8.dp)) }

        Spacer(Modifier.height(24.dp))
        Text("Níveis cadastrados", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (uiState.isLoading) CircularProgressIndicator()
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(uiState.niveis, key = { it.id }) { nivel ->
                NivelCard(
                    nivel = nivel,
                    onEdit = {
                        nome = nivel.nome
                        tamanho = "${nivel.linhas}x${nivel.colunas}"
                        minas = nivel.minas.toString()
                        nivelEmEdicao = nivel.id
                    },
                    onDelete = {
                        adminViewModel.excluirNivel(nivel)
                        if (nivelEmEdicao == nivel.id) {
                            nivelEmEdicao = null
                            nome = ""
                            tamanho = "8x8"
                            minas = "10"
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NivelCard(nivel: NivelEntity, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2833))) {
        Column(Modifier.padding(14.dp)) {
            Text(nivel.nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Tabuleiro: ${nivel.linhas}x${nivel.colunas} | Minas: ${nivel.minas}", color = Color(0xFFB9C7D2))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete) { Text("Excluir", color = Color(0xFFFF8A80)) }
            }
        }
    }
}
