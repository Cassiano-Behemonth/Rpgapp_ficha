package com.example.rpgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpgapp.data.entity.PericiaEntity
import com.example.rpgapp.viewmodel.FichaViewModel

@Composable
fun PericiasScreen(
    onBack: () -> Unit,
    viewModel: FichaViewModel = viewModel()
) {
    val pericias by viewModel.pericias.collectAsState()
    val ficha by viewModel.ficha.collectAsState()
    val historicoRolagens by viewModel.historicoRolagens.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Estado para animação — resultado só entra no histórico após animação
    var showDiceAnimation by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(0) }
    var pendingHistoryEntry by remember { mutableStateOf<String?>(null) }

    val periciasPadrao = listOf(
        "Acrobacia" to "AGI",
        "Atletismo" to "FOR",
        "Ciências" to "PRE",
        "Crime" to "AGI",
        "Diplomacia" to "PRE",
        "Enganação" to "PRE",
        "Fortitude" to "FOR",
        "Furtividade" to "AGI",
        "Iniciativa" to "AGI",
        "Intimidação" to "PRE",
        "Intuição" to "PRE",
        "Investigação" to "PRE",
        "Luta" to "FOR",
        "Medicina" to "PRE",
        "Ocultismo" to "PRE",
        "Percepção" to "PRE",
        "Pilotagem" to "AGI",
        "Pontaria" to "AGI",
        "Profissão" to "PRE",
        "Reflexos" to "AGI",
        "Religião" to "PRE",
        "Sobrevivência" to "PRE",
        "Tática" to "PRE",
        "Tecnologia" to "PRE",
        "Vontade" to "PRE"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // === CABEÇALHO ===
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "▸ PERÍCIAS",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${pericias.size} perícia(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (pericias.isEmpty()) {
                    FilledTonalButton(
                        onClick = {
                            periciasPadrao.forEach { (nome, attr) ->
                                viewModel.adicionarPericia(nome, attr)
                            }
                        }
                    ) {
                        Text("Padrão", fontSize = 12.sp)
                    }
                }
                FilledTonalButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Nova", fontSize = 12.sp)
                }
            }
        }

        // === CARD DE ÚLTIMA ROLAGEM (igual ao Fantasia) ===
        if (historicoRolagens.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "📜 Última Rolagem",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    historicoRolagens.take(1).forEach { rolagem ->
                        Text(
                            "▹ $rolagem",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // === LISTA (LazyColumn) ===
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (pericias.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🎲", style = MaterialTheme.typography.displayMedium)
                            Text(
                                "Nenhuma perícia cadastrada",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Adicione perícias manualmente ou use o conjunto padrão",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                val forcaVal = ficha?.forca ?: 0
                val agilidadeVal = ficha?.agilidade ?: 0
                val presencaVal = ficha?.presenca ?: 0

                items(
                    items = pericias,
                    key = { it.id },
                    contentType = { "pericia" }
                ) { pericia ->
                    val atributoVal = when (pericia.atributo) {
                        "FOR" -> forcaVal
                        "AGI" -> agilidadeVal
                        "PRE" -> presencaVal
                        else -> 0
                    }

                    HorrorPericiaCard(
                        pericia = pericia,
                        atributoVal = atributoVal,
                        onTreinoChange = {
                            viewModel.atualizarPericia(pericia.copy(treino = it))
                        },
                        onVantagemChange = { nova ->
                            viewModel.atualizarPericia(
                                pericia.copy(
                                    vantagem = nova,
                                    desvantagem = if (nova) false else pericia.desvantagem
                                )
                            )
                        },
                        onDesvantagemChange = { nova ->
                            viewModel.atualizarPericia(
                                pericia.copy(
                                    desvantagem = nova,
                                    vantagem = if (nova) false else pericia.vantagem
                                )
                            )
                        },
                        onBonusChange = {
                            viewModel.atualizarPericia(pericia.copy(bonus = it.toIntOrNull() ?: 0))
                        },
                        onDelete = { viewModel.deletarPericia(pericia) },
                        onRolar = { resultado, totalResult ->
                            diceResult = totalResult
                            pendingHistoryEntry = resultado
                            showDiceAnimation = true
                        }
                    )
                }
            }
        }
    }

    // Animação de dado — resultado só vai ao histórico depois da animação
    if (showDiceAnimation) {
        DiceRollAnimation(
            result = diceResult,
            faces = 20,
            onDismiss = { showDiceAnimation = false },
            onAnimationFinished = {
                pendingHistoryEntry?.let { entry ->
                    viewModel.adicionarRolagem(entry)
                    pendingHistoryEntry = null
                }
            }
        )
    }

    if (showAddDialog) {
        PericiaDialog(
            title = "Adicionar Perícia",
            pericia = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { nome, atributo ->
                viewModel.adicionarPericia(nome, atributo)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun HorrorPericiaCard(
    pericia: PericiaEntity,
    atributoVal: Int,
    onTreinoChange: (Boolean) -> Unit,
    onVantagemChange: (Boolean) -> Unit,
    onDesvantagemChange: (Boolean) -> Unit,
    onBonusChange: (String) -> Unit,
    onDelete: () -> Unit,
    onRolar: (String, Int) -> Unit  // (texto para histórico, resultado numérico para animação)
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // === Linha 1: Nome + Atributo + Botão Rolar ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        pericia.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Exibe atributo e quantos dados serão rolados
                    val numDados = maxOf(atributoVal, 1)
                    val bonusLabel = when {
                        pericia.bonus > 0 -> " +${pericia.bonus}(Bôn)"
                        pericia.bonus < 0 -> " ${pericia.bonus}(Bôn)"
                        else -> ""
                    }
                    val treinoLabel = if (pericia.treino) " +5(Trei)" else ""
                    Text(
                        "${pericia.atributo} • ${numDados}d20$treinoLabel$bonusLabel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalButton(
                    onClick = {
                        val numDados = maxOf(atributoVal, 1)
                        val resultados = (1..numDados).map { (1..20).random() }
                        val maiorBase = when {
                            pericia.vantagem -> resultados.max()
                            pericia.desvantagem -> resultados.min()
                            else -> resultados.max()
                        }
                        val total = maiorBase + pericia.bonus + (if (pericia.treino) 5 else 0)

                        val detalhes = if (numDados > 1) resultados.joinToString(", ") else maiorBase.toString()
                        val statusLabel = when {
                            pericia.vantagem -> " (Vantagem)"
                            pericia.desvantagem -> " (Desvantagem)"
                            else -> ""
                        }
                        val bonusStr = if (pericia.bonus != 0) " + ${pericia.bonus}" else ""
                        val treinoStr = if (pericia.treino) " +5(T)" else ""
                        val textoHistorico = "${pericia.nome}$statusLabel: [$detalhes]→$maiorBase$treinoStr$bonusStr = $total"
                        onRolar(textoHistorico, total)
                    },
                    modifier = Modifier.height(40.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("🎲", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // === Linha 2: Bônus + Checkboxes (T/V/D) + Deletar ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bônus
                OutlinedTextField(
                    value = if (pericia.bonus == 0) "" else pericia.bonus.toString(),
                    onValueChange = { if (it.length <= 3) onBonusChange(it) },
                    label = { Text("Bônus", fontSize = 10.sp) },
                    modifier = Modifier.width(70.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Checkboxes T / V / D
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Treino
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onTreinoChange(!pericia.treino) }
                    ) {
                        Checkbox(
                            checked = pericia.treino,
                            onCheckedChange = onTreinoChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        Text("T", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Vantagem
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onVantagemChange(!pericia.vantagem) }
                    ) {
                        Checkbox(
                            checked = pericia.vantagem,
                            onCheckedChange = onVantagemChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier.size(32.dp)
                        )
                        Text("V", fontSize = 11.sp)
                    }

                    // Desvantagem
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onDesvantagemChange(!pericia.desvantagem) }
                    ) {
                        Checkbox(
                            checked = pericia.desvantagem,
                            onCheckedChange = onDesvantagemChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.size(32.dp)
                        )
                        Text("D", fontSize = 11.sp)
                    }
                }

                // Deletar
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PericiaDialog(
    title: String,
    pericia: PericiaEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var nome by remember { mutableStateOf(pericia?.nome ?: "") }
    var atributoSelecionado by remember { mutableStateOf(pericia?.atributo ?: "FOR") }
    var expandedAtributo by remember { mutableStateOf(false) }

    val atributos = listOf("FOR", "AGI", "PRE")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da Perícia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Box {
                    OutlinedTextField(
                        value = atributoSelecionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Atributo") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Selecionar",
                                modifier = Modifier.clickable { expandedAtributo = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedAtributo = true }
                    )

                    // Overlay transparente para capturar clique em qualquer área
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { expandedAtributo = true }
                    )

                    DropdownMenu(
                        expanded = expandedAtributo,
                        onDismissRequest = { expandedAtributo = false }
                    ) {
                        atributos.forEach { atributo ->
                            DropdownMenuItem(
                                text = { Text(atributo) },
                                onClick = {
                                    atributoSelecionado = atributo
                                    expandedAtributo = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nome.isNotBlank()) {
                        onConfirm(nome, atributoSelecionado)
                    }
                },
                enabled = nome.isNotBlank()
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