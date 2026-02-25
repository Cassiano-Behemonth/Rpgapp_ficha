package com.example.rpgapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.ItemAssimilacaoEntity
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel

private const val SLOTS_PADRAO = 10

@Composable
fun InventarioAssimilacaoScreen(
    viewModel: FichaAssimilacaoViewModel
) {
    val itens by viewModel.itens.collectAsState()
    val slotsUsados by viewModel.slotsUsados.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ItemAssimilacaoEntity?>(null) }

    val slotsRestantes = SLOTS_PADRAO - slotsUsados
    val sobrecarga = slotsUsados > SLOTS_PADRAO

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
                        "▸ INVENTÁRIO",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${itens.size} item(ns)",
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

            // ── Card de slots ─────────────────────────────────
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
                                "🎒 Capacidade",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "$slotsUsados / $SLOTS_PADRAO slots",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (sobrecarga)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.primary
                            )
                        }

                        if (sobrecarga) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "⚠️ SOBRECARGA",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    "${slotsUsados - SLOTS_PADRAO} slot(s) excesso",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        } else {
                            Text(
                                "$slotsRestantes restante(s)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Barra de slots visuais
                    SlotBar(
                        usados = slotsUsados,
                        total = SLOTS_PADRAO
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (itens.isEmpty()) {
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
                        Text("🎒", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Inventário vazio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Adicione itens, equipamentos e suprimentos",
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
                    itens.forEach { item ->
                        ItemAssimilacaoCard(
                            item = item,
                            onEdit = { itemToEdit = item },
                            onDelete = { viewModel.deletarItem(item) }
                        )
                    }
                }
            }
        }

        // ── Dialogs ──────────────────────────────────────────
        if (showAddDialog) {
            ItemAssimilacaoDialog(
                item = null,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, quantidade, descricao, slots ->
                    viewModel.adicionarItem(nome, quantidade, descricao, slots)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { item ->
            ItemAssimilacaoDialog(
                item = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { nome, quantidade, descricao, slots ->
                    viewModel.atualizarItem(
                        item.copy(
                            nome = nome,
                            quantidade = quantidade,
                            descricao = descricao,
                            slots = slots
                        )
                    )
                    itemToEdit = null
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// BARRA DE SLOTS VISUAL
// ─────────────────────────────────────────────────────────────
@Composable
fun SlotBar(usados: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        for (i in 1..total) {
            val ocupado = i <= usados
            val sobrecarga = usados > total
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            ocupado && sobrecarga -> MaterialTheme.colorScheme.error
                            ocupado -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// CARD DO ITEM
// ─────────────────────────────────────────────────────────────
@Composable
fun ItemAssimilacaoCard(
    item: ItemAssimilacaoEntity,
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
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.nome,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Qtd: ${item.quantidade}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("•", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "${item.slotsTotal()} slot(s)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
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

            if (item.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    item.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// DIALOG — ADICIONAR / EDITAR ITEM
// ─────────────────────────────────────────────────────────────
@Composable
fun ItemAssimilacaoDialog(
    item: ItemAssimilacaoEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int) -> Unit
) {
    var nome      by remember { mutableStateOf(item?.nome ?: "") }
    var quantidade by remember { mutableStateOf(item?.quantidade ?: "1") }
    var descricao  by remember { mutableStateOf(item?.descricao ?: "") }
    var slots      by remember { mutableStateOf(item?.slots?.toString() ?: "1") }

    // Preview de slots totais
    val slotsTotal = (quantidade.toIntOrNull() ?: 1) * (slots.toIntOrNull() ?: 1)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (item == null) "Adicionar Item" else "Editar Item",
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
                    label = { Text("Nome do item") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Quantidade e Slots lado a lado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantidade,
                        onValueChange = { if (it.length <= 3) quantidade = it },
                        label = { Text("Qtd") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = slots,
                        onValueChange = { if (it.length <= 2) slots = it },
                        label = { Text("Slots") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("1") }
                    )
                }

                // Preview de slots totais
                Text(
                    "Slots totais: $slotsTotal / $SLOTS_PADRAO disponíveis",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Descrição
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nome.isNotBlank())
                        onConfirm(nome, quantidade, descricao, slots.toIntOrNull() ?: 1)
                },
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