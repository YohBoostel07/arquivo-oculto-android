package com.arquivoculto.game

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.ui.unit.sp

private val Night = Color(0xFF080D14)
private val Panel = Color(0xFF111C29)
private val Gold = Color(0xFFD7B477)
private val Pale = Color(0xFFF1EEE7)
private val Muted = Color(0xFFA6B0BC)
private val Line = Color(0xFF344355)

private data class Evidence(val id: Int, val type: String, val title: String, val place: String, val description: String, val detail: String)
private data class Person(val name: String, val role: String, val summary: String)
private data class Question(val title: String, val answer: String, val clue: String)
private data class BoardNode(val id: Int, val title: String, val subtitle: String, val kind: String)
private data class SceneObject(val id:Int,val title:String,val room:String,val description:String,val discovery:String,val requirement:Int?=null)

private val evidenceFiles = listOf(
    Evidence(1,"DOCUMENTO","Relatório da polícia","Residência Blackwood","O relatório inicial da noite do desaparecimento.","Registro: 14 de outubro, 02h17. A porta da frente estava destrancada. Uma xícara de chá permanecia morna. Nenhuma janela foi quebrada. O relógio da sala parou às 23h46."),
    Evidence(2,"FOTOGRAFIA","A mesa de jantar","Sala de jantar","Dois pratos foram encontrados na casa de Elias.","A fotografia mostra dois pratos, embora Elias morasse sozinho. Há marcas de água em um copo e uma cadeira afastada. No verso: 'Ele estava esperando por alguém'."),
    Evidence(3,"CARTA","Bilhete sem assinatura","Escritório","Papel dobrado sob o tapete da entrada.","A mensagem diz: 'Não venha pela estrada principal. A luz da estação ainda está acesa.' A caligrafia não foi identificada. O papel tem cheiro de fumaça e barro escuro."),
    Evidence(4,"REGISTRO","Ligação interrompida","Central telefônica","Última chamada registrada no telefone da casa.","Às 23h39, uma chamada de 46 segundos foi feita para a antiga estação. A ligação terminou abruptamente. O número foi desativado há seis anos."),
    Evidence(5,"FOTOGRAFIA","Pegadas no jardim","Fundos da residência","Duas sequências de pegadas parcialmente preservadas.","Uma sequência é compatível com as botas de Elias. A segunda é menor e tem sola incomum. As marcas seguem até o portão dos fundos, não até a estrada.")
)
private val stationEvidence = listOf(
    Evidence(101,"DOCUMENTO","Planta de manutenção","Arquivo da estação","Planta antiga com corredor técnico marcado à mão.","A planta indica um corredor sob a plataforma 2. Uma anotação recente circula a entrada do túnel e marca 00h12."),
    Evidence(102,"FOTOGRAFIA","Luz na plataforma","Plataforma 2","Uma imagem noturna encontrada na guarita.","A luz da plataforma 2 está acesa. No reflexo da janela aparecem duas pessoas: uma alta e outra usando casaco claro."),
    Evidence(103,"OBJETO","Chave enferrujada T-04","Guarita","Chave presa a uma etiqueta quase apagada.","A chave tem resíduos de terra úmida. A etiqueta T-04 corresponde à porta técnica indicada na planta."),
    Evidence(104,"FOTOGRAFIA","Marcas no banco","Plataforma 2","Riscos recentes na madeira do banco.","As marcas formam as letras A.B. e parecem ter sido feitas pouco antes do abandono da estação."),
    Evidence(105,"OBJETO","Lanterna sem pilhas","Entrada do túnel","Uma lanterna caída junto à porta técnica.","Há barro escuro no aro e uma fibra de tecido azul presa na alça. A lanterna foi usada recentemente.")
)

private val sceneEvidence = listOf(
    Evidence(107,"DOCUMENTO","Bilhete de embarque rasgado","Plataforma 2","Um bilhete preso entre as tábuas do banco.","Emitido às 23h21 para uma viagem ausente dos registros oficiais. No verso há as letras E.B. e uma mancha de graxa."),
    Evidence(108,"MECANISMO","Alavanca do sinal","Plataforma 2","Uma alavanca de sinalização travada na posição manual.","O mecanismo foi acionado recentemente e aponta para o ramal de carga, desativado desde o fechamento da estação."),
    Evidence(109,"DOCUMENTO","Livro de turnos","Guarita","Um livro antigo escondido sob o balcão.","A página de 14 de outubro foi arrancada. Marcas na folha seguinte revelam: A.B. — acesso autorizado — 23h32."),
    Evidence(110,"OBJETO","Gaveta com fundo falso","Guarita","Uma gaveta parece mais funda do que deveria.","Atrás do fundo falso há uma fotografia de Elias ao lado de alguém cujo rosto foi raspado."),
    Evidence(111,"OBJETO","Grade solta no túnel","Túnel T-04","Uma grade de ventilação está parcialmente desprendida.","Atrás da grade há fibras de tecido azul e terra avermelhada. O espaço continua sob o ramal de carga."),
    Evidence(112,"DOCUMENTO","Mapa do ramal de carga","Sala de serviço","Um mapa dobrado dentro de um armário metálico.","O mapa marca um depósito fora do perímetro da estação. Uma anotação diz: entrada pelo lado norte.")
)
private val sceneObjects = listOf(
    SceneObject(107,"Entre as tábuas do banco","PLATAFORMA","Um bilhete está preso em uma fresta da madeira. Parece ter sido escondido às pressas.","Você retira um bilhete de embarque rasgado. O horário e as iniciais podem ligar Elias a uma viagem clandestina."),
    SceneObject(108,"Painel de sinalização","PLATAFORMA","O painel está empoeirado, mas uma das alavancas não tem poeira ao redor.","A alavanca foi usada recentemente e aponta para um ramal desativado.",102),
    SceneObject(109,"Livro sob o balcão","GUARITA","Um livro de turnos foi empurrado para trás de uma caixa de ferramentas.","As marcas deixadas na página seguinte revelam um acesso autorizado às 23h32."),
    SceneObject(110,"Gaveta trancada","GUARITA","A gaveta tem uma fechadura antiga e um fundo aparentemente irregular.","Você encontra um compartimento falso e uma fotografia parcialmente raspada.",103),
    SceneObject(111,"Grade de ventilação","TÚNEL","A grade está solta. Há marcas recentes de ferramenta nos parafusos.","Atrás da grade há fibras azuis e terra avermelhada. O túnel segue além da planta conhecida.",105),
    SceneObject(112,"Armário metálico","TÚNEL","O armário está no fim da sala de serviço e parece ter sido fechado recentemente.","Dentro há um mapa do ramal de carga e a indicação de um depósito ao norte.",106)
)

private val allEvidence = evidenceFiles + stationEvidence + sceneEvidence
private val people = listOf(
    Person("Marta Voss","Vizinha de Elias","Mora ao lado da residência e ouviu um veículo durante a noite."),
    Person("Inspetor Raul Mendes","Polícia local","Fez o primeiro registro da ocorrência e conhece o horário oficial."),
    Person("Arthur Bell","Ex-funcionário da estação","Conhece as passagens técnicas e trabalhou na estação até seu fechamento.")
)
private val questions = mapOf(
    "Marta Voss" to listOf(
        Question("O que ouviu naquela noite?","Ouvi um motor às 23h50. Não parecia um trem. Vi uma luz se mover na direção da estrada velha.","Marta confirma um veículo pouco depois da ligação das 23h39."),
        Question("Você conhecia o bilhete?","Elias me perguntou se a estação ainda tinha energia. Eu disse que não deveria haver luz alguma lá.","Marta sabia que a estação estava oficialmente desativada."),
        Question("Viu alguém com Elias?","Vi uma pessoa de casaco claro perto do portão dos fundos. Não consegui reconhecer o rosto.","A descrição combina com a segunda figura da fotografia da plataforma.")
    ),
    "Inspetor Raul Mendes" to listOf(
        Question("Por que o relógio parou?","O mecanismo estava intacto. O ponteiro foi parado manualmente, mas isso não constou no relatório inicial.","O relatório omitiu que o relógio foi manipulado."),
        Question("Quem tinha acesso à casa?","Arthur Bell esteve na residência dois dias antes. Disse que foi devolver um objeto de Elias.","Raul confirma uma visita não registrada no depoimento inicial."),
        Question("Por que não investigou a estação?","Recebi uma ordem para tratar o caso como desaparecimento voluntário. A ordem veio de um telefone interno sem identificação.","A investigação foi desviada antes de examinar a estação.")
    ),
    "Arthur Bell" to listOf(
        Question("O que sabe sobre o túnel T-04?","A chave T-04 abre a porta sob a plataforma 2. Só antigos funcionários sabiam disso.","Arthur conhece a chave e a passagem secreta."),
        Question("Esteve na estação naquela noite?","Não. A estação estava fechada e eu estava em casa antes das onze.","Arthur nega presença na estação."),
        Question("O que significam as letras A.B.?","Podem ser minhas iniciais, mas muita gente tem essas letras. Não lembro de ter escrito aquilo.","Arthur reconhece que as iniciais podem identificá-lo, mas evita explicar.")
    )
)
private val validLinks = setOf(setOf(3,4),setOf(101,103),setOf(102,5),setOf(104,106),setOf(105,103),setOf(4,-2),setOf(102,-1),setOf(103,-3),setOf(104,-3))
private val suspectContradictions = mapOf(
    "Marta Voss" to listOf("Disse não saber se a estação tinha energia, mas admitiu que Elias perguntou sobre a luz do local.","Seu relato do casaco claro coincide com a figura refletida na fotografia."),
    "Inspetor Raul Mendes" to listOf("O relatório não menciona que o relógio foi parado manualmente.","A ordem para não investigar a estação contradiz o procedimento normal."),
    "Arthur Bell" to listOf("Negou ter ido à estação, mas conhece detalhes específicos da chave T-04.","As iniciais A.B. foram encontradas no banco da plataforma.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8,13,20)
        window.navigationBarColor = android.graphics.Color.rgb(8,13,20)
        setContent { MaterialTheme { Surface(color=Night) { ArquivoOcultoApp() } } }
    }
}

@Composable private fun ArquivoOcultoApp() {
    var showingCase by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF172333),Night,Color(0xFF05080D))))) {
        AnimatedContent(showingCase, transitionSpec={ fadeIn() togetherWith fadeOut() }, label="screen") {
            if (it) CaseScreen { showingCase=false } else HomeScreen { showingCase=true }
        }
    }
}
@Composable private fun HomeScreen(onOpen:()->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(26.dp)) {
        BrandHeader()
        Spacer(Modifier.height(60.dp))
        Text("DOSSIÊ 001  •  ACESSO RESTRITO",color=Gold,fontSize=11.sp,letterSpacing=2.sp)
        Spacer(Modifier.height(14.dp))
        SceneIllustration("CASA", Modifier.fillMaxWidth().height(210.dp))
        Spacer(Modifier.height(16.dp))
        Text("Toda verdade\ndeixa rastros.",color=Pale,fontSize=43.sp,lineHeight=49.sp,fontWeight=FontWeight.Light)
        Spacer(Modifier.height(16.dp))
        Text("Investigue casos inexplicáveis. Reúna evidências. Descubra o que realmente aconteceu.",color=Muted,fontSize=16.sp,lineHeight=25.sp)
        Spacer(Modifier.height(32.dp))
        Column(Modifier.fillMaxWidth().border(1.dp,Gold.copy(alpha=.45f),RoundedCornerShape(18.dp)).background(Panel,RoundedCornerShape(18.dp)).padding(22.dp)) {
            Text("PRIMEIRO CASO  /  INVESTIGAÇÃO",color=Gold,fontSize=10.sp)
            Spacer(Modifier.height(14.dp))
            Text("O Desaparecimento\nde Blackwood",color=Pale,fontSize=27.sp,lineHeight=33.sp,fontWeight=FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Text("Uma cidade pequena. Uma noite sem testemunhas. Uma pessoa que parece nunca ter existido.",color=Muted,fontSize=14.sp,lineHeight=22.sp)
            Spacer(Modifier.height(22.dp))
            GoldButton("CONTINUAR INVESTIGAÇÃO  →",onOpen)
        }
        Spacer(Modifier.height(24.dp))
        Text("VERSÃO 0.9  •  EXPLORAÇÃO DE AMBIENTES",color=Muted,fontSize=10.sp,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center)
    }
}
@Composable private fun BrandHeader() {
    Row(verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).border(1.dp,Gold,RoundedCornerShape(12.dp)),contentAlignment=Alignment.Center){ Text("AO",color=Gold,fontWeight=FontWeight.Bold) }
        Column(Modifier.padding(start=12.dp)) {
            Text("ARQUIVO OCULTO",color=Pale,fontSize=14.sp,fontWeight=FontWeight.Bold,letterSpacing=2.sp)
            Text("DEPARTAMENTO DE INVESTIGAÇÃO",color=Muted,fontSize=9.sp,letterSpacing=1.sp)
        }
    }
}
@Composable private fun GoldButton(label:String,onClick:()->Unit) {
    Button(onClick=onClick,modifier=Modifier.fillMaxWidth().height(52.dp),shape=RoundedCornerShape(10.dp),colors=ButtonDefaults.buttonColors(containerColor=Gold,contentColor=Night)){ Text(label,fontWeight=FontWeight.Bold,letterSpacing=.5.sp) }
}
@Composable private fun CaseScreen(onBack:()->Unit) {
    val context= LocalContext.current
    val prefs= remember(context){context.getSharedPreferences("arquivo_oculto_save",Context.MODE_PRIVATE)}
    var section by remember { mutableStateOf("DOSSIÊ") }
    var area by remember { mutableStateOf("PLATAFORMA") }
    var selectedEvidence by remember { mutableStateOf<Evidence?>(null) }
    var selectedSceneObject by remember { mutableStateOf<SceneObject?>(null) }
    var examined by remember { mutableStateOf(prefs.getStringSet("examined",emptySet())?.mapNotNull{it.toIntOrNull()}?.toSet()?:emptySet()) }
    var notes by remember { mutableStateOf(prefs.getString("notes","")?:"") }
    var solved by remember { mutableStateOf(prefs.getBoolean("station_unlocked",false)) }
    var selectedLinks by remember { mutableStateOf(emptySet<Int>()) }
    var confirmedLinks by remember { mutableStateOf(prefs.getStringSet("links",emptySet())?.toSet()?:emptySet()) }
    var interviewPerson by remember { mutableStateOf<String?>(null) }
    var interviewAnswers by remember { mutableStateOf(prefs.getStringSet("answers",emptySet())?.toSet()?:emptySet()) }
    var currentQuestion by remember { mutableStateOf<Question?>(null) }
    var selectedSuspect by remember { mutableStateOf("Arthur Bell") }
    var analysisDone by remember { mutableStateOf(prefs.getBoolean("analysis_done",false)) }
    var theory by remember { mutableStateOf(prefs.getString("theory","")?:"") }
    var accusation by remember { mutableStateOf(prefs.getString("accusation","")?:"") }
    var ending by remember { mutableStateOf(prefs.getString("ending","")?:"") }

    LaunchedEffect(examined,notes,solved,confirmedLinks,interviewAnswers,analysisDone,theory,accusation,ending) {
        prefs.edit().putStringSet("examined",examined.map{it.toString()}.toSet())
            .putString("notes",notes).putBoolean("station_unlocked",solved)
            .putStringSet("links",confirmedLinks).putStringSet("answers",interviewAnswers)
            .putBoolean("analysis_done",analysisDone).putString("theory",theory)
            .putString("accusation",accusation).putString("ending",ending).apply()
    }
    Column(Modifier.fillMaxSize().padding(horizontal=16.dp,vertical=16.dp)) {
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically) {
            Column { Text("ARQUIVO OCULTO  /  CASO 001",color=Gold,fontSize=10.sp,letterSpacing=1.sp); Text("BLACKWOOD",color=Pale,fontSize=22.sp,fontWeight=FontWeight.Bold,letterSpacing=2.sp) }
            TextButton(onClick=onBack){Text("SAIR ×",color=Muted)}
        }
        Spacer(Modifier.height(10.dp))
        val tabs=listOf("DOSSIÊ","PISTAS","CONEXÕES","LOCAIS","PESSOAS","ANÁLISE","ACUSAÇÃO","CADERNO")
        Column(verticalArrangement=Arrangement.spacedBy(5.dp)) {
            tabs.chunked(4).forEach { group ->
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    group.forEach { tab ->
                        Button(onClick={ if(tab!="LOCAIS"||solved) section=tab },modifier=Modifier.weight(1f).height(39.dp),contentPadding=PaddingValues(1.dp),shape=RoundedCornerShape(7.dp),colors=ButtonDefaults.buttonColors(containerColor=if(section==tab)Gold else Panel,contentColor=if(section==tab)Night else Muted,disabledContainerColor=Panel,disabledContentColor=Muted),enabled=tab!="LOCAIS"||solved){Text(tab,fontSize=8.sp,fontWeight=FontWeight.Bold)}
                    }
                    if(group.size<4) repeat(4-group.size){ Spacer(Modifier.weight(1f)) }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            when(section) {
                "DOSSIÊ" -> {
                    SectionTitle("O DESAPARECIMENTO DE BLACKWOOD")
                    Headline("A casa estava aberta. O jantar continuava servido. Elias Blackwood havia desaparecido.")
                    Body("14 DE OUTUBRO  •  BLACKWOOD  •  02H17\n\nQuando a polícia chegou, não encontrou sinais de arrombamento. O telefone estava fora do gancho e o relógio da sala havia parado. Os vizinhos afirmaram não ter ouvido gritos.")
                    InfoPanel("OBJETIVO","Descubra por que Elias saiu de casa, quem esteve com ele e para onde foi.")
                    InfoPanel("PROGRESSO","Evidências examinadas: ${examined.size}/${allEvidence.size}  •  Conexões confirmadas: ${confirmedLinks.size}  •  Perguntas feitas: ${interviewAnswers.size}")
                    if(solved) GoldButton("IR PARA A ESTAÇÃO FERROVIÁRIA →"){section="LOCAIS"}
                }
                "PISTAS" -> {
                    SectionTitle("ARQUIVO DE EVIDÊNCIAS")
                    Body("Abra documentos, fotografias e objetos. Toque novamente para rever os detalhes.")
                    allEvidence.forEach { EvidenceCard(it,it.id in examined){selectedEvidence=it} }
                }
                "CONEXÕES" -> {
                    SectionTitle("MURAL DE CONEXÕES")
                    Body("Arraste as evidências e as pessoas pelo mural. Toque em dois cartões para selecioná-los e analise a relação.")
                    InvestigationBoard(
                        evidence=allEvidence.filter { it.id in examined },
                        people=people,
                        selected=selectedLinks,
                        confirmed=confirmedLinks,
                        onToggle={ id -> selectedLinks=if(id in selectedLinks) selectedLinks-id else if(selectedLinks.size<2) selectedLinks+id else setOf(id) }
                    )
                    GoldButton("ANALISAR RELAÇÃO SELECIONADA") {
                        if(selectedLinks.size==2) {
                            val key=selectedLinks.sorted().joinToString(",")
                            if(validLinks.any { it==selectedLinks }) confirmedLinks=confirmedLinks+key
                            selectedLinks=emptySet()
                        }
                    }
                    Text("Relações confirmadas: ${confirmedLinks.size}",color=Gold,fontSize=12.sp)
                    if(confirmedLinks.isNotEmpty()) confirmedLinks.forEach { key ->
                        val ids=if(key.contains(",")) key.split(",").mapNotNull { it.toIntOrNull() } else key.split("-").mapNotNull { it.toIntOrNull() }
                        val labels=ids.mapNotNull { id -> allEvidence.find { it.id==id }?.title ?: people.getOrNull(-id-1)?.name }
                        Text("✓ "+labels.joinToString("  ↔  "),color=Color(0xFF9BC7A5),fontSize=12.sp)
                    }
                    InfoPanel("COMO INVESTIGAR","Organize as evidências no mural, aproxime depoimentos e documentos e procure relações sustentadas pelos detalhes do caso.")
                }
                "LOCAIS" -> {
                    SectionTitle("ESTAÇÃO FERROVIÁRIA")
                    SceneIllustration(area, Modifier.fillMaxWidth().height(238.dp))
                    Headline("A estação deveria estar abandonada. Mesmo assim, alguém esteve aqui depois da meia-noite.")
                    Body("O portão lateral está entreaberto. Há marcas recentes na poeira e uma lâmpada continua acesa.")
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp),modifier=Modifier.fillMaxWidth()) {
                        listOf("PLATAFORMA","GUARITA","TÚNEL").forEach { loc -> Button(onClick={area=loc},modifier=Modifier.weight(1f),contentPadding=PaddingValues(2.dp),colors=ButtonDefaults.buttonColors(containerColor=if(area==loc)Gold else Panel,contentColor=if(area==loc)Night else Pale)){Text(loc,fontSize=9.sp)} }
                    }
                    Spacer(Modifier.height(12.dp))
                    val areaEvidence=when(area){"PLATAFORMA"->stationEvidence.filter{it.id==102||it.id==104};"GUARITA"->stationEvidence.filter{it.id==103};else->stationEvidence.filter{it.id==101||it.id==105}}
                    InfoPanel("ÁREA: $area",when(area){"PLATAFORMA"->"A luz ainda funciona. Há marcas recentes no banco e sinais de que duas pessoas estiveram aqui.";"GUARITA"->"A guarita está revirada. Um molho de chaves parece ter sido escondido às pressas.";else->"A porta técnica está fechada. A planta e a chave podem revelar como entrar."})
                    areaEvidence.forEach { EvidenceCard(it,it.id in examined){selectedEvidence=it} }
                    Spacer(Modifier.height(8.dp))
                    SectionTitle("OBJETOS INTERATIVOS")
                    Body("Explore os detalhes do ambiente. Alguns objetos escondem evidências e exigem que você encontre a pista certa.")
                    sceneObjects.filter { it.room==area }.forEach { obj ->
                        SceneObjectCard(obj,obj.id in examined,obj.requirement==null || obj.requirement in examined) { selectedSceneObject=obj }
                    }
                    if(area=="TÚNEL"&&103 in examined) GoldButton("USAR CHAVE T-04 NA PORTA"){ examined=examined+106; selectedEvidence=Evidence(106,"LOCAL DESBLOQUEADO","Passagem subterrânea","Túnel T-04","A porta foi aberta com a chave encontrada na guarita.","O túnel segue sob os trilhos e termina em uma sala de serviço. Há marcas de arrasto no chão e um pedaço de tecido azul preso a uma grade.") }
                    if(106 in examined) InfoPanel("PASSAGEM ABERTA","Você encontrou a sala de serviço sob a estação. A investigação pode continuar.")
                }
                "PESSOAS" -> {
                    SectionTitle("INTERROGATÓRIOS")
                    Body("Escolha uma pessoa, selecione perguntas e compare as respostas com as evidências.")
                    people.forEach { p -> PersonCard(p){interviewPerson=p.name} }
                    InfoPanel("REGISTRO","Respostas registradas: ${interviewAnswers.size}/9")
                }
                "ANÁLISE" -> {
                    SectionTitle("SUSPEITOS E CONTRADIÇÕES")
                    Body("Compare álibis e depoimentos. A análise usa as respostas obtidas nos interrogatórios.")
                    people.forEach { p ->
                        Button(onClick={selectedSuspect=p.name},modifier=Modifier.fillMaxWidth().padding(vertical=3.dp),colors=ButtonDefaults.buttonColors(containerColor=if(selectedSuspect==p.name)Gold else Panel,contentColor=if(selectedSuspect==p.name)Night else Pale)){Text("${p.name}  •  ${p.role}",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Start)}
                    }
                    InfoPanel("ÁLIBI DECLARADO",when(selectedSuspect){"Marta Voss"->"Afirma estar em casa, mas relata ter visto um veículo e uma pessoa de casaco claro por volta da meia-noite.";"Inspetor Raul Mendes"->"Afirma ter seguido o procedimento, porém admite ter recebido uma ordem para não investigar a estação.";else->"Afirma estar em casa antes das 23h e nega ter visitado a estação na noite do desaparecimento."})
                    Text("CONTRADIÇÕES IDENTIFICADAS",color=Gold,fontSize=11.sp,fontWeight=FontWeight.Bold)
                    suspectContradictions[selectedSuspect]?.forEach { Body("• $it") }
                    val relevant=interviewAnswers.count{it.startsWith(selectedSuspect+"|")}
                    InfoPanel("CONFIABILIDADE DO DEPOIMENTO","Perguntas respondidas a esta pessoa: $relevant de 3. As contradições acima são pontos para investigar, não uma prova automática de culpa.")
                    GoldButton("CONCLUIR ANÁLISE DOS DEPOIMENTOS"){analysisDone=interviewAnswers.size>=3}
                    if(analysisDone) InfoPanel("ANÁLISE REGISTRADA","Você concluiu uma primeira comparação dos relatos. Use as conexões e evidências antes de formular uma acusação.")
                }
                "ACUSAÇÃO" -> {
                    SectionTitle("ACUSAÇÃO FINAL")
                    Body("Chegou a hora de reconstruir a noite de 14 de outubro. Uma acusação precisa de evidências, depoimentos e uma sequência de fatos que faça sentido.")
                    if(ending.isNotBlank()) {
                        InfoPanel("DESFECHO REGISTRADO",ending)
                        GoldButton("REABRIR A INVESTIGAÇÃO"){ending="";accusation=""}
                    } else {
                        InfoPanel("REQUISITOS PARA UMA CONCLUSÃO","Registre as pistas da chave T-04, das iniciais A.B., do livro de turnos, da fotografia escondida e do mapa do ramal. Interrogue Arthur e o inspetor e conclua a análise dos depoimentos.")
                        val requiredEvidence=setOf(103,104,109,110,112)
                        val requiredAnswers=setOf("Arthur Bell|1","Arthur Bell|2","Inspetor Raul Mendes|1")
                        val evidenceReady=requiredEvidence.all { it in examined }
                        val interviewsReady=requiredAnswers.all { it in interviewAnswers }
                        InfoPanel("EVIDÊNCIAS",if(evidenceReady) "✓ Todas as cinco pistas decisivas foram registradas." else "Faltam ${requiredEvidence.count { it !in examined }} das cinco pistas decisivas.")
                        InfoPanel("DEPOIMENTOS",if(interviewsReady) "✓ Os depoimentos essenciais foram obtidos." else "Ainda faltam respostas essenciais nos interrogatórios.")
                        InfoPanel("ANÁLISE",if(analysisDone) "✓ Comparação dos depoimentos concluída." else "Conclua a análise na aba ANÁLISE.")
                        SectionTitle("QUEM VOCÊ ACUSA?")
                        people.forEach { person ->
                            Button(onClick={accusation=person.name},modifier=Modifier.fillMaxWidth().padding(vertical=4.dp),colors=ButtonDefaults.buttonColors(containerColor=if(accusation==person.name)Gold else Panel,contentColor=if(accusation==person.name)Night else Pale)) {
                                Text(person.name,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Start,fontWeight=FontWeight.SemiBold)
                            }
                        }
                        GoldButton("APRESENTAR ACUSAÇÃO") {
                            if(accusation.isNotBlank()) {
                                ending=when {
                                    accusation=="Arthur Bell" && evidenceReady && interviewsReady && analysisDone ->
                                        "CONCLUSÃO: ARTHUR BELL FOI O ELO ENTRE ELIAS E A ESTAÇÃO.\\n\\nA chave T-04, o registro de acesso às 23h32, as iniciais no banco e a fotografia escondida desmontam o álibi de Arthur. Confrontado com o mapa do ramal, ele admite que ajudou Elias a chegar ao depósito do norte.\\n\\nElias não foi levado à força: planejou desaparecer depois de descobrir uma rede clandestina que operava pela antiga estação. Arthur ajudou a apagar os rastros. O inspetor Raul recebeu ordens para encerrar a investigação antes que a rota fosse descoberta.\\n\\nCASO 001 CONCLUÍDO — O desaparecimento foi planejado, mas a rede por trás dele continua sem identificação."
                                    accusation=="Arthur Bell" ->
                                        "CONCLUSÃO INCOMPLETA: Arthur Bell continua sendo o principal suspeito, mas as provas reunidas não sustentam uma acusação definitiva. O depoimento dele apresenta contradições, porém faltam registros e confirmações. O caso permanece oficialmente aberto."
                                    else ->
                                        "ACUSAÇÃO CONTESTADA: as evidências da estação não sustentam a acusação contra $accusation. A linha do tempo continua apontando para a passagem técnica e para o acesso registrado às 23h32. A acusação foi arquivada por falta de correspondência entre suspeita e provas. O caso permanece aberto."
                                }
                            }
                        }
                    }
                }
                "CADERNO" -> {
                    SectionTitle("CADERNO DO INVESTIGADOR")
                    Body("Anote horários, detalhes e contradições. Suas anotações são salvas automaticamente no aparelho.")
                    OutlinedTextField(value=notes,onValueChange={notes=it},modifier=Modifier.fillMaxWidth().height(170.dp),placeholder={Text("Escreva suas observações...",color=Muted)},minLines=4)
                    Spacer(Modifier.height(14.dp))
                    InfoPanel("DEDUÇÃO INICIAL","Qual pista indica o próximo local importante?")
                    listOf("A delegacia de Blackwood.","A segunda pessoa encontrou Elias na estação ferroviária.","O hospital da cidade.").forEach { option ->
                        Button(onClick={theory=option},modifier=Modifier.fillMaxWidth().padding(vertical=3.dp),colors=ButtonDefaults.buttonColors(containerColor=if(theory==option)Gold else Panel,contentColor=if(theory==option)Night else Pale)){Text(option,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Start)}
                    }
                    GoldButton("VERIFICAR DEDUÇÃO"){if(theory=="A segunda pessoa encontrou Elias na estação ferroviária."&&examined.size>=3){solved=true}}
                    if(solved) InfoPanel("ESTAÇÃO DESBLOQUEADA","A dedução foi confirmada. A estação ferroviária está disponível na aba LOCAIS.")
                }
            }
        }
    }
    selectedSceneObject?.let { obj ->
        AlertDialog(
            onDismissRequest={selectedSceneObject=null},
            containerColor=Panel,
            title={Text(obj.title,color=Pale,fontWeight=FontWeight.Bold)},
            text={Column {
                Text("OBJETO INTERATIVO  •  ${obj.room}",color=Gold,fontSize=10.sp)
                Spacer(Modifier.height(8.dp))
                Text(obj.description,color=Pale,lineHeight=21.sp)
                if(obj.id in examined) { Spacer(Modifier.height(10.dp)); Text(obj.discovery,color=Muted,lineHeight=21.sp) }
            }},
            confirmButton={TextButton(onClick={
                if(obj.requirement==null || obj.requirement in examined) {
                    examined=examined+obj.id
                    selectedEvidence=sceneEvidence.firstOrNull { it.id==obj.id }
                }
                selectedSceneObject=null
            }) { Text(if(obj.id in examined) "FECHAR" else if(obj.requirement!=null && obj.requirement !in examined) "PISTA NECESSÁRIA" else "EXAMINAR",color=Gold) }}
        )
    }
    selectedEvidence?.let { item ->
        AlertDialog(onDismissRequest={selectedEvidence=null},containerColor=Panel,title={Text(item.title,color=Pale,fontWeight=FontWeight.Bold)},text={Column{Text(item.type+"  •  "+item.place.uppercase(),color=Gold,fontSize=10.sp);Spacer(Modifier.height(10.dp));Text(item.detail,color=Pale,lineHeight=23.sp)}},confirmButton={TextButton(onClick={examined=examined+item.id;selectedEvidence=null}){Text(if(item.id in examined)"FECHAR" else "REGISTRAR EVIDÊNCIA",color=Gold)}})
    }
    interviewPerson?.let { name ->
        val qs=questions[name]?:emptyList()
        AlertDialog(onDismissRequest={interviewPerson=null;currentQuestion=null},containerColor=Panel,title={Text("INTERROGATÓRIO  /  $name",color=Gold,fontSize=16.sp)},text={
            Column(Modifier.heightIn(max=430.dp).verticalScroll(rememberScrollState())) {
                qs.forEachIndexed { index,q ->
                    val key="$name|$index"
                    Button(onClick={currentQuestion=q;interviewAnswers=interviewAnswers+key},modifier=Modifier.fillMaxWidth().padding(vertical=3.dp),colors=ButtonDefaults.buttonColors(containerColor=if(key in interviewAnswers)Color(0xFF294238) else Color(0xFF1B2938),contentColor=Pale)){Text(q.title,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Start)}
                    if(currentQuestion==q) {
                        Text(q.answer,color=Pale,fontSize=13.sp,lineHeight=21.sp)
                        Text("ANOTAÇÃO: ${q.clue}",color=Gold,fontSize=11.sp,lineHeight=17.sp)
                    }
                }
            }
        },confirmButton={TextButton(onClick={interviewPerson=null;currentQuestion=null}){Text("ENCERRAR",color=Gold)}})
    }
}

@Composable private fun SceneObjectCard(obj:SceneObject,examined:Boolean,available:Boolean,onClick:()->Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical=4.dp)
            .border(1.dp,if(examined)Gold.copy(alpha=.55f) else Line,RoundedCornerShape(11.dp))
            .background(Panel,RoundedCornerShape(11.dp)).clickable(onClick=onClick).padding(13.dp),
        verticalAlignment=Alignment.CenterVertically
    ) {
        Box(Modifier.size(38.dp).border(1.dp,if(available)Gold.copy(alpha=.6f) else Line,RoundedCornerShape(9.dp)),contentAlignment=Alignment.Center) {
            Text(if(examined) "✓" else "⌕",color=if(available)Gold else Muted,fontSize=19.sp,fontWeight=FontWeight.Bold)
        }
        Column(Modifier.weight(1f).padding(start=11.dp)) {
            Text(obj.title,color=Pale,fontSize=14.sp,fontWeight=FontWeight.SemiBold)
            Text(if(examined) "EVIDÊNCIA REGISTRADA" else if(!available) "REQUER OUTRA EVIDÊNCIA" else "EXAMINAR OBJETO",color=if(examined)Color(0xFF9BC7A5) else Gold,fontSize=9.sp,letterSpacing=.6.sp)
        }
        Text("›",color=Gold,fontSize=24.sp)
    }
}

@Composable private fun InvestigationBoard(
    evidence: List<Evidence>,
    people: List<Person>,
    selected: Set<Int>,
    confirmed: Set<String>,
    onToggle: (Int) -> Unit
) {
    val nodes = remember(evidence, people) {
        evidence.map { BoardNode(it.id,it.title,it.type+" • "+it.place,"EVIDÊNCIA") } +
            people.mapIndexed { index,p -> BoardNode(-(index+1),p.name,p.role,"PESSOA") }
    }
    val positions = remember { androidx.compose.runtime.mutableStateMapOf<Int,Offset>() }
    val density = androidx.compose.ui.platform.LocalDensity.current
    BoxWithConstraints(Modifier.fillMaxWidth().height(760.dp).border(1.dp,Gold.copy(alpha=.35f),RoundedCornerShape(12.dp)).background(Color(0xFF0B131E),RoundedCornerShape(12.dp))) {
        val cardWidth=(maxWidth-28.dp)/2
        Canvas(Modifier.fillMaxSize()) {
            for (x in 0..size.width.toInt() step 28) drawLine(Color(0xFF263443).copy(alpha=.34f),Offset(x.toFloat(),0f),Offset(x.toFloat(),size.height),strokeWidth=1f)
            for (y in 0..size.height.toInt() step 28) drawLine(Color(0xFF263443).copy(alpha=.34f),Offset(0f,y.toFloat()),Offset(size.width,y.toFloat()),strokeWidth=1f)
            confirmed.forEach { key ->
                val ids=if(key.contains(",")) key.split(",").mapNotNull { it.toIntOrNull() } else key.split("-").mapNotNull { it.toIntOrNull() }
                if(ids.size==2) {
                    val a=nodes.indexOfFirst { it.id==ids[0] }; val b=nodes.indexOfFirst { it.id==ids[1] }
                    if(a>=0 && b>=0) {
                        val pa=positions[ids[0]] ?: defaultBoardOffset(a,maxWidth.value)
                        val pb=positions[ids[1]] ?: defaultBoardOffset(b,maxWidth.value)
                        drawLine(Gold,Offset((pa.x+cardWidth.value/2)*density.density,(pa.y+42f)*density.density),Offset((pb.x+cardWidth.value/2)*density.density,(pb.y+42f)*density.density),strokeWidth=3.dp.toPx())
                    }
                }
            }
        }
        nodes.forEachIndexed { index,node ->
            val fallback=defaultBoardOffset(index,maxWidth.value)
            val position=positions[node.id] ?: fallback
            Column(
                Modifier.offset { IntOffset((position.x*density.density).roundToInt(),(position.y*density.density).roundToInt()) }
                    .width(cardWidth).heightIn(min=78.dp)
                    .pointerInput(node.id) {
                        detectDragGestures { change,dragAmount ->
                            change.consume()
                            val current=positions[node.id] ?: fallback
                            val maxX=(maxWidth-cardWidth).value.coerceAtLeast(0f)
                            val maxY=670f
                            positions[node.id]=Offset((current.x+dragAmount.x/density.density).coerceIn(0f,maxX),(current.y+dragAmount.y/density.density).coerceIn(0f,maxY))
                        }
                    }
                    .clickable { onToggle(node.id) }
                    .border(1.dp,if(node.id in selected)Gold else if(node.id<0)Color(0xFF7187A0) else Line,RoundedCornerShape(9.dp))
                    .background(if(node.id in selected)Color(0xFF3A3428) else Panel,RoundedCornerShape(9.dp))
                    .padding(9.dp)
            ) {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.size(9.dp).background(if(node.id<0)Color(0xFF8DA8C4) else Gold,RoundedCornerShape(50)))
                    Text("  "+node.kind,color=Gold,fontSize=8.sp,letterSpacing=.8.sp,fontWeight=FontWeight.Bold)
                }
                Spacer(Modifier.height(4.dp))
                Text(node.title,color=Pale,fontSize=12.sp,lineHeight=15.sp,fontWeight=FontWeight.SemiBold,maxLines=2)
                Text(node.subtitle,color=Muted,fontSize=9.sp,lineHeight=12.sp,maxLines=2)
                Text(if(node.id in selected)"SELECIONADO" else "TOQUE / ARRASTE",color=if(node.id in selected)Gold else Muted,fontSize=7.sp)
            }
        }
    }
    Text("Evidências examinadas: ${evidence.size}  •  Pessoas: ${people.size}",color=Muted,fontSize=10.sp,modifier=Modifier.padding(top=7.dp))
}

private fun defaultBoardOffset(index:Int,width:Float):Offset {
    val x=if(index%2==0)8f else width/2f+4f
    val y=8f+(index/2)*118f
    return Offset(x,y)
}


@Composable private fun SceneIllustration(area:String, modifier:Modifier=Modifier) {
    Canvas(modifier.border(1.dp,Gold.copy(alpha=.55f),RoundedCornerShape(13.dp))) {
        val w=size.width; val h=size.height
        drawRect(Brush.verticalGradient(listOf(Color(0xFF101D2C),Color(0xFF263445),Color(0xFF090E15))))
        drawCircle(Color(0xFFD9D1B7).copy(alpha=.75f),radius=19f,center=Offset(w*.79f,h*.20f))
        drawRect(Color(0xFF101722).copy(alpha=.58f),Offset(0f,h*.52f),androidx.compose.ui.geometry.Size(w,h*.48f))
        if(area=="CASA") {
            drawRect(Color(0xFF171D25),Offset(w*.27f,h*.36f),androidx.compose.ui.geometry.Size(w*.48f,h*.43f))
            val roof=androidx.compose.ui.graphics.Path().apply { moveTo(w*.22f,h*.38f);lineTo(w*.51f,h*.12f);lineTo(w*.80f,h*.38f);close() }
            drawPath(roof,Color(0xFF090D13))
            drawRect(Color(0xFFB88B50),Offset(w*.43f,h*.49f),androidx.compose.ui.geometry.Size(w*.12f,h*.30f))
            drawRect(Color(0xFFE6B96F),Offset(w*.61f,h*.47f),androidx.compose.ui.geometry.Size(w*.09f,h*.12f))
        } else {
            drawRect(Color(0xFF111923),Offset(w*.13f,h*.39f),androidx.compose.ui.geometry.Size(w*.74f,h*.36f))
            val roof=androidx.compose.ui.graphics.Path().apply { moveTo(w*.08f,h*.40f);lineTo(w*.50f,h*.20f);lineTo(w*.92f,h*.40f);close() }
            drawPath(roof,Color(0xFF080D13))
            drawRect(Color(0xFFB68A4E).copy(alpha=.85f),Offset(w*.45f,h*.47f),androidx.compose.ui.geometry.Size(w*.10f,h*.22f))
            for(i in 0..3) drawRect(Color(0xFFE4B96F).copy(alpha=.78f),Offset(w*(.22f+i*.16f),h*.48f),androidx.compose.ui.geometry.Size(w*.07f,h*.09f))
            if(area=="PLATAFORMA" || area=="TÚNEL") {
                drawLine(Color(0xFF687584),Offset(w*.12f,h*.91f),Offset(w*.48f,h*.72f),strokeWidth=5f)
                drawLine(Color(0xFF687584),Offset(w*.88f,h*.91f),Offset(w*.52f,h*.72f),strokeWidth=5f)
                for(i in 0..5) drawLine(Color(0xFF9A8B70).copy(alpha=.75f),Offset(w*(.18f+i*.12f),h*(.88f-i*.027f)),Offset(w*(.82f-i*.12f),h*(.88f-i*.027f)),strokeWidth=3f)
            }
            if(area=="GUARITA") {
                drawRect(Color(0xFF080D13),Offset(w*.68f,h*.57f),androidx.compose.ui.geometry.Size(w*.13f,h*.18f))
                drawCircle(Gold.copy(alpha=.8f),radius=7f,center=Offset(w*.75f,h*.65f))
            }
        }
        drawRect(Brush.horizontalGradient(listOf(Color.Black.copy(alpha=.34f),Color.Transparent,Color.Black.copy(alpha=.34f))))
        drawRoundRect(Color(0xCC080D14),topLeft=Offset(12f,h-37f),size=androidx.compose.ui.geometry.Size(w*.55f,25f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(5f))
    }
    Text(area.uppercase(),color=Gold,fontSize=9.sp,letterSpacing=1.6.sp,modifier=Modifier.padding(start=13.dp,top=6.dp))
}
@Composable private fun EvidenceThumbnail(type:String,modifier:Modifier=Modifier) {
    Canvas(modifier.border(1.dp,Gold.copy(alpha=.4f),RoundedCornerShape(7.dp)).background(Color(0xFF202A34),RoundedCornerShape(7.dp))) {
        val w=size.width; val h=size.height
        when(type) {
            "FOTOGRAFIA" -> {
                drawRect(Color(0xFFB5A88B),Offset(w*.12f,h*.12f),androidx.compose.ui.geometry.Size(w*.76f,h*.76f))
                drawRect(Color(0xFF344657),Offset(w*.2f,h*.22f),androidx.compose.ui.geometry.Size(w*.6f,h*.5f))
                drawCircle(Color(0xFFD7B477),radius=7f,center=Offset(w*.68f,h*.35f))
                drawLine(Color(0xFF111923),Offset(w*.24f,h*.68f),Offset(w*.74f,h*.48f),strokeWidth=3f)
            }
            "DOCUMENTO","CARTA","REGISTRO" -> {
                drawRect(Color(0xFFD8CFB8),Offset(w*.24f,h*.08f),androidx.compose.ui.geometry.Size(w*.52f,h*.84f))
                for(i in 0..4) drawLine(Color(0xFF6E6A60),Offset(w*.31f,h*(.25f+i*.12f)),Offset(w*.68f,h*(.25f+i*.12f)),strokeWidth=2f)
            }
            else -> {
                drawCircle(Color(0xFF9B8054),radius=w*.25f,center=Offset(w*.5f,h*.48f))
                drawCircle(Color(0xFF252B32),radius=w*.16f,center=Offset(w*.5f,h*.48f))
                drawLine(Gold,Offset(w*.5f,h*.12f),Offset(w*.5f,h*.82f),strokeWidth=2f)
            }
        }
    }
}
@Composable private fun PersonPortrait(name:String,modifier:Modifier=Modifier) {
    Canvas(modifier.border(1.dp,Gold.copy(alpha=.5f),RoundedCornerShape(9.dp)).background(Color(0xFF263444),RoundedCornerShape(9.dp))) {
        val w=size.width; val h=size.height
        drawRect(Brush.verticalGradient(listOf(Color(0xFF536477),Color(0xFF111923))))
        drawCircle(Color(0xFFD0B18A),radius=w*.22f,center=Offset(w*.5f,h*.34f))
        drawOval(Color(0xFF18212C),topLeft=Offset(w*.18f,h*.52f),size=androidx.compose.ui.geometry.Size(w*.64f,h*.54f))
        drawRect(Color(0xFF111923).copy(alpha=.6f),Offset(0f,h*.78f),androidx.compose.ui.geometry.Size(w,h*.22f))
    }
}

@Composable private fun SectionTitle(value:String){Spacer(Modifier.height(7.dp));Text(value,color=Gold,fontSize=11.sp,letterSpacing=1.4.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(10.dp))}
@Composable private fun Headline(value:String){Text(value,color=Pale,fontSize=22.sp,lineHeight=30.sp,fontWeight=FontWeight.SemiBold);Spacer(Modifier.height(12.dp))}
@Composable private fun Body(value:String){Text(value,color=Muted,fontSize=14.sp,lineHeight=23.sp);Spacer(Modifier.height(12.dp))}
@Composable private fun InfoPanel(title:String,body:String){
    Column(Modifier.fillMaxWidth().padding(vertical=5.dp).border(1.dp,Gold.copy(alpha=.35f),RoundedCornerShape(10.dp)).background(Color(0xFF182433),RoundedCornerShape(10.dp)).padding(13.dp)){
        Text(title,color=Gold,fontSize=10.sp,letterSpacing=1.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(5.dp));Text(body,color=Pale,fontSize=13.sp,lineHeight=20.sp)
    }
}
@Composable private fun EvidenceCard(item:Evidence,examined:Boolean,onClick:()->Unit){
    Column(Modifier.fillMaxWidth().padding(vertical=5.dp).border(1.dp,Line,RoundedCornerShape(12.dp)).background(Panel,RoundedCornerShape(12.dp)).clickable{onClick()}.padding(13.dp)){
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            EvidenceThumbnail(item.type,Modifier.size(76.dp,62.dp))
            Column(Modifier.weight(1f).padding(start=11.dp)) {
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(item.type,color=Gold,fontSize=9.sp,letterSpacing=1.sp);Text(if(examined)"EXAMINADA ✓" else "NÃO EXAMINADA",color=if(examined)Color(0xFF9BC7A5) else Muted,fontSize=9.sp)}
                Spacer(Modifier.height(6.dp));Text(item.title,color=Pale,fontSize=16.sp,fontWeight=FontWeight.SemiBold);Text(item.place,color=Muted,fontSize=11.sp)
            }
        }
        Spacer(Modifier.height(6.dp));Spacer(Modifier.height(5.dp));Text(item.description,color=Pale.copy(alpha=.85f),fontSize=12.sp,lineHeight=18.sp);Text("EXAMINAR DETALHES  →",color=Gold,fontSize=10.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=8.dp))
    }
}
@Composable private fun SelectableEvidence(item:Evidence,selected:Boolean,onClick:()->Unit){
    Row(Modifier.fillMaxWidth().padding(vertical=3.dp).border(1.dp,if(selected)Gold else Line,RoundedCornerShape(9.dp)).background(if(selected)Color(0xFF293343) else Panel,RoundedCornerShape(9.dp)).clickable{onClick()}.padding(11.dp),verticalAlignment=Alignment.CenterVertically){
        Text(if(selected)"☑" else "□",color=Gold,fontSize=18.sp);Column(Modifier.padding(start=10.dp)){Text(item.title,color=Pale,fontSize=13.sp,fontWeight=FontWeight.SemiBold);Text(item.type+" • "+item.place,color=Muted,fontSize=10.sp)}
    }
}
@Composable private fun PersonCard(person:Person,onClick:()->Unit){
    Row(Modifier.fillMaxWidth().padding(vertical=5.dp).border(1.dp,Line,RoundedCornerShape(12.dp)).background(Panel,RoundedCornerShape(12.dp)).padding(13.dp),verticalAlignment=Alignment.CenterVertically){
        PersonPortrait(person.name,Modifier.size(78.dp))
        Column(Modifier.weight(1f).padding(start=12.dp)) {
            Text("TESTEMUNHA / SUSPEITO",color=Gold,fontSize=9.sp);Spacer(Modifier.height(5.dp));Text(person.name,color=Pale,fontSize=17.sp,fontWeight=FontWeight.SemiBold);Text(person.role,color=Muted,fontSize=11.sp);Spacer(Modifier.height(6.dp));Text(person.summary,color=Pale,fontSize=12.sp,lineHeight=18.sp);TextButton(onClick=onClick,contentPadding=PaddingValues(0.dp)){Text("INTERROGAR →",color=Gold,fontSize=10.sp,fontWeight=FontWeight.Bold)}
        }
    }
}
