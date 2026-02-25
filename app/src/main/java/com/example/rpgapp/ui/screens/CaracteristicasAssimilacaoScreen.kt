package com.example.rpgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.CaracteristicaAssimilacaoEntity
import com.example.rpgapp.ui.theme.AppTextFieldDefaults
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel

// Cor por custo de pontos
fun corDoCusto(custo: Int): Color = when (custo) {
    1 -> Color(0xFF43A047) // Verde
    2 -> Color(0xFF1E88E5) // Azul
    3 -> Color(0xFFFFB300) // Âmbar
    4 -> Color(0xFFE53935) // Vermelho
    5 -> Color(0xFF6A1B9A) // Roxo
    else -> Color(0xFF757575)
}

@Composable
fun CaracteristicasAssimilacaoScreen(
    viewModel: FichaAssimilacaoViewModel
) {
    val caracteristicas by viewModel.caracteristicas.collectAsState()
    val totalPontos by viewModel.totalPontosCaracteristicas.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<CaracteristicaAssimilacaoEntity?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ── Header ───────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "▸ CARACTERÍSTICAS",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${caracteristicas.size} característica(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Adicionar", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Card de total de pontos ───────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total de pontos gastos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "$totalPontos pts",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Legenda de custos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 1..5) {
                        val cor = corDoCusto(i)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(cor)
                            )
                            Text(
                                "${i}pt",
                                fontSize = 10.sp,
                                color = cor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (caracteristicas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚡", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Nenhuma característica",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Adicione talentos e habilidades especiais",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    caracteristicas.forEach { caracteristica ->
                        CaracteristicaCard(
                            caracteristica = caracteristica,
                            onEdit = { itemToEdit = caracteristica },
                            onDelete = { viewModel.deletarCaracteristica(caracteristica) }
                        )
                    }
                }
            }
        }

        // ── Dialogs ──────────────────────────────────────────
        if (showAddDialog) {
            CaracteristicaDialog(
                caracteristica = null,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, custo, requisitos, descricao ->
                    viewModel.adicionarCaracteristica(nome, custo, requisitos, descricao)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { item ->
            CaracteristicaDialog(
                caracteristica = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { nome, custo, requisitos, descricao ->
                    viewModel.atualizarCaracteristica(
                        item.copy(
                            nome = nome,
                            custo = custo,
                            requisitos = requisitos,
                            descricao = descricao
                        )
                    )
                    itemToEdit = null
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// CARD DA CARACTERÍSTICA
// ─────────────────────────────────────────────────────────────
@Composable
fun CaracteristicaCard(
    caracteristica: CaracteristicaAssimilacaoEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val cor = corDoCusto(caracteristica.custo)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Badge de custo
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(cor.copy(alpha = 0.15f))
                            .border(1.5.dp, cor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${caracteristica.custo}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = cor
                        )
                    }

                    Column {
                        Text(
                            caracteristica.nome,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (caracteristica.requisitos.isNotBlank()) {
                            Text(
                                "Req: ${caracteristica.requisitos}",
                                fontSize = 11.sp,
                                color = cor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Default.Edit, null,
                            Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Default.Delete, null,
                            Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (caracteristica.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    caracteristica.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// DIALOG — ADICIONAR / EDITAR CARACTERÍSTICA
// ─────────────────────────────────────────────────────────────
@Composable
fun CaracteristicaDialog(
    caracteristica: CaracteristicaAssimilacaoEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String) -> Unit
) {
    var nome       by remember { mutableStateOf(caracteristica?.nome ?: "") }
    var custo      by remember { mutableStateOf(caracteristica?.custo ?: 1) }
    var requisitos by remember { mutableStateOf(caracteristica?.requisitos ?: "") }
    var descricao  by remember { mutableStateOf(caracteristica?.descricao ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (caracteristica == null) "Nova Característica" else "Editar Característica",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.colors() // ← isso aqui
                )

                // Seletor de custo (1 a 5)
                Column {
                    Text(
                        "Custo em pontos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 1..5) {
                            val cor = corDoCusto(i)
                            val selecionado = custo == i
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selecionado) cor.copy(alpha = 0.2f)
                                        else Color.Transparent
                                    )
                                    .border(
                                        width = if (selecionado) 2.dp else 1.dp,
                                        color = if (selecionado) cor else cor.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { custo = i }) {
                                    Text(
                                        "$i",
                                        fontSize = 16.sp,
                                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                        color = cor
                                    )
                                }
                            }
                        }
                    }
                }

                // Requisitos
                OutlinedTextField(
                    value = requisitos,
                    onValueChange = { requisitos = it },
                    label = { Text("Requisitos (opcional)") },
                    placeholder = { Text("Ex: Potência 2+, Resolução 2+") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Descrição
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição / Efeito") },
                    placeholder = { Text("Descreva o efeito mecânico...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConfirm(nome, custo, requisitos, descricao) },
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