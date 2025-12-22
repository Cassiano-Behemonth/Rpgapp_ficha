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
import com.example.rpgapp.data.entity.ItemVelhoOesteEntity
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel

@Composable
fun EquipamentoVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel
) {
    val itens by viewModel.itens.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ItemVelhoOesteEntity?>(null) }

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
                        "▸ EQUIPAMENTO",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${itens.size} item(ns)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                FilledTonalButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Adicionar", fontSize = 13.sp)
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔫", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Equipamento vazio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Adicione armas, itens, cavalos e equipamentos",
                            style = MaterialTheme.typography.bodyMedium
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
                        ItemVelhoOesteCard(
                            item = item,
                            onEdit = { itemToEdit = item },
                            onDelete = { viewModel.deletarItem(item) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            ItemVelhoOesteDialog(
                title = "Adicionar Item",
                item = null,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, tipo, quantidade, descricao ->
                    viewModel.adicionarItem(nome, tipo, quantidade, descricao)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { item ->
            ItemVelhoOesteDialog(
                title = "Editar Item",
                item = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { nome, tipo, quantidade, descricao ->
                    viewModel.atualizarItem(
                        item.copy(
                            nome = nome,
                            tipo = tipo,
                            quantidade = quantidade,
                            descricao = descricao
                        )
                    )
                    itemToEdit = null
                }
            )
        }
    }
}

@Composable
fun ItemVelhoOesteCard(
    item: ItemVelhoOesteEntity,
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
                        item.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${item.tipo} • Qtd: ${item.quantidade}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Edit, null, Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            null,
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (item.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(item.descricao, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ItemVelhoOesteDialog(
    title: String,
    item: ItemVelhoOesteEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(item?.nome ?: "") }
    var tipo by remember { mutableStateOf(item?.tipo ?: "Item") }
    var quantidade by remember { mutableStateOf(item?.quantidade ?: "1") }
    var descricao by remember { mutableStateOf(item?.descricao ?: "") }

    val tipos = listOf("Arma", "Item", "Equipamento", "Cavalo", "Munição")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    label = { Text("Quantidade") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConfirm(nome, tipo, quantidade, descricao) },
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