package com.example.rpgapp.ui.screens.velhooeste

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.HabilidadeVelhoOesteEntity
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel

@Composable
fun HabilidadesVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel
) {
    val habilidades by viewModel.habilidades.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingHabilidade by remember { mutableStateOf<HabilidadeVelhoOesteEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header com título
        Text(
            "▸ HABILIDADES",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            "${habilidades.size} habilidade(s)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botão adicionar
        Button(
            onClick = {
                editingHabilidade = null
                showAddDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, null, Modifier.size(18.dp))
            Spacer(Modifier.width(4.dp))
            Text("Nova Habilidade")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (habilidades.isEmpty()) {
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
                    Text("⚔️", style = MaterialTheme.typography.displayMedium)
                    Text(
                        "Nenhuma habilidade cadastrada",
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                habilidades.forEach { habilidade ->
                    HabilidadeCard(
                        habilidade = habilidade,
                        onEdit = {
                            editingHabilidade = habilidade
                            showAddDialog = true
                        },
                        onDelete = { viewModel.deletarHabilidade(habilidade) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        HabilidadeDialog(
            habilidade = editingHabilidade,
            onDismiss = {
                showAddDialog = false
                editingHabilidade = null
            },
            onConfirm = { nome, descricao, danoOuDado ->
                if (editingHabilidade != null) {
                    viewModel.atualizarHabilidade(
                        editingHabilidade!!.copy(
                            nome = nome,
                            descricao = descricao,
                            danoOuDado = danoOuDado
                        )
                    )
                } else {
                    viewModel.adicionarHabilidade(nome, descricao, danoOuDado)
                }
                showAddDialog = false
                editingHabilidade = null
            }
        )
    }
}

@Composable
fun HabilidadeCard(
    habilidade: HabilidadeVelhoOesteEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        habilidade.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (habilidade.danoOuDado.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Dano/Dado: ${habilidade.danoOuDado}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            null,
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            null,
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (habilidade.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    habilidade.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun HabilidadeDialog(
    habilidade: HabilidadeVelhoOesteEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(habilidade?.nome ?: "") }
    var descricao by remember { mutableStateOf(habilidade?.descricao ?: "") }
    var danoOuDado by remember { mutableStateOf(habilidade?.danoOuDado ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (habilidade == null) "Nova Habilidade" else "Editar Habilidade",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = danoOuDado,
                    onValueChange = { danoOuDado = it },
                    label = { Text("Dano/Dado") },
                    placeholder = { Text("Ex: 1d6, +2, 2d8+3") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    placeholder = { Text("Descreva a habilidade...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConfirm(nome, descricao, danoOuDado) },
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