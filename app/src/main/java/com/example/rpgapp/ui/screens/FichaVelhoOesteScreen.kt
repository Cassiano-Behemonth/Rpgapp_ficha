package com.example.rpgapp.ui.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import com.example.rpgapp.ui.screens.AntecedentesVelhoOesteScreen
import com.example.rpgapp.ui.screens.DescricaoVelhoOesteScreen
import com.example.rpgapp.ui.screens.EquipamentoVelhoOesteScreen
import com.example.rpgapp.ui.screens.HabilidadesVelhoOesteScreen
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FichaVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    val ficha by viewModel.ficha.collectAsState()

    var nome by remember { mutableStateOf("") }
    var fisico by remember { mutableStateOf("") }
    var velocidade by remember { mutableStateOf("") }
    var intelecto by remember { mutableStateOf("") }
    var coragem by remember { mutableStateOf("") }
    var defesa by remember { mutableStateOf("") }
    var dinheiro by remember { mutableStateOf("") }
    var vidaAtual by remember { mutableStateOf(0) }
    var dorAtual by remember { mutableStateOf(0) }

    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            fisico = it.fisico.toString()
            velocidade = it.velocidade.toString()
            intelecto = it.intelecto.toString()
            coragem = it.coragem.toString()
            defesa = it.defesa.toString()
            dinheiro = it.dinheiro
            vidaAtual = it.vidaAtual
            dorAtual = it.dorAtual
        }
    }

    // Salvamento automático
    LaunchedEffect(nome, fisico, velocidade, intelecto, coragem, defesa, dinheiro, vidaAtual, dorAtual) {
        delay(1000)
        viewModel.salvarFichaCompleta(
            nome, fisico, velocidade, intelecto, coragem, defesa, dinheiro, vidaAtual, dorAtual
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ficha", "Antecedentes", "Habilidades", "Equipamento", "Descrição")
    val pagerState = rememberPagerState(pageCount = { tabs.size })

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
                0 -> FichaVelhoOesteTab(
                    nome = nome,
                    fisico = fisico,
                    velocidade = velocidade,
                    intelecto = intelecto,
                    coragem = coragem,
                    defesa = defesa,
                    dinheiro = dinheiro,
                    vidaAtual = vidaAtual,
                    vidaMaxima = ficha?.vidaMaxima ?: 6,
                    dorAtual = dorAtual,
                    selosMorteBonus = ficha?.selosMorteBonus ?: 0,
                    onNomeChange = { nome = it },
                    onFisicoChange = { fisico = it },
                    onVelocidadeChange = { velocidade = it },
                    onIntelectoChange = { intelecto = it },
                    onCoragemChange = { coragem = it },
                    onDefesaChange = { defesa = it },
                    onDinheiroChange = { dinheiro = it },
                    onVidaAtualChange = {
                        vidaAtual = it
                        viewModel.atualizarVida(it)
                    },
                    onDorAtualChange = {
                        dorAtual = it
                        viewModel.atualizarDor(it)
                    },
                    onSelosBonusChange = { viewModel.atualizarSelosBonus(it) },
                    viewModel = viewModel,
                    onThemeChange = onThemeChange,
                    onModeChange = onModeChange
                )
                1 -> AntecedentesVelhoOesteScreen(viewModel = viewModel)
                2 -> HabilidadesVelhoOesteScreen(viewModel = viewModel)
                3 -> EquipamentoVelhoOesteScreen(viewModel = viewModel)
                4 -> DescricaoVelhoOesteScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FichaVelhoOesteTab(
    nome: String,
    fisico: String,
    velocidade: String,
    intelecto: String,
    coragem: String,
    defesa: String,
    dinheiro: String,
    vidaAtual: Int,
    vidaMaxima: Int,
    dorAtual: Int,
    selosMorteBonus: Int,
    onNomeChange: (String) -> Unit,
    onFisicoChange: (String) -> Unit,
    onVelocidadeChange: (String) -> Unit,
    onIntelectoChange: (String) -> Unit,
    onCoragemChange: (String) -> Unit,
    onDefesaChange: (String) -> Unit,
    onDinheiroChange: (String) -> Unit,
    onVidaAtualChange: (Int) -> Unit,
    onDorAtualChange: (Int) -> Unit,
    onSelosBonusChange: (Int) -> Unit,
    viewModel: FichaVelhoOesteViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    var historicoRolagens by remember { mutableStateOf<List<String>>(emptyList()) }
    var showDiceAnimation by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(0) }

    // Dialog para adicionar selos de morte
    var showSelosDialog by remember { mutableStateOf(false) }

    // Context para tocar som
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nome e Dinheiro
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
                    placeholder = { Text("Digite o nome") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dinheiro
                OutlinedTextField(
                    value = dinheiro,
                    onValueChange = onDinheiroChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("💰 Dinheiro") },
                    placeholder = { Text("Ex: 100 moedas") }
                )
            }
        }

        // Atributos
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AtributoCompactoOeste("FÍSICO", fisico, onFisicoChange, Modifier.weight(1f))
                    AtributoCompactoOeste("VELOCIDADE", velocidade, onVelocidadeChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AtributoCompactoOeste("INTELECTO", intelecto, onIntelectoChange, Modifier.weight(1f))
                    AtributoCompactoOeste("CORAGEM", coragem, onCoragemChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AtributoCompactoOeste("DEFESA", defesa, onDefesaChange, Modifier.weight(1f))
                }
            }
        }

        // Sistema de Dor (6 pontos fixos)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "▸ DOR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "$dorAtual / 6",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                PainCircles(
                    currentPain = dorAtual,
                    onPainChange = onDorAtualChange
                )
            }
        }

        // Sistema de Selo da Morte com botão +
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "▸ SELO DA MORTE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "$vidaAtual / $vidaMaxima (6 + Físico $fisico${if (selosMorteBonus > 0) " +$selosMorteBonus" else ""})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Botão + para adicionar selos
                    IconButton(
                        onClick = { showSelosDialog = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Adicionar selos",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Círculos de Selo da Morte
                HealthCircles(
                    currentHealth = vidaAtual,
                    maxHealth = vidaMaxima,
                    onHealthChange = onVidaAtualChange
                )
            }
        }

        // Tambor de Revólver (1d6) com SOM
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ 🔫 TAMBOR DE REVÓLVER",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Botão com som de disparo
                Button(
                    onClick = {
                        diceResult = (1..6).random()
                        showDiceAnimation = true
                        historicoRolagens = listOf("1d6 = $diceResult") + historicoRolagens.take(4)

                        // Toca som de disparo
                        playGunSound(context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text("🎲 ROLAR 1d6", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

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

        // Botões de ação
        OutlinedButton(
            onClick = onThemeChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎨 TROCAR TEMA", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onModeChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🔄 TROCAR MODO DE JOGO", fontWeight = FontWeight.Bold)
        }
    }

    if (showDiceAnimation) {
        RevolverCylinderAnimation(
            result = diceResult,
            onDismiss = { showDiceAnimation = false }
        )
    }

    // Dialog para adicionar selos de morte
    if (showSelosDialog) {
        SelosMorteDialog(
            currentBonus = selosMorteBonus,
            onDismiss = { showSelosDialog = false },
            onConfirm = { novoBonus ->
                onSelosBonusChange(novoBonus)
                showSelosDialog = false
            }
        )
    }
}

/**
 * Função para tocar som de disparo
 *
 * IMPORTANTE: Você precisa adicionar o arquivo de som!
 * Arquivo: gun_shot.mp3
 * Local: res/raw/gun_shot.mp3
 */
private fun playGunSound(context: Context) {
    try {
        val mediaPlayer = MediaPlayer.create(context, context.resources.getIdentifier(
            "gun_shot",  // Nome do arquivo (sem extensão)
            "raw",       // Pasta res/raw/
            context.packageName
        ))

        mediaPlayer?.apply {
            setOnCompletionListener { mp ->
                mp.release()  // Libera memória
            }
            start()  // Toca o som!
        }
    } catch (e: Exception) {
        // Se não encontrar o som, não faz nada
        e.printStackTrace()
    }
}

/**
 * Dialog para adicionar bônus aos selos de morte
 */
@Composable
fun SelosMorteDialog(
    currentBonus: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var bonus by remember { mutableStateOf(currentBonus.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Bônus de Selos de Morte",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Adicione selos extras de habilidades, magias ou itens especiais.",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = bonus,
                    onValueChange = {
                        if (it.isEmpty() || it == "-") {
                            bonus = it
                        } else {
                            val num = it.toIntOrNull()
                            if (num != null && num >= 0 && num <= 20) {
                                bonus = it
                            }
                        }
                    },
                    label = { Text("Bônus (selos)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("0") }
                )

                Text(
                    "Total: 6 + Físico + $bonus = ${6 + (bonus.toIntOrNull() ?: 0)} selos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(bonus.toIntOrNull() ?: 0)
                }
            ) {
                Text(
                    "Salvar",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun RevolverCylinderAnimation(
    result: Int,
    onDismiss: () -> Unit
) {
    var isSpinning by remember { mutableStateOf(true) }
    var rotation by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (isSpinning) {
            rotation += 30f
            delay(50)
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        isSpinning = false
        delay(800)
        onDismiss()
    }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.size(250.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isSpinning) {
                    Canvas(
                        modifier = Modifier
                            .size(150.dp)
                            .graphicsLayer { rotationZ = rotation }
                    ) {
                        val radius = size.minDimension / 2
                        val center = Offset(size.width / 2, size.height / 2)

                        drawCircle(
                            color = Color.Black,
                            radius = radius,
                            center = center
                        )

                        for (i in 0 until 6) {
                            val angle = (i * 60f - 90f) * (Math.PI / 180f).toFloat()
                            val holeRadius = radius * 0.6f
                            val x = center.x + holeRadius * cos(angle)
                            val y = center.y + holeRadius * sin(angle)

                            drawCircle(
                                color = Color.White,
                                radius = radius * 0.15f,
                                center = Offset(x, y)
                            )
                        }

                        drawCircle(
                            color = Color.White.copy(alpha = 0.3f),
                            radius = radius * 0.2f,
                            center = center
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "💥",
                            fontSize = 60.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = result.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "1d6",
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
fun HealthCircles(
    currentHealth: Int,
    maxHealth: Int,
    onHealthChange: (Int) -> Unit
) {
    val rows = (maxHealth + 5) / 6

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val startIndex = rowIndex * 6
                val endIndex = minOf(startIndex + 6, maxHealth)

                for (i in startIndex until endIndex) {
                    val circleNumber = i + 1
                    val isFilled = circleNumber <= currentHealth

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFilled)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable {
                                onHealthChange(
                                    if (isFilled && circleNumber == currentHealth) {
                                        currentHealth - 1
                                    } else {
                                        circleNumber
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isFilled) {
                            Text(
                                text = "💀",
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                repeat(6 - (endIndex - startIndex)) {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}

@Composable
fun PainCircles(
    currentPain: Int,
    onPainChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..6) {
            val isFilled = i <= currentPain

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isFilled)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surface
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable {
                        onPainChange(
                            if (isFilled && i == currentPain) {
                                currentPain - 1
                            } else {
                                i
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isFilled) {
                    Text(
                        text = "👊",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AtributoCompactoOeste(
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