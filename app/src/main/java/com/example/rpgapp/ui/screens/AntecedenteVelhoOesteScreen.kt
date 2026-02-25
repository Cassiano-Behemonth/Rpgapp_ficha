package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.AntecedenteVelhoOesteEntity
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel
import com.example.rpgapp.ui.theme.AppTextFieldDefaults

@Composable
fun AntecedentesVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel
) {
    val antecedentes by viewModel.antecedentes.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val antecedentePadrao = listOf(
        "Atenção",
        "Medicina",
        "Montaria",
        "Negócios",
        "Roubo",
        "Suor",
        "Tradição",
        "Violência"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            "▸ ANTECEDENTES",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            "${antecedentes.size} antecedente(s)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (antecedentes.isEmpty()) {
                Button(
                    onClick = {
                        antecedentePadrao.forEach { nome ->
                            viewModel.adicionarAntecedente(nome, 0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Adicionar Padrão")
                }
            }

            OutlinedButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Novo Antecedente")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (antecedentes.isEmpty()) {
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
                    Text("📖", style = MaterialTheme.typography.displayMedium)
                    Text(
                        "Nenhum antecedente cadastrado",
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
                antecedentes.forEach { antecedente ->
                    AntecedenteCard(
                        antecedente = antecedente,
                        onPontosChange = { pontos ->
                            viewModel.atualizarAntecedente(antecedente.copy(pontos = pontos))
                        },
                        onDelete = { viewModel.deletarAntecedente(antecedente) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AntecedenteDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nome ->
                viewModel.adicionarAntecedente(nome, 0)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AntecedenteCard(
    antecedente: AntecedenteVelhoOesteEntity,
    onPontosChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                    antecedente.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "1d6 +",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (antecedente.pontos > 0) {
                            onPontosChange(antecedente.pontos - 1)
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    antecedente.pontos.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(32.dp)
                )

                IconButton(
                    onClick = { onPontosChange(antecedente.pontos + 1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        null,
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AntecedenteDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var nome by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Antecedente", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.colors()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConfirm(nome) },
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