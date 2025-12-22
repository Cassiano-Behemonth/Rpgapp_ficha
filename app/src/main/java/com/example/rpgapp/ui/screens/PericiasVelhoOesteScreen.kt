package com.example.rpgapp.ui.screens.velhooeste

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.PericiaVelhoOesteEntity
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel

@Composable
fun PericiasVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel
) {
    val pericias by viewModel.pericias.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val periciasPadrao = listOf(
        "Cavalgar" to "REF",
        "Rastreamento" to "ESP",
        "Jogo" to "ESP",
        "Intimidação" to "VIG",
        "Persuasão" to "CAR",
        "Tiro Rápido" to "PON",
        "Arremesso" to "PON",
        "Primeiros Socorros" to "ESP",
        "Sobrevivência" to "VIG",
        "Percepção" to "ESP",
        "Furtividade" to "REF",
        "Briga" to "VIG",
        "Lábia" to "CAR",
        "Explosivos" to "ESP"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "▸ PERÍCIAS DO OESTE",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${pericias.size} perícia(s)",
                    style = MaterialTheme.typography.bodySmall
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
                    Icon(Icons.Default.Add, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Nova", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (pericias.isEmpty()) {
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎯", style = MaterialTheme.typography.displayMedium)
                    Text(
                        "Nenhuma perícia cadastrada",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                pericias.forEach { pericia ->
                    PericiaVelhoOesteCard(
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
        PericiaVelhoOesteDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nome, atributo ->
                viewModel.adicionarPericia(nome, atributo)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PericiaVelhoOesteCard(
    pericia: PericiaVelhoOesteEntity,
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
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    pericia.nome,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    pericia.atributo,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = if (pericia.bonus == 0) "" else pericia.bonus.toString(),
                    onValueChange = { if (it.length <= 2) onBonusChange(it) },
                    modifier = Modifier.width(60.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    placeholder = { Text("±", fontSize = 12.sp) }
                )
                Checkbox(
                    checked = pericia.treino,
                    onCheckedChange = onTreinoChange,
                    modifier = Modifier.size(36.dp)
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Delete, null, Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun PericiaVelhoOesteDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var atributo by remember { mutableStateOf("PON") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Perícia", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Dropdown simples para atributos
                Text("Atributo: $atributo", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConfirm(nome, atributo) },
                enabled = nome.isNotBlank()
            ) {
                Text("Salvar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}