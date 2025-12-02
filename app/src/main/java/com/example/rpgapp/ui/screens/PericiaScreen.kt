package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpgapp.viewmodel.FichaViewModel
import com.example.rpgapp.data.entity.PericiaEntity

@Composable
fun PericiasScreen(
    onBack: () -> Unit,
    viewModel: FichaViewModel = viewModel()
) {
    val pericias by viewModel.pericias.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Lista de perícias padrão do sistema
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
            .padding(16.dp)
    ) {
        // Cabeçalho
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

        Spacer(modifier = Modifier.height(16.dp))

        if (pericias.isEmpty()) {
            // Estado vazio
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
                    Text(
                        "🎲",
                        style = MaterialTheme.typography.displayMedium
                    )
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
            // Lista de perícias
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                pericias.forEach { pericia ->
                    PericiaCard(
                        pericia = pericia,
                        onTreinoChange = {
                            viewModel.atualizarPericia(pericia.copy(treino = it))
                        },
                        onBonusChange = {
                            viewModel.atualizarPericia(pericia.copy(bonus = it.toIntOrNull() ?: 0))
                        },
                        onDelete = { viewModel.deletarPericia(pericia) }
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
    onTreinoChange: (Boolean) -> Unit,
    onBonusChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (pericia.treino)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nome e atributo
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    pericia.nome,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (pericia.treino)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Text(
                    pericia.atributo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Controles
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bônus
                OutlinedTextField(
                    value = if (pericia.bonus == 0) "" else pericia.bonus.toString(),
                    onValueChange = { if (it.length <= 2) onBonusChange(it) },
                    modifier = Modifier.width(60.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    placeholder = { Text("±", fontSize = 12.sp) }
                )

                // Treino
                Checkbox(
                    checked = pericia.treino,
                    onCheckedChange = onTreinoChange,
                    modifier = Modifier.size(36.dp)
                )

                // Deletar
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
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