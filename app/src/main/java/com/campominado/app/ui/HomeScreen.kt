
package com.campominado.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onPlayClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTipsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "💣 CAMPO MINADO",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF65D6A6)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Teste sua estratégia e encontre as casas seguras!",
            fontSize = 15.sp,
            color = Color(0xFFB9C7D2)
        )

        Spacer(modifier = Modifier.height(40.dp))

        HomeButton("JOGAR", onPlayClick)

        Spacer(modifier = Modifier.height(14.dp))

        HomeButton("HISTÓRICO DE PARTIDAS", onHistoryClick)

        Spacer(modifier = Modifier.height(14.dp))

        HomeButton("DICAS", onTipsClick)
    }
}

@Composable
private fun HomeButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF168A62)
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
