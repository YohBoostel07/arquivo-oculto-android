package com.arquivoculto.game

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Night = Color(0xFF080D14)
private val Panel = Color(0xFF111C29)
private val Gold = Color(0xFFD7B477)
private val Pale = Color(0xFFF1EEE7)
private val Muted = Color(0xFFA6B0BC)
private val Line = Color(0xFF344355)

private data class Evidence(val id: Int, val type: String, val title: String, val place: String, val description: String, val detail: String)

private val evidenceFiles = listOf(
    Evidence(1, "DOCUMENTO", "Relatório da polícia", "Residência Blackwood", "O relatório inicial da noite do desaparecimento.", "Registro: 14 de outubro, 02h17. A porta da frente estava destrancada. Uma xícara de chá permanecia sobre a mesa, ainda morna quando a equipe chegou. Nenhuma janela foi encontrada quebrada. O relógio da sala parou às 23h46."),
    Evidence(2, "FOTOGRAFIA", "A mesa de jantar", "Sala de jantar", "Uma fotografia feita pelos primeiros policiais no local.", "A fotografia mostra dois pratos, embora Elias morasse sozinho. Há marcas recentes de água em um dos copos e uma cadeira afastada da mesa. No verso, alguém escreveu: 'Ele estava esperando por alguém'."),
    Evidence(3, "CARTA", "Bilhete sem assinatura", "Escritório", "Um papel dobrado encontrado sob o tapete da entrada.", "A mensagem diz: 'Não venha pela estrada principal. A luz da estação ainda está acesa.' A caligrafia não foi identificada. O papel tem cheiro de fumaça e uma pequena mancha de barro escuro."),
    Evidence(4, "REGISTRO", "Ligação interrompida", "Central telefônica", "Última chamada registrada no telefone da casa.", "Às 23h39, uma chamada de 46 segundos foi feita para a antiga estação ferroviária. A ligação terminou abruptamente. O número de destino foi desativado há seis anos, pouco depois do fechamento da estação."),
    Evidence(5, "FOTOGRAFIA", "Pegadas no jardim", "Fundos da residência", "Marcas parcialmente preservadas pela chuva.", "Duas sequências de pegadas seguem até o portão dos fundos. Uma delas é compatível com as botas de Elias. A segunda é menor e apresenta um desenho de sola incomum. As marcas não seguem em direção à estrada.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8, 13, 20)
        window.navigationBarColor = android.graphics.Color.rgb(8, 13, 20)
        setContent { MaterialTheme { Surface(color = Night) { ArquivoOcultoApp() } } }
    }
}

@Composable
private fun ArquivoOcultoApp() {
    var showingCase by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF172333), Night, Color(0xFF05080D))))) {
        AnimatedContent(targetState = showingCase, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "tela") { open ->
            if (open) CaseScreen(onBack = { showingCase = false }) else HomeScreen(onOpen = { showingCase = true })
        }
    }
}

@Composable
private fun HomeScreen(onOpen: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 26.dp, vertical = 30.dp)) {
        BrandHeader()
        Spacer(Modifier.height(70.dp))
        Text("DOSSIÊ 001   •   ACESSO RESTRITO", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(17.dp))
        Text("Toda verdade\ndeixa rastros.", color = Pale, fontSize = 43.sp, lineHeight = 49.sp, fontWeight = FontWeight.Light)
        Spacer(Modifier.height(16.dp))
        Text("Investigue casos inexplicáveis. Reúna evidências. Descubra o que realmente aconteceu.", color = Muted, fontSize = 16.sp, lineHeight = 25.sp)
        Spacer(Modifier.height(36.dp))
        Column(Modifier.fillMaxWidth().border(1.dp, Gold.copy(alpha = .45f), RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(Color(0xFF1B2A3B), Panel)), RoundedCornerShape(18.dp)).padding(22.dp)) {
            Text("PRIMEIRO CASO  /  INVESTIGAÇÃO", color = Gold, fontSize = 10.sp, letterSpacing = 1.5.sp)
            Spacer(Modifier.height(16.dp))
            Text("O Desaparecimento\nde Blackwood", color = Pale, fontSize = 27.sp, lineHeight = 33.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Text("Uma cidade pequena. Uma noite sem testemunhas. Uma pessoa que parece nunca ter existido.", color = Muted, fontSize = 14.sp, lineHeight = 22.sp)
            Spacer(Modifier.height(24.dp))
            GoldButton("INICIAR INVESTIGAÇÃO   →", onOpen)
        }
        Spacer(Modifier.height(28.dp))
        Text("VERSÃO 0.2  •  PRIMEIRA ATUALIZAÇÃO JOGÁVEL", color = Color(0xFF657487), fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun BrandHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).border(1.dp, Gold.copy(alpha = .7f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Text("AO", color = Gold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text("ARQUIVO OCULTO", color = Pale, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("DEPARTAMENTO DE INVESTIGAÇÃO", color = Muted, fontSize = 9.sp, letterSpacing = 1.2.sp)
        }
    }
}

@Composable
private fun GoldButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Night)) {
        Text(label, fontWeight = FontWeight.Bold, letterSpacing = .7.sp)
    }
}

@Composable
private fun CaseScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember(context) { context.getSharedPreferences("arquivo_oculto_save", Context.MODE_PRIVATE) }
    var section by remember { mutableStateOf("DOSSIÊ") }
    var selectedEvidence by remember { mutableStateOf<Evidence?>(null) }
    var selectedPerson by remember { mutableStateOf<String?>(null) }
    var examined by remember { mutableStateOf(prefs.getStringSet("examined", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()) }
    var notes by remember { mutableStateOf(prefs.getString("notes", "") ?: "") }
    var theory by remember { mutableStateOf(prefs.getString("theory", "") ?: "") }
    var solved by remember { mutableStateOf(prefs.getBoolean("station_unlocked", false)) }

    LaunchedEffect(examined, notes, theory, solved) {
        prefs.edit()
            .putStringSet("examined", examined.map { it.toString() }.toSet())
            .putString("notes", notes)
            .putString("theory", theory)
            .putBoolean("station_unlocked", solved)
            .apply()
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("ARQUIVO OCULTO  /  CASO 001", color = Gold, fontSize = 10.sp, letterSpacing = 1.3.sp)
                Text("BLACKWOOD", color = Pale, fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
            TextButton(onClick = onBack) { Text("SAIR  ×", color = Muted) }
        }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            listOf("DOSSIÊ", "PISTAS", "PESSOAS", "CADERNO").forEach { tab ->
                Button(onClick = { section = tab }, modifier = Modifier.weight(1f).height(42.dp), contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = if (section == tab) Gold else Panel, contentColor = if (section == tab) Night else Muted)) {
                    Text(tab, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = .2.sp)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            when (section) {
                "DOSSIÊ" -> DossierContent(examined.size, examined, solved, onOpenEvidence = { selectedEvidence = it }, onGoClues = { section = "PISTAS" }, onGoStation = { section = "ESTAÇÃO" })
                "PISTAS" -> EvidenceContent(examined, onExamine = { selectedEvidence = it })
                "ESTAÇÃO" -> StationContent(examined, onExamine = { selectedEvidence = it })
                "PESSOAS" -> PeopleContent(onInterview = { selectedPerson = it })
                "CADERNO" -> NotebookContent(notes, { notes = it }, theory, { theory = it }, examined.size, solved, { solved = theory == "A segunda pessoa encontrou Elias na estação ferroviária." })
            }
        }
    }

    selectedEvidence?.let { item ->
        AlertDialog(onDismissRequest = { selectedEvidence = null }, containerColor = Panel,
            title = { Text(item.title, color = Pale, fontWeight = FontWeight.Bold) },
            text = { Column {
                Text(item.type + "  •  " + item.place.uppercase(), color = Gold, fontSize = 10.sp, letterSpacing = 1.sp)
                Spacer(Modifier.height(12.dp))
                Text(item.detail, color = Pale, lineHeight = 23.sp)
            } },
            confirmButton = { TextButton(onClick = { examined = examined + item.id; selectedEvidence = null }) { Text(if (item.id in examined) "FECHAR" else "REGISTRAR PISTA", color = Gold) } }
        )
    }

    selectedPerson?.let { person ->
        val statement = when (person) {
            "Marta Voss" -> "Sou a vizinha da casa ao lado. Ouvi um motor por volta da meia-noite, mas não vi o carro. Elias parecia assustado nos últimos dias. Ele me perguntou se a estação ainda recebia trens."
            "Inspetor Raul Mendes" -> "Fui o primeiro a entrar na residência. Não havia sinais de luta. O relógio parado às 23h46 me chamou a atenção, mas não posso afirmar se foi sabotado."
            else -> "Trabalhei na estação até seu fechamento. Elias esteve lá algumas vezes no último mês. Na última visita, perguntou sobre o túnel de manutenção que passa sob os trilhos."
        }
        AlertDialog(onDismissRequest = { selectedPerson = null }, containerColor = Panel,
            title = { Text("DEPOIMENTO  /  $person", color = Gold, fontSize = 16.sp) },
            text = { Text(statement, color = Pale, lineHeight = 23.sp) },
            confirmButton = { TextButton(onClick = { selectedPerson = null }) { Text("ENCERRAR", color = Gold) } }
        )
    }
}

@Composable
private fun DossierContent(examinedCount: Int, examined: Set<Int>, stationUnlocked: Boolean, onOpenEvidence: (Evidence) -> Unit, onGoClues: () -> Unit, onGoStation: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text("O DESAPARECIMENTO DE BLACKWOOD", color = Gold, fontSize = 10.sp, letterSpacing = 1.4.sp)
    Spacer(Modifier.height(10.dp))
    Text("A casa estava aberta. O jantar continuava servido. Elias Blackwood havia desaparecido.", color = Pale, fontSize = 23.sp, lineHeight = 31.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(14.dp))
    Text("14 DE OUTUBRO  •  BLACKWOOD  •  02H17", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp)
    Spacer(Modifier.height(18.dp))
    Text("Quando a polícia chegou, não encontrou sinais de arrombamento. O telefone estava fora do gancho e o relógio da sala havia parado. Os vizinhos afirmaram não ter ouvido gritos.", color = Pale, fontSize = 15.sp, lineHeight = 25.sp)
    Spacer(Modifier.height(18.dp))
    InfoPanel("OBJETIVO", "Descubra por que Elias saiu de casa, quem esteve com ele naquela noite e para onde foi.")
    Spacer(Modifier.height(18.dp))
    Text("PRIMEIRAS EVIDÊNCIAS", color = Gold, fontSize = 11.sp, letterSpacing = 1.3.sp)
    Spacer(Modifier.height(8.dp))
    evidenceFiles.take(2).forEach { item -> EvidenceCard(item, item.id in examined, onClick = { onOpenEvidence(item) }) }
    Spacer(Modifier.height(10.dp))
    Text("Evidências examinadas: $examinedCount / 5", color = Muted, fontSize = 12.sp)
    GoldButton("ABRIR MURAL DE PISTAS", onGoClues)
    if (stationUnlocked) {
        Spacer(Modifier.height(14.dp))
        InfoPanel("NOVO LOCAL DESBLOQUEADO", "A dedução foi confirmada. A antiga estação ferroviária agora pode ser investigada.")
        Spacer(Modifier.height(10.dp))
        GoldButton("INVESTIGAR A ESTAÇÃO  →", onGoStation)
    }
}

private val stationEvidence = listOf(
    Evidence(101, "DOCUMENTO", "Planta de manutenção", "Arquivo da estação", "Uma planta antiga com um corredor técnico marcado à mão.", "A planta indica um corredor de manutenção sob a plataforma 2. Uma anotação recente circula a entrada do túnel e marca o horário 00h12."),
    Evidence(102, "FOTOGRAFIA", "Luz na plataforma", "Plataforma 2", "Uma imagem noturna encontrada na guarita.", "Ao fundo da fotografia, uma luz aparece acesa na plataforma 2. No reflexo da janela é possível distinguir duas pessoas: uma alta e outra aparentemente usando um casaco claro."),
    Evidence(103, "OBJETO", "Chave enferrujada", "Guarita", "Uma chave presa a uma etiqueta quase apagada.", "A etiqueta ainda permite ler 'T-04'. A chave tem resíduos de terra úmida e parece abrir uma porta que ficou muito tempo sem uso.")
)

@Composable
private fun StationContent(examined: Set<Int>, onExamine: (Evidence) -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text("LOCAL 02  /  ESTAÇÃO FERROVIÁRIA", color = Gold, fontSize = 10.sp, letterSpacing = 1.4.sp)
    Spacer(Modifier.height(10.dp))
    Text("A estação deveria estar abandonada. Mesmo assim, alguém esteve aqui depois da meia-noite.", color = Pale, fontSize = 23.sp, lineHeight = 31.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(12.dp))
    Text("O portão lateral está entreaberto. Há marcas recentes de passos na poeira e uma lâmpada continua acesa sobre a plataforma 2.", color = Muted, fontSize = 14.sp, lineHeight = 23.sp)
    Spacer(Modifier.height(18.dp))
    InfoPanel("OBJETIVO", "Descubra o que liga Elias ao túnel de manutenção e identifique quem apareceu na plataforma naquela noite.")
    Spacer(Modifier.height(14.dp))
    stationEvidence.forEach { item -> EvidenceCard(item, item.id in examined, onClick = { onExamine(item) }) }
    Spacer(Modifier.height(8.dp))
    InfoPanel("REGISTRO", stationEvidence.count { it.id in examined }.toString() + " de 3 evidências da estação examinadas.")
}

@Composable
private fun EvidenceContent(examined: Set<Int>, onExamine: (Evidence) -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text("MURAL DE EVIDÊNCIAS", color = Gold, fontSize = 11.sp, letterSpacing = 1.5.sp)
    Spacer(Modifier.height(8.dp))
    Text("Examine cada item com atenção. Detalhes aparentemente pequenos podem mudar o rumo do caso.", color = Muted, fontSize = 14.sp, lineHeight = 22.sp)
    Spacer(Modifier.height(12.dp))
    evidenceFiles.forEach { item -> EvidenceCard(item, item.id in examined, onClick = { onExamine(item) }) }
    Spacer(Modifier.height(8.dp))
    InfoPanel("PROGRESSO", examined.size.toString() + " de 5 evidências examinadas. Registre suas observações no caderno.")
}

@Composable
private fun EvidenceCard(item: Evidence, examined: Boolean, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 5.dp).border(1.dp, Line, RoundedCornerShape(12.dp)).background(Panel, RoundedCornerShape(12.dp)).padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(item.type, color = Gold, fontSize = 9.sp, letterSpacing = 1.2.sp)
            Text(if (examined) "EXAMINADA ✓" else "NÃO EXAMINADA", color = if (examined) Color(0xFF9BC7A5) else Muted, fontSize = 9.sp)
        }
        Spacer(Modifier.height(7.dp))
        Text(item.title, color = Pale, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(item.place, color = Muted, fontSize = 11.sp)
        Spacer(Modifier.height(7.dp))
        Text(item.description, color = Pale.copy(alpha = .85f), fontSize = 13.sp, lineHeight = 19.sp)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onClick, contentPadding = PaddingValues(0.dp)) { Text(if (examined) "REEXAMINAR EVIDÊNCIA  →" else "EXAMINAR EVIDÊNCIA  →", color = Gold, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
    }
}

@Composable
private fun PeopleContent(onInterview: (String) -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text("PESSOAS RELACIONADAS", color = Gold, fontSize = 11.sp, letterSpacing = 1.4.sp)
    Spacer(Modifier.height(8.dp))
    Text("Converse com as pessoas envolvidas. Compare versões e procure contradições.", color = Muted, fontSize = 14.sp, lineHeight = 22.sp)
    Spacer(Modifier.height(12.dp))
    PersonCard("Marta Voss", "Vizinha de Elias", "Afirma ter ouvido um veículo durante a noite.", onInterview)
    PersonCard("Inspetor Raul Mendes", "Polícia local", "Responsável pelo primeiro relatório da ocorrência.", onInterview)
    PersonCard("Arthur Bell", "Ex-funcionário da estação", "Conhece a antiga estação e suas passagens de manutenção.", onInterview)
}

@Composable
private fun PersonCard(name: String, role: String, summary: String, onInterview: (String) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 5.dp).border(1.dp, Line, RoundedCornerShape(12.dp)).background(Panel, RoundedCornerShape(12.dp)).padding(14.dp)) {
        Text("TESTEMUNHA / SUSPEITO", color = Gold, fontSize = 9.sp, letterSpacing = 1.sp)
        Spacer(Modifier.height(5.dp))
        Text(name, color = Pale, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(role, color = Muted, fontSize = 11.sp)
        Spacer(Modifier.height(8.dp))
        Text(summary, color = Pale.copy(alpha = .85f), fontSize = 13.sp, lineHeight = 19.sp)
        TextButton(onClick = { onInterview(name) }, contentPadding = PaddingValues(0.dp)) { Text("INTERROGAR  →", color = Gold, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
    }
}

@Composable
private fun NotebookContent(notes: String, onNotesChange: (String) -> Unit, theory: String, onTheoryChange: (String) -> Unit, examinedCount: Int, solved: Boolean, onSubmit: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text("CADERNO DO INVESTIGADOR", color = Gold, fontSize = 11.sp, letterSpacing = 1.4.sp)
    Spacer(Modifier.height(8.dp))
    Text("Anote detalhes, horários e contradições. Suas anotações permanecem enquanto esta sessão estiver aberta.", color = Muted, fontSize = 13.sp, lineHeight = 21.sp)
    Spacer(Modifier.height(14.dp))
    Text("MINHAS ANOTAÇÕES", color = Pale, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(value = notes, onValueChange = onNotesChange, modifier = Modifier.fillMaxWidth().height(150.dp), placeholder = { Text("Escreva aqui suas observações...", color = Muted) }, minLines = 4)
    Spacer(Modifier.height(20.dp))
    InfoPanel("PRIMEIRA DEDUÇÃO", "Com base nos relatos, qual parece ser o próximo local importante para investigar?")
    Spacer(Modifier.height(10.dp))
    listOf("A delegacia de Blackwood.", "A segunda pessoa encontrou Elias na estação ferroviária.", "O hospital da cidade.").forEach { option ->
        Button(onClick = { onTheoryChange(option) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(9.dp), colors = ButtonDefaults.buttonColors(containerColor = if (theory == option) Gold else Panel, contentColor = if (theory == option) Night else Pale)) {
            Text(option, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, fontSize = 12.sp)
        }
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = onSubmit, enabled = examinedCount >= 3 && theory.isNotBlank(), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(9.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Night, disabledContainerColor = Line, disabledContentColor = Muted)) {
        Text("VERIFICAR DEDUÇÃO", fontWeight = FontWeight.Bold)
    }
    if (examinedCount < 3) {
        Spacer(Modifier.height(8.dp))
        Text("Examine pelo menos 3 evidências para liberar a verificação.", color = Muted, fontSize = 11.sp)
    }
    if (solved) {
        Spacer(Modifier.height(12.dp))
        InfoPanel("DEDUÇÃO CORRETA", "As pistas apontam para a antiga estação ferroviária. Uma nova etapa do caso foi desbloqueada: investigar o local.")
    } else if (theory.isNotBlank() && examinedCount >= 3) {
        Spacer(Modifier.height(8.dp))
        Text("A dedução ainda não está confirmada. Releia os depoimentos e as evidências.", color = Gold, fontSize = 12.sp)
    }
}

@Composable
private fun InfoPanel(title: String, body: String) {
    Column(Modifier.fillMaxWidth().border(1.dp, Gold.copy(alpha = .35f), RoundedCornerShape(10.dp)).background(Color(0xFF182433), RoundedCornerShape(10.dp)).padding(14.dp)) {
        Text(title, color = Gold, fontSize = 10.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(body, color = Pale, fontSize = 13.sp, lineHeight = 20.sp)
    }
}
