package com.example.rpgapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.math.cos
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FichaRpgScreen(
    onSalvar: () -> Unit,
    onInventario: () -> Unit,
    onDescricao: () -> Unit,
    onPericias: () -> Unit,
    viewModel: com.example.rpgapp.viewmodel.FichaViewModel,
    onThemeChange: () -> Unit = {},
    onModeChange: () -> Unit = {}
) {
    val ficha by viewModel.ficha.collectAsState()
    val historicoRolagens by viewModel.historicoRolagens.collectAsState()

    var nome by remember { mutableStateOf("") }
    var forca by remember { mutableStateOf("") }
    var agilidade by remember { mutableStateOf("") }
    var presenca by remember { mutableStateOf("") }
    var nex by remember { mutableStateOf("5") }
    var vidaAtual by remember { mutableStateOf("") }
    var vidaMax by remember { mutableStateOf("") }
    var sanidadeAtual by remember { mutableStateOf("") }
    var sanidadeMax by remember { mutableStateOf("") }

    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            forca = it.forca.toString()
            agilidade = it.agilidade.toString()
            presenca = it.presenca.toString()
            nex = it.nex.toString()
            vidaAtual = it.vidaAtual.toString()
            vidaMax = it.vidaMax.toString()
            sanidadeAtual = it.sanidadeAtual.toString()
            sanidadeMax = it.sanidadeMax.toString()
        }
    }

    // Salvamento automático quando qualquer campo muda
    LaunchedEffect(nome, forca, agilidade, presenca, nex, vidaAtual, vidaMax, sanidadeAtual, sanidadeMax) {
        kotlinx.coroutines.delay(1000) // Espera 1 segundo após a última digitação
        viewModel.salvarFichaCompleta(
            nome, forca, agilidade, presenca, nex,
            vidaAtual, vidaMax, sanidadeAtual, sanidadeMax
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ficha", "Perícias", "Inventário", "Descrição")
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    // Sincronizar tab selecionada com pager
    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
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
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FichaTab(
                    nome = nome,
                    forca = forca,
                    agilidade = agilidade,
                    presenca = presenca,
                    nex = nex,
                    vidaAtual = vidaAtual,
                    vidaMax = vidaMax,
                    sanidadeAtual = sanidadeAtual,
                    sanidadeMax = sanidadeMax,

                    onNomeChange = { nome = it },
                    onForcaChange = { forca = it },
                    onAgilidadeChange = { agilidade = it },
                    onPresencaChange = { presenca = it },
                    onNexChange = { nex = it },
                    onVidaAtualChange = { vidaAtual = it },
                    onVidaMaxChange = { vidaMax = it },
                    onSanidadeAtualChange = { sanidadeAtual = it },
                    onSanidadeMaxChange = { sanidadeMax = it },

                    historicoRolagens = historicoRolagens,
                    onRolar = { texto -> viewModel.adicionarRolagem(texto) },
                    onThemeChange = onThemeChange,
                    onModeChange = onModeChange
                )
                1 -> PericiasScreen(onBack = {}, viewModel = viewModel)
                2 -> InventarioScreen(onBack = {}, viewModel = viewModel)
                3 -> DescricaoScreen(onBack = {}, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FichaTab(
    nome: String,
    forca: String,
    agilidade: String,
    presenca: String,
    nex: String,
    vidaAtual: String,
    vidaMax: String,
    sanidadeAtual: String,
    sanidadeMax: String,
    onNomeChange: (String) -> Unit,
    onForcaChange: (String) -> Unit,
    onAgilidadeChange: (String) -> Unit,
    onPresencaChange: (String) -> Unit,
    onNexChange: (String) -> Unit,
    onVidaAtualChange: (String) -> Unit,
    onVidaMaxChange: (String) -> Unit,
    onSanidadeAtualChange: (String) -> Unit,
    onSanidadeMaxChange: (String) -> Unit,
    historicoRolagens: List<String>,
    onRolar: (String) -> Unit,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    var dadoCustom by remember { mutableStateOf("") }
    var showDiceAnimation by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(0) }
    var diceFaces by remember { mutableStateOf(20) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nome do Personagem
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ NOME DO PERSONAGEM",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nome,
                    onValueChange = onNomeChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Digite o nome do personagem") }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ ATRIBUTOS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AtributoCompacto("FORÇA", forca, onForcaChange, Modifier.weight(1f))
                    AtributoCompacto("AGILIDADE", agilidade, onAgilidadeChange, Modifier.weight(1f))
                    AtributoCompacto("PRESENÇA", presenca, onPresencaChange, Modifier.weight(1f))
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ RECURSOS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecursoField("PV Atual", vidaAtual, onVidaAtualChange, Modifier.weight(1f))
                    RecursoField("PV Máx", vidaMax, onVidaMaxChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecursoField("SAN Atual", sanidadeAtual, onSanidadeAtualChange, Modifier.weight(1f))
                    RecursoField("SAN Máx", sanidadeMax, onSanidadeMaxChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                RecursoField("NEX (%)", nex, onNexChange, Modifier.fillMaxWidth())
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ ROLAGEM DE DADOS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(4, 6, 8, 10, 12, 20).forEach { faces ->
                        HorrorDiceButton(faces = faces) {
                            diceFaces = faces
                            diceResult = (1..faces).random()
                            showDiceAnimation = true
                            onRolar("d$faces = $diceResult")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = dadoCustom,
                        onValueChange = { dadoCustom = it },
                        label = { Text("Ex: 2d6+3", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            val (resultado, texto) = rolarCustom(dadoCustom)
                            if (resultado > 0) {
                                diceResult = resultado
                                diceFaces = 20
                                showDiceAnimation = true
                                onRolar(texto)
                            }
                        }
                    ) {
                        Text("Rolar")
                    }
                }

                if (historicoRolagens.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Últimas Rolagens:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    historicoRolagens.take(4).forEach { rolagem ->
                        Text(
                            "▹ $rolagem",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onThemeChange,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("🎨 TROCAR TEMA", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onModeChange,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("🔄 TROCAR MODO DE JOGO", fontWeight = FontWeight.Bold)
        }
    }

    if (showDiceAnimation) {
        DiceRollAnimation(
            result = diceResult,
            faces = diceFaces,
            onDismiss = { showDiceAnimation = false }
        )
    }
}

@Composable
fun DiceRollAnimation(
    result: Int,
    faces: Int,
    onDismiss: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1500)
        isAnimating = false
        delay(800)
        onDismiss()
    }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.size(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isAnimating) {
                    AnimatedDice(faces = faces)
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = result.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "d$faces",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedDice(faces: Int) {
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

    Box(
        modifier = Modifier
            .size(100.dp)
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        HorrorDiceShape(
            faces = faces,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
    }
}


@Composable
fun AtributoCompacto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        OutlinedTextField(
            value = value,
            onValueChange = { if (it.length <= 2) onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun RecursoField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 3) onValueChange(it) },
        label = { Text(label, fontSize = 12.sp) },
        modifier = modifier,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

fun rolarCustom(expr: String): Pair<Int, String> {
    val regex = Regex("(\\d+)d(\\d+)([+\\-*/]\\d+)?")
    val match = regex.find(expr.lowercase()) ?: return Pair(0, "")

    val qtd = match.groupValues[1].toIntOrNull() ?: return Pair(0, "")
    val faces = match.groupValues[2].toIntOrNull() ?: return Pair(0, "")
    val modStr = match.groupValues[3]

    // Rola todos os dados
    val resultados = mutableListOf<Int>()
    repeat(qtd) {
        resultados.add((1..faces).random())
    }

    // Pega o maior resultado
    val maiorResultado = resultados.maxOrNull() ?: 0

    // Aplica o modificador apenas no maior
    val resultadoFinal = if (modStr.isNotEmpty()) {
        val operador = modStr[0]
        val valor = modStr.substring(1).toIntOrNull() ?: 0

        when (operador) {
            '+' -> maiorResultado + valor
            '-' -> maiorResultado - valor
            '*' -> maiorResultado * valor
            '/' -> if (valor != 0) maiorResultado / valor else maiorResultado
            else -> maiorResultado
        }
    } else {
        maiorResultado
    }

    // Monta o texto simplificado
    val textoFinal = if (qtd > 1) {
        if (modStr.isNotEmpty()) {
            "dados: ${resultados.joinToString(", ")} $modStr = $resultadoFinal"
        } else {
            "dados: ${resultados.joinToString(", ")} = $maiorResultado"
        }
    } else {
        if (modStr.isNotEmpty()) {
            "dado: $maiorResultado $modStr = $resultadoFinal"
        } else {
            "dado: $maiorResultado = $maiorResultado"
        }
    }

    return Pair(resultadoFinal, textoFinal)
}

@Composable
fun HorrorDiceButton(faces: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            HorrorDiceShape(faces = faces, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
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
fun HorrorDiceShape(faces: Int, color: Color, modifier: Modifier = Modifier) {
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
            8 -> { // Losango
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w / 2, 0f)
                    lineTo(w, h / 2)
                    lineTo(w / 2, h)
                    lineTo(0f, h / 2)
                    close()
                }
                drawPath(path, color)
            }
            10 -> { // Pipa
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w / 2, 0f)
                    lineTo(w, h * 0.4f)
                    lineTo(w / 2, h)
                    lineTo(0f, h * 0.4f)
                    close()
                }
                drawPath(path, color)
            }
            12 -> { // Hexágono
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
            20 -> { // Hexágono com linha central (d20)
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
                drawLine(
                    color = color.copy(alpha = 0.4f),
                    start = androidx.compose.ui.geometry.Offset(w / 2, 0f),
                    end = androidx.compose.ui.geometry.Offset(w / 2, h),
                    strokeWidth = 1.5f
                )
            }
        }
    }
}