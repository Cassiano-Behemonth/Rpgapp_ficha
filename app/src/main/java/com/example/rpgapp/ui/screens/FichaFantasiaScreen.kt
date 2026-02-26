package com.example.rpgapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.math.cos
import androidx.compose.ui.text.style.TextAlign
import com.example.rpgapp.utils.DiceRoller

@Composable
fun FichaFantasiaScreen(
    onSalvar: () -> Unit,
    onInventario: () -> Unit,
    onDescricao: () -> Unit,
    onPericias: () -> Unit,
    viewModel: com.example.rpgapp.viewmodel.FichaFantasiaViewModel,
    onThemeChange: () -> Unit = {},
    onModeChange: () -> Unit = {}
) {
    val ficha by viewModel.ficha.collectAsState()

    var nome by remember { mutableStateOf("") }
    var forca by remember { mutableStateOf("0") }
    var destreza by remember { mutableStateOf("0") }
    var constituicao by remember { mutableStateOf("0") }
    var inteligencia by remember { mutableStateOf("0") }
    var sabedoria by remember { mutableStateOf("0") }
    var carisma by remember { mutableStateOf("0") }
    var nivel by remember { mutableStateOf("1") }
    var xp by remember { mutableStateOf("0") }
    var vidaAtual by remember { mutableStateOf("") }
    var vidaMax by remember { mutableStateOf("") }
    var manaAtual by remember { mutableStateOf("") }
    var manaMax by remember { mutableStateOf("") }
    var deslocamento by remember { mutableStateOf("9m") }
    var tamanho by remember { mutableStateOf("Médio") }
    var dinheiro by remember { mutableStateOf("0") }

    // Coleta os bônus calculados automaticamente dos itens
    val bonusArmadura by viewModel.bonusTotalArmadura.collectAsState()
    val bonusEscudo by viewModel.bonusTotalEscudo.collectAsState()
    val outrosBonusDefesa by viewModel.outrosBonusDefesaTotal.collectAsState()
    val bonusFortitude by viewModel.bonusTotalFortitude.collectAsState()
    val bonusReflexos by viewModel.bonusTotalReflexos.collectAsState()
    val bonusVontade by viewModel.bonusTotalVontade.collectAsState()

    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            forca = it.forca.toString()
            destreza = it.destreza.toString()
            constituicao = it.constituicao.toString()
            inteligencia = it.inteligencia.toString()
            sabedoria = it.sabedoria.toString()
            carisma = it.carisma.toString()
            nivel = it.nivel.toString()
            xp = it.xp.toString()
            vidaAtual = it.vidaAtual.toString()
            vidaMax = it.vidaMax.toString()
            manaAtual = it.manaAtual.toString()
            manaMax = it.manaMax.toString()
            deslocamento = it.deslocamento
            tamanho = it.tamanho
            dinheiro = it.dinheiro
        }
    }

    LaunchedEffect(nome, forca, destreza, constituicao, inteligencia, sabedoria, carisma, nivel, xp, vidaAtual, vidaMax, manaAtual, manaMax, deslocamento, tamanho, dinheiro) {
        kotlinx.coroutines.delay(1000)
        viewModel.salvarFichaCompleta(
            nome, forca, destreza, constituicao, inteligencia, sabedoria, carisma,
            nivel, xp, vidaAtual, vidaMax, manaAtual, manaMax, deslocamento, tamanho, dinheiro
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ficha", "Perícias", "Inventário", "Habilidades", "Magias", "Descrição")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> FichaFantasiaTab(
                    nome, forca, destreza, constituicao, inteligencia, sabedoria, carisma,
                    nivel, xp, vidaAtual, vidaMax, manaAtual, manaMax, deslocamento, tamanho, dinheiro,
                    bonusArmadura, bonusEscudo, outrosBonusDefesa, bonusFortitude, bonusReflexos, bonusVontade,
                    onNomeChange = { nome = it },
                    onForcaChange = { forca = it },
                    onDestrezaChange = { destreza = it },
                    onConstituicaoChange = { constituicao = it },
                    onInteligenciaChange = { inteligencia = it },
                    onSabedoriaChange = { sabedoria = it },
                    onCarismaChange = { carisma = it },
                    onNivelChange = { nivel = it },
                    onXpChange = { xp = it },
                    onVidaAtualChange = { vidaAtual = it },
                    onVidaMaxChange = { vidaMax = it },
                    onManaAtualChange = { manaAtual = it },
                    onManaMaxChange = { manaMax = it },
                    onDeslocamentoChange = { deslocamento = it },
                    onTamanhoChange = { tamanho = it },
                    onDinheiroChange = { dinheiro = it },
                    ficha = ficha,
                    viewModel = viewModel,
                    onThemeChange = onThemeChange,
                    onModeChange = onModeChange
                )
                1 -> PericiasFantasiaScreen(onBack = {}, viewModel = viewModel)
                2 -> InventarioFantasiaScreen(onBack = {}, viewModel = viewModel)
                3 -> HabilidadesFantasiaScreen(onBack = {}, viewModel = viewModel)
                4 -> MagiasFantasiaScreen(onBack = {}, viewModel = viewModel)
                5 -> DescricaoFantasiaScreen(onBack = {}, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FichaFantasiaTab(
    nome: String, forca: String, destreza: String, constituicao: String,
    inteligencia: String, sabedoria: String, carisma: String,
    nivel: String, xp: String, vidaAtual: String, vidaMax: String,
    manaAtual: String, manaMax: String, deslocamento: String, tamanho: String, dinheiro: String,
    bonusArmaduraCalculado: Int, bonusEscudoCalculado: Int, outrosBonusDefesaCalculado: Int, bonusFortitudeCalculado: Int, bonusReflexosCalculado: Int, bonusVontadeCalculado: Int,
    onNomeChange: (String) -> Unit,
    onForcaChange: (String) -> Unit,
    onDestrezaChange: (String) -> Unit,
    onConstituicaoChange: (String) -> Unit,
    onInteligenciaChange: (String) -> Unit,
    onSabedoriaChange: (String) -> Unit,
    onCarismaChange: (String) -> Unit,
    onNivelChange: (String) -> Unit,
    onXpChange: (String) -> Unit,
    onVidaAtualChange: (String) -> Unit,
    onVidaMaxChange: (String) -> Unit,
    onManaAtualChange: (String) -> Unit,
    onManaMaxChange: (String) -> Unit,
    onDeslocamentoChange: (String) -> Unit,
    onTamanhoChange: (String) -> Unit,
    onDinheiroChange: (String) -> Unit,
    ficha: com.example.rpgapp.data.entity.FichaFantasiaEntity?,
    viewModel: com.example.rpgapp.viewmodel.FichaFantasiaViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    val historicoRolagens by viewModel.historicoRolagens.collectAsState()
    var dadoCustom by remember { mutableStateOf("") }
    var showDiceAnimation by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(0) }
    var diceFaces by remember { mutableStateOf(20) }
    var pendingHistoryEntry by remember { mutableStateOf<String?>(null) }

    // Calcula defesas com os bônus dos itens (10 + nível/2 + mod + bônus de itens)
    val nivelCalculado = ficha?.nivel ?: 1
    val metadeNivel = nivelCalculado / 2
    
    val defesaTotal = remember(ficha, bonusArmaduraCalculado, bonusEscudoCalculado, outrosBonusDefesaCalculado) {
        10 + metadeNivel + (ficha?.modDestreza() ?: 0) + bonusArmaduraCalculado + bonusEscudoCalculado + outrosBonusDefesaCalculado
    }
    val fortitudeTotal = remember(ficha, bonusFortitudeCalculado) {
        10 + metadeNivel + (ficha?.modConstituicao() ?: 0) + bonusFortitudeCalculado
    }
    val reflexosTotal = remember(ficha, bonusReflexosCalculado) {
        10 + metadeNivel + (ficha?.modDestreza() ?: 0) + bonusReflexosCalculado
    }
    val vontadeTotal = remember(ficha, bonusVontadeCalculado) {
        10 + metadeNivel + (ficha?.modSabedoria() ?: 0) + bonusVontadeCalculado
    }

    val defesaBonusIcon = if (bonusArmaduraCalculado + bonusEscudoCalculado + outrosBonusDefesaCalculado > 0) "🛡️ +${bonusArmaduraCalculado + bonusEscudoCalculado + outrosBonusDefesaCalculado}" else ""
    val fortitudeBonusIcon = if (bonusFortitudeCalculado > 0) "💪 +$bonusFortitudeCalculado" else ""
    val reflexosBonusIcon = if (bonusReflexosCalculado > 0) "⚡ +$bonusReflexosCalculado" else ""
    val vontadeBonusIcon = if (bonusVontadeCalculado > 0) "🧠 +$bonusVontadeCalculado" else ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("▸ PERSONAGEM", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = nome,
                    onValueChange = onNomeChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Nome do personagem") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "💰",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    OutlinedTextField(
                        value = dinheiro,
                        onValueChange = onDinheiroChange,
                        label = { Text("Tibares (T$)", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("0") }
                    )
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("▸ ATRIBUTOS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AtributoFantasiaField("FOR", forca, onForcaChange, ficha?.modForca() ?: 0, Modifier.weight(1f))
                    AtributoFantasiaField("DES", destreza, onDestrezaChange, ficha?.modDestreza() ?: 0, Modifier.weight(1f))
                    AtributoFantasiaField("CON", constituicao, onConstituicaoChange, ficha?.modConstituicao() ?: 0, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AtributoFantasiaField("INT", inteligencia, onInteligenciaChange, ficha?.modInteligencia() ?: 0, Modifier.weight(1f))
                    AtributoFantasiaField("SAB", sabedoria, onSabedoriaChange, ficha?.modSabedoria() ?: 0, Modifier.weight(1f))
                    AtributoFantasiaField("CAR", carisma, onCarismaChange, ficha?.modCarisma() ?: 0, Modifier.weight(1f))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("▸ DEFESAS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DefesaFantasiaDisplay("Defesa", defesaTotal, "10", metadeNivel, ficha?.modDestreza() ?: 0, bonusArmaduraCalculado, bonusEscudoCalculado, outrosBonusDefesaCalculado, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DefesaFantasiaDisplay("Fortitude", fortitudeTotal, "10", metadeNivel, ficha?.modConstituicao() ?: 0, 0, 0, bonusFortitudeCalculado, Modifier.weight(1f))
                    DefesaFantasiaDisplay("Reflexos", reflexosTotal, "10", metadeNivel, ficha?.modDestreza() ?: 0, 0, 0, bonusReflexosCalculado, Modifier.weight(1f))
                    DefesaFantasiaDisplay("Vontade", vontadeTotal, "10", metadeNivel, ficha?.modSabedoria() ?: 0, 0, 0, bonusVontadeCalculado, Modifier.weight(1f))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("▸ RECURSOS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RecursoFantasiaField("PV Atual", vidaAtual, onVidaAtualChange, Modifier.weight(1f))
                    RecursoFantasiaField("PV Máx", vidaMax, onVidaMaxChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RecursoFantasiaField("PM Atual", manaAtual, onManaAtualChange, Modifier.weight(1f))
                    RecursoFantasiaField("PM Máx", manaMax, onManaMaxChange, Modifier.weight(1f))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("▸ INFORMAÇÕES", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RecursoFantasiaField("Nível", nivel, onNivelChange, Modifier.weight(1f))
                    RecursoFantasiaField("XP", xp, onXpChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RecursoFantasiaField("Desloc.", deslocamento, onDeslocamentoChange, Modifier.weight(1f))
                    RecursoFantasiaField("Tamanho", tamanho, onTamanhoChange, Modifier.weight(1f))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("▸ ROLAGEM DE DADOS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(4, 6, 8, 10, 12, 20).forEach { faces ->
                        DiceButton(faces = faces) {
                            diceFaces = faces
                            diceResult = (1..faces).random()
                            showDiceAnimation = true
                            pendingHistoryEntry = "d$faces = $diceResult"
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = dadoCustom, onValueChange = { dadoCustom = it }, label = { Text("Ex: 2d6+3", fontSize = 12.sp) }, modifier = Modifier.weight(1f), singleLine = true)
                    Button(onClick = {
                        val (resultado, texto) = DiceRoller.rolarCustom(dadoCustom)
                        if (resultado > 0) {
                            diceResult = resultado
                            diceFaces = 20
                            showDiceAnimation = true
                            pendingHistoryEntry = texto
                        }
                    }) { Text("Rolar") }
                }
                if (historicoRolagens.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Últimas Rolagens:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    historicoRolagens.forEach { rolagem ->
                        Text("▹ $rolagem", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        OutlinedButton(onClick = onThemeChange, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)) {
            Text("🎨 TROCAR TEMA", fontWeight = FontWeight.Bold)
        }
        
        OutlinedButton(
            onClick = onModeChange, 
            modifier = Modifier.fillMaxWidth(), 
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("🐉 TROCAR MODO DE JOGO", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(64.dp))
    }

    if (showDiceAnimation) {
        DiceRollFantasiaAnimation(
            result = diceResult, 
            faces = diceFaces, 
            onDismiss = { showDiceAnimation = false },
            onAnimationFinished = {
                pendingHistoryEntry?.let { entry ->
                    viewModel.adicionarRolagem(entry)
                    pendingHistoryEntry = null
                }
            }
        )
    }
}

@Composable
fun AtributoFantasiaField(label: String, value: String, onValueChange: (String) -> Unit, modificador: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(value = value, onValueChange = { if (it.length <= 3) onValueChange(it) }, modifier = Modifier.fillMaxWidth(), singleLine = true, textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center))
    }
}

@Composable
fun DefesaFantasiaDisplay(label: String, total: Int, base: String, metadeNivel: Int, modAtributo: Int, armadura: Int, escudo: Int, outros: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Grande bloco do total
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = total.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // Breakdown da fórmula (apenas para Defesa)
                if (label == "Defesa") {
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FormulaItem(base, "Base")
                            Text(
                                "+",
                                modifier = Modifier.padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FormulaItem(metadeNivel.toString(), "Nív/2")
                            Text(
                                "+",
                                modifier = Modifier.padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FormulaItem(modAtributo.toString(), "Mod")
                            Text(
                                "+",
                                modifier = Modifier.padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FormulaItem(armadura.toString(), "Arm")
                            Text(
                                "+",
                                modifier = Modifier.padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FormulaItem(escudo.toString(), "Esc")
                            Text(
                                "+",
                                modifier = Modifier.padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FormulaItem(outros.toString(), "Out")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormulaItem(valor: String, legenda: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(text = legenda, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun RecursoFantasiaField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(value = value, onValueChange = { if (it.length <= 10) onValueChange(it) }, label = { Text(label, fontSize = 12.sp) }, modifier = modifier, singleLine = true, textStyle = LocalTextStyle.current.copy(fontSize = 16.sp))
}

@Composable
fun DiceRollFantasiaAnimation(
    result: Int, 
    faces: Int, 
    onDismiss: () -> Unit,
    onAnimationFinished: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1500)
        isAnimating = false
        onAnimationFinished() // Aqui a surpresa é revelada no histórico
        delay(800)
        onDismiss()
    }
    Dialog(onDismissRequest = {}) {
        Card(modifier = Modifier.size(200.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isAnimating) {
                    AnimatedFantasiaDice(faces = faces)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(text = result.toString(), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(text = "d$faces", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun DiceButton(faces: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            DiceShape(faces = faces, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        }
        Text(
            text = "d$faces",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DiceShape(faces: Int, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        
        when (faces) {
            4 -> { // Triângulo
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w / 2, 0f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path, color)
            }
            6 -> { // Quadrado
                drawRect(color, size = size)
            }
            8 -> { // Losango (Diamante)
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w / 2, 0f)
                    lineTo(w, h / 2)
                    lineTo(w / 2, h)
                    lineTo(0f, h / 2)
                    close()
                }
                drawPath(path, color)
            }
            10 -> { // Pipa/Kite
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w / 2, 0f)
                    lineTo(w, h * 0.4f)
                    lineTo(w / 2, h)
                    lineTo(0f, h * 0.4f)
                    close()
                }
                drawPath(path, color)
            }
            12 -> { // Hexágono (simplificado para d12)
                val path = androidx.compose.ui.graphics.Path().apply {
                    val angle = 360f / 6
                    for (i in 0 until 6) {
                        val rad = Math.toRadians((i * angle - 90).toDouble())
                        val x = w / 2 + (w / 2) * Math.cos(rad).toFloat()
                        val y = h / 2 + (h / 2) * Math.sin(rad).toFloat()
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                drawPath(path, color)
            }
            20 -> { // Hexágono com linhas internas (simplificado)
                val path = androidx.compose.ui.graphics.Path().apply {
                    val angle = 360f / 6
                    for (i in 0 until 6) {
                        val rad = Math.toRadians((i * angle - 90).toDouble())
                        val x = w / 2 + (w / 2) * Math.cos(rad).toFloat()
                        val y = h / 2 + (h / 2) * Math.sin(rad).toFloat()
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                drawPath(path, color)
                // Linhas internas para dar profundidade de d20
                drawLine(color, start = androidx.compose.ui.geometry.Offset(w/2, 0f), end = androidx.compose.ui.geometry.Offset(w/2, h), strokeWidth = 1f)
            }
        }
    }
}

@Composable
fun AnimatedFantasiaDice(faces: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dice")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(150.dp)) {
        Box(modifier = Modifier.rotate(rotation)) {
            DiceShape(faces = faces, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(100.dp))
        }
    }
}
