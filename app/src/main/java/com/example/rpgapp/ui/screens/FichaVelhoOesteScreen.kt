package com.example.rpgapp.ui.screens.velhooeste

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.rpgapp.ui.screens.*

@Composable
fun FichaVelhoOesteScreen(
    viewModel: com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    val ficha by viewModel.ficha.collectAsState()

    var nome by remember { mutableStateOf("") }
    var pontaria by remember { mutableStateOf("") }
    var vigor by remember { mutableStateOf("") }
    var esperteza by remember { mutableStateOf("") }
    var carisma by remember { mutableStateOf("") }
    var reflexos by remember { mutableStateOf("") }
    var vidaAtual by remember { mutableStateOf("") }
    var vidaMax by remember { mutableStateOf("") }
    var municao by remember { mutableStateOf("") }
    var dinheiro by remember { mutableStateOf("0") }

    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            pontaria = it.pontaria.toString()
            vigor = it.vigor.toString()
            esperteza = it.esperteza.toString()
            carisma = it.carisma.toString()
            reflexos = it.reflexos.toString()
            vidaAtual = it.vidaAtual.toString()
            vidaMax = it.vidaMax.toString()
            municao = it.municao.toString()
            dinheiro = it.dinheiro
        }
    }

    // Salvamento automático
    LaunchedEffect(nome, pontaria, vigor, esperteza, carisma, reflexos, vidaAtual, vidaMax, municao, dinheiro) {
        delay(1000)
        viewModel.salvarFichaCompleta(
            nome, pontaria, vigor, esperteza, carisma, reflexos,
            vidaAtual, vidaMax, municao, dinheiro
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ficha", "Perícias", "Equipamento", "Descrição")

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
            0 -> FichaVelhoOesteTab(
                nome = nome,
                pontaria = pontaria,
                vigor = vigor,
                esperteza = esperteza,
                carisma = carisma,
                reflexos = reflexos,
                vidaAtual = vidaAtual,
                vidaMax = vidaMax,
                municao = municao,
                dinheiro = dinheiro,
                onNomeChange = { nome = it },
                onPontariaChange = { pontaria = it },
                onVigorChange = { vigor = it },
                onEspertezaChange = { esperteza = it },
                onCarismaChange = { carisma = it },
                onReflexosChange = { reflexos = it },
                onVidaAtualChange = { vidaAtual = it },
                onVidaMaxChange = { vidaMax = it },
                onMunicaoChange = { municao = it },
                onDinheiroChange = { dinheiro = it },
                onThemeChange = onThemeChange,
                onModeChange = onModeChange
            )
            1 -> PericiasVelhoOesteScreen(viewModel = viewModel)
            2 -> EquipamentoVelhoOesteScreen(viewModel = viewModel)
            3 -> DescricaoVelhoOesteScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun FichaVelhoOesteTab(
    nome: String,
    pontaria: String,
    vigor: String,
    esperteza: String,
    carisma: String,
    reflexos: String,
    vidaAtual: String,
    vidaMax: String,
    municao: String,
    dinheiro: String,
    onNomeChange: (String) -> Unit,
    onPontariaChange: (String) -> Unit,
    onVigorChange: (String) -> Unit,
    onEspertezaChange: (String) -> Unit,
    onCarismaChange: (String) -> Unit,
    onReflexosChange: (String) -> Unit,
    onVidaAtualChange: (String) -> Unit,
    onVidaMaxChange: (String) -> Unit,
    onMunicaoChange: (String) -> Unit,
    onDinheiroChange: (String) -> Unit,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    var historicoRolagens by remember { mutableStateOf<List<String>>(emptyList()) }
    var showDiceAnimation by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nome
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ NOME DO PISTOLEIRO",
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
                    placeholder = { Text("Ex: John 'Relâmpago' Smith") }
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
                    AtributoCompactoOeste("PONTARIA", pontaria, onPontariaChange, Modifier.weight(1f))
                    AtributoCompactoOeste("VIGOR", vigor, onVigorChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AtributoCompactoOeste("ESPERTEZA", esperteza, onEspertezaChange, Modifier.weight(1f))
                    AtributoCompactoOeste("CARISMA", carisma, onCarismaChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                AtributoCompactoOeste("REFLEXOS", reflexos, onReflexosChange, Modifier.fillMaxWidth())
            }
        }

        // Recursos
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
                    RecursoField("Vida Atual", vidaAtual, onVidaAtualChange, Modifier.weight(1f))
                    RecursoField("Vida Máx", vidaMax, onVidaMaxChange, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecursoField("Munição", municao, onMunicaoChange, Modifier.weight(1f))
                    OutlinedTextField(
                        value = dinheiro,
                        onValueChange = onDinheiroChange,
                        label = { Text("Dinheiro ($)", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        // Rolagem de Dados
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ DADOS DO DESTINO",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(6, 8, 10, 12, 20).forEach { faces ->
                        Button(
                            onClick = {
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
        DiceRollAnimation(
            result = diceResult,
            faces = 20,
            onDismiss = { showDiceAnimation = false }
        )
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