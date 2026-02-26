package com.example.rpgapp.ui.screens

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.ItemFantasiaEntity
import com.example.rpgapp.utils.DiceRoller

@Composable
fun InventarioFantasiaScreen(
    onBack: () -> Unit,
    viewModel: com.example.rpgapp.viewmodel.FichaFantasiaViewModel
) {
    val itens by viewModel.itens.collectAsState()
    val ficha by viewModel.ficha.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ItemFantasiaEntity?>(null) }

    val slotsUsados = itens.sumOf { it.slotsTotal() }
    val limiteSlots = ficha?.limiteCarga() ?: 0

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

                FilledTonalButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Adicionar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Adicionar", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Carga",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "$slotsUsados / $limiteSlots slots",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (slotsUsados > limiteSlots) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    }

                    if (slotsUsados > limiteSlots) {
                        Text(
                            "⚠️ SOBRECARGA",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "🎒",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            "Inventário vazio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Adicione itens, armas, armaduras e equipamentos",
                            style = MaterialTheme.typography.bodyMedium,
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
                        ItemFantasiaCard(
                            item = item,
                            onEdit = { itemToEdit = item },
                            onDelete = { viewModel.deletarItem(item) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            ItemFantasiaDialog(
                title = "Adicionar Item",
                item = null,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, quantidade, descricao, slots, bonusDefesa, bonusFort, bonusRef, bonusVont, bonusAtributo, tipo ->
                    viewModel.adicionarItem(nome, quantidade, descricao, slots, bonusDefesa, bonusFort, bonusRef, bonusVont, bonusAtributo, tipo)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { item ->
            ItemFantasiaDialog(
                title = "Editar Item",
                item = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { nome, quantidade, descricao, slots, bonusDefesa, bonusFort, bonusRef, bonusVont, bonusAtributo, tipo ->
                    viewModel.atualizarItem(
                        item.copy(
                            nome = nome,
                            quantidade = quantidade,
                            descricao = descricao,
                            slots = slots,
                            bonusDefesa = bonusDefesa,
                            bonusFortitude = bonusFort,
                            bonusReflexos = bonusRef,
                            bonusVontade = bonusVont,
                            bonusAtributo = bonusAtributo,
                            tipo = tipo
                        )
                    )
                    itemToEdit = null
                }
            )
        }
    }
}

@Composable
fun ItemFantasiaCard(
    item: ItemFantasiaEntity,
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            item.nome,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (item.tipo != "Geral") {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    item.tipo,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Qtd: ${item.quantidade}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "${item.slotsTotal()} slot(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Deletar",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            val todosBonus = item.formatarTodosBonus()
            if (todosBonus.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "⚡",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            todosBonus,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }


            if (item.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    item.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ItemFantasiaDialog(
    title: String,
    item: ItemFantasiaEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int, Int, Int, Int, Int, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(item?.nome ?: "") }
    var quantidade by remember { mutableStateOf(item?.quantidade ?: "1") }
    var descricao by remember { mutableStateOf(item?.descricao ?: "") }
    var slots by remember { mutableStateOf(item?.slots?.toString() ?: "1") }
    var bonusDefesa by remember { mutableStateOf(item?.bonusDefesa?.toString() ?: "0") }
    var bonusFortitude by remember { mutableStateOf(item?.bonusFortitude?.toString() ?: "0") }
    var bonusReflexos by remember { mutableStateOf(item?.bonusReflexos?.toString() ?: "0") }
    var bonusVontade by remember { mutableStateOf(item?.bonusVontade?.toString() ?: "0") }
    var bonusAtributo by remember { mutableStateOf(item?.bonusAtributo ?: "") }
    var tipo by remember { mutableStateOf(item?.tipo ?: "Geral") }

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
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do Item") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantidade,
                        onValueChange = { quantidade = it },
                        label = { Text("Qtd") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = slots,
                        onValueChange = { if (it.length <= 2) slots = it },
                        label = { Text("Slots") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo (Geral, Arma, Armadura...)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                HorizontalDivider()

                Text(
                    "Bônus de Defesas",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = bonusDefesa,
                        onValueChange = { if (it.length <= 3) bonusDefesa = it },
                        label = { Text("🛡️ Def", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("0") }
                    )

                    OutlinedTextField(
                        value = bonusFortitude,
                        onValueChange = { if (it.length <= 3) bonusFortitude = it },
                        label = { Text("💪 Fort", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("0") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = bonusReflexos,
                        onValueChange = { if (it.length <= 3) bonusReflexos = it },
                        label = { Text("⚡ Ref", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("0") }
                    )

                    OutlinedTextField(
                        value = bonusVontade,
                        onValueChange = { if (it.length <= 3) bonusVontade = it },
                        label = { Text("🧠 Vont", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("0") }
                    )
                }

                OutlinedTextField(
                    value = bonusAtributo,
                    onValueChange = { bonusAtributo = it },
                    label = { Text("Bônus de Atributo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ex: FOR+2 ou DES-1") }
                )


                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nome.isNotBlank()) {
                        onConfirm(
                            nome,
                            quantidade,
                            descricao,
                            slots.toIntOrNull() ?: 1,
                            bonusDefesa.toIntOrNull() ?: 0,
                            bonusFortitude.toIntOrNull() ?: 0,
                            bonusReflexos.toIntOrNull() ?: 0,
                            bonusVontade.toIntOrNull() ?: 0,
                            bonusAtributo,
                            tipo
                        )
                    }
                },
                enabled = nome.isNotBlank()
            ) {
                Text(
                    if (item == null) "Adicionar" else "Salvar",
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
