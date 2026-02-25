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
import com.example.rpgapp.data.entity.AssimilacaoEntity
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel

// ── Cores e emojis por tipo ──────────────────────────────────
fun corDoTipo(tipo: String): Color = when (tipo) {
    "Evolutiva"  -> Color(0xFF43A047) // Verde
    "Adaptativa" -> Color(0xFF1E88E5) // Azul
    "Inoportuna" -> Color(0xFFE53935) // Vermelho
    "Singular"   -> Color(0xFFFFB300) // Âmbar
    else         -> Color(0xFF757575)
}

fun emojiDoTipo(tipo: String): String = when (tipo) {
    "Evolutiva"  -> "🌿"
    "Adaptativa" -> "🔄"
    "Inoportuna" -> "⚠️"
    "Singular"   -> "⭐"
    else         -> "🧬"
}

val tiposAssimilacao = listOf("Evolutiva", "Adaptativa", "Inoportuna", "Singular")

@Composable
fun AssimilacoesScreen(
    viewModel: FichaAssimilacaoViewModel
) {
    val assimilacoes by viewModel.assimilacoes.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<AssimilacaoEntity?>(null) }

    // Agrupa por tipo para exibição organizada
    val agrupadas = tiposAssimilacao.map { tipo ->
        tipo to assimilacoes.filter { it.tipo == tipo }
    }

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
                        "▸ ASSIMILAÇÕES",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${assimilacoes.size} mutação(ões) registrada(s)",
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

            if (assimilacoes.isEmpty()) {
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
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🧬", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Nenhuma mutação registrada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Registre as mutações adquiridas durante o jogo",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Exibe agrupado por tipo
                    agrupadas.forEach { (tipo, lista) ->
                        if (lista.isNotEmpty()) {
                            GrupoAssimilacao(
                                tipo = tipo,
                                lista = lista,
                                onEdit = { itemToEdit = it },
                                onDelete = { viewModel.deletarAssimilacao(it) }
                            )
                        }
                    }
                }
            }
        }

        // ── Dialogs ──────────────────────────────────────────
        if (showAddDialog) {
            AssimilacaoDialog(
                assimilacao = null,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, tipo, descricao ->
                    viewModel.adicionarAssimilacao(nome, tipo, descricao)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { item ->
            AssimilacaoDialog(
                assimilacao = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { nome, tipo, descricao ->
                    viewModel.atualizarAssimilacao(
                        item.copy(nome = nome, tipo = tipo, descricao = descricao)
                    )
                    itemToEdit = null
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// GRUPO POR TIPO
// ─────────────────────────────────────────────────────────────
@Composable
fun GrupoAssimilacao(
    tipo: String,
    lista: List<AssimilacaoEntity>,
    onEdit: (AssimilacaoEntity) -> Unit,
    onDelete: (AssimilacaoEntity) -> Unit
) {
    val cor = corDoTipo(tipo)
    val emoji = emojiDoTipo(tipo)

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        // Header do grupo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            Text(emoji, fontSize = 16.sp)
            Text(
                tipo.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = cor
            )
            Text(
                "(${lista.size})",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        lista.forEach { assimilacao ->
            AssimilacaoCard(
                assimilacao = assimilacao,
                onEdit = { onEdit(assimilacao) },
                onDelete = { onDelete(assimilacao) }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// CARD DE MUTAÇÃO
// ─────────────────────────────────────────────────────────────
@Composable
fun AssimilacaoCard(
    assimilacao: AssimilacaoEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val cor = corDoTipo(assimilacao.tipo)

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Badge do tipo
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(cor.copy(alpha = 0.15f))
                            .border(1.dp, cor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            assimilacao.tipo,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = cor
                        )
                    }

                    Text(
                        assimilacao.nome,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
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

            if (assimilacao.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    assimilacao.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// DIALOG — ADICIONAR / EDITAR
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssimilacaoDialog(
    assimilacao: AssimilacaoEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nome     by remember { mutableStateOf(assimilacao?.nome ?: "") }
    var tipo     by remember { mutableStateOf(assimilacao?.tipo ?: "Evolutiva") }
    var descricao by remember { mutableStateOf(assimilacao?.descricao ?: "") }
    var expandedTipo by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (assimilacao == null) "Nova Mutação" else "Editar Mutação",
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
                    label = { Text("Nome da mutação") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Tipo — dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedTipo,
                    onExpandedChange = { expandedTipo = it }
                ) {
                    OutlinedTextField(
                        value = "${emojiDoTipo(tipo)} $tipo",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTipo,
                        onDismissRequest = { expandedTipo = false }
                    ) {
                        tiposAssimilacao.forEach { opcao ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(emojiDoTipo(opcao))
                                        Text(opcao)
                                        Spacer(Modifier.weight(1f))
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(corDoTipo(opcao))
                                        )
                                    }
                                },
                                onClick = {
                                    tipo = opcao
                                    expandedTipo = false
                                }
                            )
                        }
                    }
                }

                // Descrição
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição / Efeito") },
                    placeholder = { Text("Descreva o efeito da mutação...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (nome.isNotBlank()) onConfirm(nome, tipo, descricao) },
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