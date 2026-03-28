package com.example.rpgapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpgapp.data.entity.FichaEntity
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

    // Lista de perícias padrão do sistema (Ordem Paranormal)
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
    ) {
        // === BANNER STICKY DE ÚLTIMA ROLAGEM ===
        val ultimaRolagem = historicoRolagens.firstOrNull()
        if (ultimaRolagem != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "🎲",
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        ultimaRolagem,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // === CABEÇALHO ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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

                FilledTonalButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Adicionar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Nova", fontSize = 12.sp)
                }
            }
        }

        // === LISTA DE PERÍCIAS ===
        if (pericias.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
        } else {
            // Pre-compute atributos to avoid reading from ficha inside each card
            val forcaVal = ficha?.forca ?: 0
            val agilidadeVal = ficha?.agilidade ?: 0
            val presencaVal = ficha?.presenca ?: 0

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
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

                    PericiaCard(
                        pericia = pericia,
                        atributoVal = atributoVal,
                        onTreinoChange = {
                            viewModel.atualizarPericia(pericia.copy(treino = it))
                        },
                        onVantagemChange = { novaVantagem ->
                            viewModel.atualizarPericia(
                                pericia.copy(
                                    vantagem = novaVantagem,
                                    desvantagem = if (novaVantagem) false else pericia.desvantagem
                                )
                            )
                        },
                        onDesvantagemChange = { novaDesvantagem ->
                            viewModel.atualizarPericia(
                                pericia.copy(
                                    desvantagem = novaDesvantagem,
                                    vantagem = if (novaDesvantagem) false else pericia.vantagem
                                )
                            )
                        },
                        onBonusChange = {
                            viewModel.atualizarPericia(pericia.copy(bonus = it.toIntOrNull() ?: 0))
                        },
                        onDelete = { viewModel.deletarPericia(pericia) },
                        onRolar = { texto -> viewModel.adicionarRolagem(texto) }
                    )
                }
            }
        }
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
fun PericiaCard(
    pericia: PericiaEntity,
    atributoVal: Int,
    onTreinoChange: (Boolean) -> Unit,
    onVantagemChange: (Boolean) -> Unit,
    onDesvantagemChange: (Boolean) -> Unit,
    onBonusChange: (String) -> Unit,
    onDelete: () -> Unit,
    onRolar: (String) -> Unit
) {
    val cardColor by animateColorAsState(
        targetValue = when {
            pericia.treino && pericia.vantagem -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f)
            pericia.treino -> MaterialTheme.colorScheme.primaryContainer
            pericia.vantagem -> MaterialTheme.colorScheme.secondaryContainer
            pericia.desvantagem -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(300),
        label = "cardColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // === LINHA SUPERIOR: Nome + Atributo + Botão Rolar ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        pericia.nome,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            pericia.atributo,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        if (atributoVal != 0) {
                            Text(
                                "(${atributoVal}d20)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (pericia.bonus != 0) {
                            Text(
                                if (pericia.bonus > 0) "+${pericia.bonus}" else "${pericia.bonus}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (pericia.bonus > 0)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                // Botão Rolar
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
                            pericia.vantagem -> " 👍Vant"
                            pericia.desvantagem -> " 👎Desv"
                            else -> ""
                        }
                        val treinoLabel = if (pericia.treino) " +5(Trei)" else ""
                        val bonusLabel = if (pericia.bonus != 0) " ${if (pericia.bonus > 0) "+" else ""}${pericia.bonus}(Bôn)" else ""

                        val texto = "${pericia.nome}$statusLabel: [$detalhes]→$maiorBase$treinoLabel$bonusLabel = $total"
                        onRolar(texto)
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("🎲 Rolar", fontSize = 12.sp)
                }

                Spacer(Modifier.width(4.dp))

                // Botão deletar
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(Modifier.height(6.dp))

            // === LINHA INFERIOR: Treino | Vantagem | Desvantagem | Bônus ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Treino
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Checkbox(
                        checked = pericia.treino,
                        onCheckedChange = onTreinoChange,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "Trei.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Vantagem
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Checkbox(
                        checked = pericia.vantagem,
                        onCheckedChange = onVantagemChange,
                        modifier = Modifier.size(28.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.tertiary
                        )
                    )
                    Text(
                        "Vant.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Desvantagem
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Checkbox(
                        checked = pericia.desvantagem,
                        onCheckedChange = onDesvantagemChange,
                        modifier = Modifier.size(28.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.error
                        )
                    )
                    Text(
                        "Desv.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Bônus
                OutlinedTextField(
                    value = if (pericia.bonus == 0) "" else pericia.bonus.toString(),
                    onValueChange = { if (it.length <= 3) onBonusChange(it) },
                    modifier = Modifier.width(64.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                    placeholder = { Text("±Bôn", fontSize = 11.sp) },
                    label = { Text("Bônus", fontSize = 10.sp) }
                )
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

                    // Overlay transparente para capturar cliques na área inteira
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