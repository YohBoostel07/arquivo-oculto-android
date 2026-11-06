package com.arquivoculto.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Night = Color(0xFF080D14)
private val Panel = Color(0xFF111C29)
private val Gold = Color(0xFFD7B477)
private val Pale = Color(0xFFF1EEE7)
private val Muted = Color(0xFFA6B0BC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8, 13, 20)
        window.navigationBarColor = android.graphics.Color.rgb(8, 13, 20)
        setContent {
            MaterialTheme {
                Surface(color = Night) {
                    ArquivoOcultoApp()
                }
            }
        }
    }
}

@Composable
private fun ArquivoOcultoApp() {
    var showingCase by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF172333), Night, Color(0xFF05080D))))
    ) {
        AnimatedContent(
            targetState = showingCase,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "tela"
        ) { caseOpen ->
            if (caseOpen) CaseScreen(onBack = { showingCase = false })
            else HomeScreen(onOpen = { showingCase = true })
        }
    }
}

@Composable
private fun HomeScreen(onOpen: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .border(1.dp, Gold.copy(alpha = .7f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("AO", color = Gold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text("ARQUIVO OCULTO", color = Pale, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("DEPARTAMENTO DE INVESTIGAÇÃO", color = Muted, fontSize = 9.sp, letterSpacing = 1.2.sp)
            }
        }

        Spacer(Modifier.height(74.dp))
        Text("DOSSIÊ 001   •   ACESSO RESTRITO", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(17.dp))
        Text(
            "Toda verdade\ndeixa rastros.",
            color = Pale,
            fontSize = 43.sp,
            lineHeight = 49.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Investigue casos inexplicáveis. Reúna evidências. Descubra o que realmente aconteceu.",
            color = Muted,
            fontSize = 16.sp,
            lineHeight = 25.sp
        )
        Spacer(Modifier.height(36.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Gold.copy(alpha = .45f), RoundedCornerShape(18.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF1B2A3B), Panel)), RoundedCornerShape(18.dp))
                .padding(22.dp)
        ) {
            Text("PRIMEIRO CASO  /  INVESTIGAÇÃO", color = Gold, fontSize = 10.sp, letterSpacing = 1.5.sp)
            Spacer(Modifier.height(16.dp))
            Text("O Desaparecimento\nde Blackwood", color = Pale, fontSize = 27.sp, lineHeight = 33.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Text(
                "Uma cidade pequena. Uma noite sem testemunhas. Uma pessoa que parece nunca ter existido.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Night)
            ) {
                Text("ABRIR DOSSIÊ     →", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }

        Spacer(Modifier.height(28.dp))
        Text("PROTÓTIPO INICIAL  •  VERSÃO 0.1", color = Color(0xFF657487), fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun CaseScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 25.dp, vertical = 28.dp)
    ) {
        Text("AO   /   ARQUIVO OCULTO", color = Gold, fontSize = 12.sp, letterSpacing = 1.8.sp)
        Spacer(Modifier.height(38.dp))
        Text("DOSSIÊ 001", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(12.dp))
        Text("O Desaparecimento de Blackwood", color = Pale, fontSize = 31.sp, lineHeight = 38.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(24.dp))
        Text("BLACKWOOD  /  02:17", color = Gold, fontSize = 11.sp, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(14.dp))
        Text(
            "A polícia encontrou a casa aberta. O jantar ainda estava sobre a mesa. Não havia sinais de arrombamento, luta ou fuga. Elias Blackwood desapareceu sem deixar explicações.",
            color = Pale,
            fontSize = 17.sp,
            lineHeight = 29.sp
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Sua investigação começa aqui. Esta primeira compilação valida a instalação e a navegação básica. As mecânicas de investigação, coleta de pistas e progressão do caso serão implementadas nas próximas etapas.",
            color = Muted,
            fontSize = 14.sp,
            lineHeight = 23.sp
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Gold)
        ) {
            Text("←  VOLTAR AO INÍCIO", fontWeight = FontWeight.Bold)
        }
    }
}
