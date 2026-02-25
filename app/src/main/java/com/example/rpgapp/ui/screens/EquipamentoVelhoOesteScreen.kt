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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.ItemVelhoOesteEntity
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel

/**
 * Retorna emoji específico baseado no tipo do item
 */
fun getEmojiPorTipo(tipo: String): String {
    return when (tipo.lowercase()) {
        "arma" -> "🔫"
        "rifle" -> "🔫"
        "pistola" -> "🔫"
        "revólver" -> "🔫"
        "espingarda" -> "🔫"
        "faca" -> "🔪"
        "explosivo" -> "💣"
        "dinamite" -> "💣"
        "cavalo" -> "🐴"
        "montaria" -> "🐴"
        "comida" -> "🍖"
        "bebida" -> "🍺"
        "remédio" -> "💊"
        "medicamento" -> "💊"
        "kit médico" -> "💊"
        "roupa" -> "👔"
        "vestimenta" -> "👔"
        "equipamento" -> "🎒"
        "mochila" -> "🎒"
        "ferramenta" -> "🔧"
        "corda" -> "➰"
        "lanterna" -> "🔦"
        "dinheiro" -> "💰"
        "ouro" -> "💰"
        "joia" -> "💎"
        "mapa" -> "🗺️"
        "documento" -> "📜"
        "livro" -> "📖"
        "chave" -> "🔑"
        "munição" -> "🔸"
        "bala" -> "🔸"
        "item" -> "🌐"
        "geral" -> "🌐"
        else -> "🌐"  // Padrão: globo
    }
}

@Composable
fun EquipamentoVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel
) {
    val itens by viewModel.itens.collectAsState()
    val ficha by viewModel.ficha.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ItemVelhoOesteEntity?>(null) }

    // Calcula peso total dos itens
    val pesoUsado = itens.sumOf { it.pesoTotal() }
    val pesoMaximo = ficha?.pesoMaximo ?: 15

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

            // Card de PESO
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
                            "🎒 Peso do Equipamento",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "$pesoUsado / $pesoMaximo kg",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (pesoUsado > pesoMaximo) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )

                        Text(
                            "Limite: 15 + Físico ${ficha?.fisico ?: 0}${if ((ficha?.pesoBonus ?: 0) > 0) " +${ficha?.pesoBonus}" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (pesoUsado > pesoMaximo) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "⚠️ SOBRECARGA",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                "${pesoUsado - pesoMaximo} kg excesso",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
                onConfirm = { nome, tipo, quantidade, peso, descricao, dano ->
                    viewModel.adicionarItem(nome, tipo, quantidade, peso, descricao, dano)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { item ->
            ItemVelhoOesteDialog(
                title = "Editar Item",
                item = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { nome, tipo, quantidade, peso, descricao, dano ->
                    viewModel.atualizarItem(
                        item.copy(
                            nome = nome,
                            tipo = tipo,
                            quantidade = quantidade,
                            peso = peso,
                            descricao = descricao,
                            dano = dano
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
    val emoji = getEmojiPorTipo(item.tipo)

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
                    // Nome com emoji do tipo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            emoji,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            item.nome,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.tipo,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text("•", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "Qtd: ${item.quantidade}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text("•", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "${item.pesoTotal()} kg",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Mostra dano se tiver (com caveira)
                        if (item.dano.isNotBlank()) {
                            Text("•", style = MaterialTheme.typography.bodySmall)
                            Text(
                                "💀 ${item.dano}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
    onConfirm: (String, String, String, Int, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(item?.nome ?: "") }
    var tipo by remember { mutableStateOf(item?.tipo ?: "Geral") }
    var quantidade by remember { mutableStateOf(item?.quantidade ?: "1") }
    var peso by remember { mutableStateOf(item?.peso?.toString() ?: "1") }
    var descricao by remember { mutableStateOf(item?.descricao ?: "") }
    var dano by remember { mutableStateOf(item?.dano ?: "") }

    // Emoji preview
    val emojiPreview = getEmojiPorTipo(tipo)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Tipo com preview do emoji
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = { tipo = it },
                        label = { Text("Tipo") },
                        placeholder = { Text("Arma, Cavalo, Chave, Geral...") },
                        modifier = Modifier.weight(1f)
                    )

                    // Preview do emoji
                    Card(
                        modifier = Modifier.size(56.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                emojiPreview,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }

                Text(
                    "Tipos: Arma 🔫, Cavalo 🐴, Chave 🔑, Comida 🍖, Bebida 🍺, Remédio 💊, Roupa 👔, Mochila 🎒, Munição 🔸, Geral 🌐",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantidade,
                        onValueChange = { quantidade = it },
                        label = { Text("Qtd") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = peso,
                        onValueChange = { if (it.length <= 3) peso = it },
                        label = { Text("Peso") },
                        modifier = Modifier.weight(1f),
                        suffix = { Text("kg", fontSize = 10.sp) },
                        placeholder = { Text("1") }
                    )

                    OutlinedTextField(
                        value = dano,
                        onValueChange = { dano = it },
                        label = { Text("Dano") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("1d6") },
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3
                )

                if (quantidade.toIntOrNull() != null && peso.toIntOrNull() != null) {
                    val pesoTotal = (quantidade.toIntOrNull() ?: 1) * (peso.toIntOrNull() ?: 1)
                    Text(
                        "Peso total: $pesoTotal kg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nome.isNotBlank())
                        onConfirm(
                            nome,
                            tipo,
                            quantidade,
                            peso.toIntOrNull() ?: 1,
                            descricao,
                            dano
                        )
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