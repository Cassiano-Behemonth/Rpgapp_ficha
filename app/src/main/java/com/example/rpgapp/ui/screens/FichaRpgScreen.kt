package com.example.rpgapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.math.cos

@Composable
fun FichaRpgScreen(
    onSalvar: () -> Unit,
    onInventario: () -> Unit,
    onDescricao: () -> Unit,
    onPericias: () -> Unit,
    viewModel: com.example.rpgapp.viewmodel.FichaViewModel
) {
    val ficha by viewModel.ficha.collectAsState()

    // Estados dos atributos
    var forca by remember { mutableStateOf("") }
    var agilidade by remember { mutableStateOf("") }
    var presenca by remember { mutableStateOf("") }
    var nex by remember { mutableStateOf("5") }
    var vidaAtual by remember { mutableStateOf("") }
    var vidaMax by remember { mutableStateOf("") }
    var sanidadeAtual by remember { mutableStateOf("") }
    var sanidadeMax by remember { mutableStateOf("") }

    // Carrega dados da ficha quando disponível
    LaunchedEffect(ficha) {
        ficha?.let {
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

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ficha", "Perícias", "Inventário", "Descrição")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // TabRow com scroll para nomes longos
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
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

        when (selectedTab) {
            0 -> FichaTab(
                forca = forca,
                agilidade = agilidade,
                presenca = presenca,
                nex = nex,
                vidaAtual = vidaAtual,
                vidaMax = vidaMax,
                sanidadeAtual = sanidadeAtual,
                sanidadeMax = sanidadeMax,
                onForcaChange = { forca = it },
                onAgilidadeChange = { agilidade = it },
                onPresencaChange = { presenca = it },
                onNexChange = { nex = it },
                onVidaAtualChange = { vidaAtual = it },
                onVidaMaxChange = { vidaMax = it },
                onSanidadeAtualChange = { sanidadeAtual = it },
                onSanidadeMaxChange = { sanidadeMax = it },
                onSalvar = {
                    viewModel.salvarFicha(
                        forca, agilidade, presenca, nex,
                        vidaAtual, vidaMax, sanidadeAtual, sanidadeMax
                    )
                }
            )
            1 -> PericiasScreen(onBack = {}, viewModel = viewModel)
            2 -> InventarioScreen(onBack = {}, viewModel = viewModel)
            3 -> DescricaoScreen(onBack = {}, viewModel = viewModel)
        }
    }
}

@Composable
fun FichaTab(
    forca: String,
    agilidade: String,
    presenca: String,
    nex: String,
    vidaAtual: String,
    vidaMax: String,
    sanidadeAtual: String,
    sanidadeMax: String,
    onForcaChange: (String) -> Unit,
    onAgilidadeChange: (String) -> Unit,
    onPresencaChange: (String) -> Unit,
    onNexChange: (String) -> Unit,
    onVidaAtualChange: (String) -> Unit,
    onVidaMaxChange: (String) -> Unit,
    onSanidadeAtualChange: (String) -> Unit,
    onSanidadeMaxChange: (String) -> Unit,
    onSalvar: () -> Unit
) {
    var historicoRolagens by remember { mutableStateOf<List<String>>(emptyList()) }
    var dadoCustom by remember { mutableStateOf("") }
    var showDiceAnimation by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(0) }
    var diceFaces by remember { mutableStateOf(20) }
    var showSaveConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Seção de Atributos
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

        // Seção de Recursos
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

        // Seção de Dados
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

                // Dados rápidos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(4, 6, 8, 10, 12, 20).forEach { faces ->
                        Button(
                            onClick = {
                                diceFaces = faces
                                diceResult = (1..faces).random()
                                showDiceAnimation = true
                                historicoRolagens = listOf("d$faces = $diceResult") + historicoRolagens.take(4)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text("d$faces", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rolagem personalizada
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
                            val resultado = rolarCustom(dadoCustom)
                            if (resultado > 0) {
                                diceResult = resultado
                                diceFaces = 20
                                showDiceAnimation = true
                                historicoRolagens = listOf("$dadoCustom = $resultado") + historicoRolagens.take(4)
                            }
                        }
                    ) {
                        Text("Rolar")
                    }
                }

                // Histórico
                if (historicoRolagens.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Últimas Rolagens:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    historicoRolagens.forEach { rolagem ->
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

        Button(
            onClick = {
                onSalvar()
                showSaveConfirmation = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("💾 SALVAR FICHA", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }

    // Confirmação de salvamento
    if (showSaveConfirmation) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSaveConfirmation = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("✓ Ficha salva com sucesso!", color = Color.Black)
            }
        }
    }

    // Animação do dado
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

    Canvas(modifier = Modifier.size(100.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 3

        rotate(rotation) {
            drawCircle(
                color = Color(0xFF00E676),
                radius = radius,
                center = center
            )

            val dotRadius = radius / 8
            when (faces) {
                20 -> {
                    for (i in 0..5) {
                        val angle = (i * 60f + rotation) * (Math.PI / 180f)
                        drawCircle(
                            color = Color.Black,
                            radius = dotRadius,
                            center = androidx.compose.ui.geometry.Offset(
                                centerX + (radius * 0.6f * cos(angle)).toFloat(),
                                centerY + (radius * 0.6f * sin(angle)).toFloat()
                            )
                        )
                    }
                }
                else -> {
                    drawCircle(
                        color = Color.Black,
                        radius = dotRadius,
                        center = center
                    )
                }
            }
        }
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

fun rolarCustom(expr: String): Int {
    val regex = Regex("(\\d+)d(\\d+)([+-]\\d+)?")
    val match = regex.find(expr.lowercase()) ?: return 0

    val qtd = match.groupValues[1].toIntOrNull() ?: return 0
    val faces = match.groupValues[2].toIntOrNull() ?: return 0
    val modStr = match.groupValues[3].ifEmpty { "+0" }
    val mod = modStr.toIntOrNull() ?: 0

    var total = 0
    repeat(qtd) { total += (1..faces).random() }

    return total + mod
}