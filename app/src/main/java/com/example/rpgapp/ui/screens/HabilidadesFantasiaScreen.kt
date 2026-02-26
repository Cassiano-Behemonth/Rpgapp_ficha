package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.HabilidadeFantasiaEntity
import androidx.compose.foundation.clickable
import com.example.rpgapp.utils.DiceRoller

@Composable
fun HabilidadesFantasiaScreen(
    onBack: () -> Unit,
    viewModel: com.example.rpgapp.viewmodel.FichaFantasiaViewModel
) {
    val habilidades by viewModel.habilidades.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var habilidadeToEdit by remember { mutableStateOf<HabilidadeFantasiaEntity?>(null) }
    var categoriaFiltro by remember { mutableStateOf("Todas") }
    var historicoRolagens by remember { mutableStateOf<List<String>>(emptyList()) }
    var dadoCustom by remember { mutableStateOf("") }

    // Agrupa habilidades por categoria
    val habilidadesPorCategoria = habilidades.groupBy { it.categoria }
    val categorias = listOf("Todas") + habilidadesPorCategoria.keys.sorted()

    val habilidadesFiltradas = if (categoriaFiltro == "Todas") {
        habilidades
    } else {
        habilidades.filter { it.categoria == categoriaFiltro }
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
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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

            // Sistema de rolagem de dados
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🎲 ROLAGEM DE DADOS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = dadoCustom,
                            onValueChange = { dadoCustom = it },
                            label = { Text("Ex: 2d6+3, 1d20", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Button(onClick = {
                            val (resultado, texto) = DiceRoller.rolarCustom(dadoCustom)
                            if (resultado > 0) {
                                historicoRolagens = listOf(texto) + historicoRolagens.take(4)
                            }
                        }) {
                            Text("Rolar")
                        }
                    }

                    if (historicoRolagens.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Últimas Rolagens:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        historicoRolagens.take(3).forEach { rolagem ->
                            Text(
                                "▹ $rolagem",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 2.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtro por categoria
            if (categorias.size > 1) {
                ScrollableTabRow(
                    selectedTabIndex = categorias.indexOf(categoriaFiltro),
                    edgePadding = 0.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    categorias.forEach { categoria ->
                        Tab(
                            selected = categoriaFiltro == categoria,
                            onClick = { categoriaFiltro = categoria },
                            text = {
                                val count = if (categoria == "Todas") {
                                    habilidades.size
                                } else {
                                    habilidadesPorCategoria[categoria]?.size ?: 0
                                }
                                Text(
                                    "$categoria ($count)",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 13.sp
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (habilidadesFiltradas.isEmpty()) {
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "⚡",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            if (categoriaFiltro == "Todas") "Nenhuma habilidade cadastrada" else "Nenhuma habilidade em $categoriaFiltro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Adicione habilidades de raça, classe, origem ou poderes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Lista de habilidades
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    habilidadesFiltradas.forEach { habilidade ->
                        HabilidadeFantasiaCard(
                            habilidade = habilidade,
                            onEdit = { habilidadeToEdit = habilidade },
                            onDelete = { viewModel.deletarHabilidade(habilidade) },
                            onRolarAcerto = { acerto ->
                                val (resultado, texto) = DiceRoller.rolarAcerto(acerto)
                                if (resultado > 0) {
                                    historicoRolagens = listOf("${habilidade.nome} - $texto") + historicoRolagens.take(4)
                                }
                            },
                            onRolarDano = { dano ->
                                val (resultado, texto) = DiceRoller.rolarDano(dano)
                                if (resultado > 0) {
                                    historicoRolagens = listOf("${habilidade.nome} - $texto") + historicoRolagens.take(4)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Dialog de adicionar
        if (showAddDialog) {
            HabilidadeFantasiaDialog(
                title = "Adicionar Habilidade",
                habilidade = null,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, categoria, descricao, custoPM, requisitos, acao, alcance, duracao, acerto, dano ->
                    viewModel.adicionarHabilidade(nome, categoria, descricao, custoPM, requisitos, acao, alcance, duracao, acerto, dano)
                    showAddDialog = false
                }
            )
        }

        // Dialog de editar
        habilidadeToEdit?.let { habilidade ->
            HabilidadeFantasiaDialog(
                title = "Editar Habilidade",
                habilidade = habilidade,
                onDismiss = { habilidadeToEdit = null },
                onConfirm = { nome, categoria, descricao, custoPM, requisitos, acao, alcance, duracao, acerto, dano ->
                    viewModel.atualizarHabilidade(
                        habilidade.copy(
                            nome = nome,
                            categoria = categoria,
                            descricao = descricao,
                            custoPM = custoPM,
                            requisitos = requisitos,
                            acao = acao,
                            alcance = alcance,
                            duracao = duracao,
                            acerto = acerto,
                            dano = dano
                        )
                    )
                    habilidadeToEdit = null
                }
            )
        }
    }
}

@Composable
fun HabilidadeFantasiaCard(
    habilidade: HabilidadeFantasiaEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onRolarAcerto: (String) -> Unit,
    onRolarDano: (String) -> Unit
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
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            habilidade.getEmoji(),
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            habilidade.nome,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                habilidade.categoria,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (habilidade.isPoder()) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    habilidade.formatarCusto(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
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

            // Requisitos
            if (habilidade.requisitos.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Requisitos: ${habilidade.requisitos}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            // Informações de combate
            if (habilidade.temInfoCombate()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    habilidade.formatarInfoCombate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botões de rolagem
            if (habilidade.acerto.isNotBlank() || habilidade.dano.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (habilidade.acerto.isNotBlank()) {
                        FilledTonalButton(
                            onClick = { onRolarAcerto(habilidade.acerto) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🎯 Acerto: ${habilidade.acerto}", fontSize = 12.sp)
                        }
                    }
                    if (habilidade.dano.isNotBlank()) {
                        FilledTonalButton(
                            onClick = { onRolarDano(habilidade.dano) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Lançar: ${habilidade.dano}", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Descrição
            if (habilidade.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
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
fun HabilidadeFantasiaDialog(
    title: String,
    habilidade: HabilidadeFantasiaEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int, String, String, String, String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(habilidade?.nome ?: "") }
    var categoria by remember { mutableStateOf(habilidade?.categoria ?: "Classe") }
    var descricao by remember { mutableStateOf(habilidade?.descricao ?: "") }
    var custoPM by remember { mutableStateOf(habilidade?.custoPM?.toString() ?: "0") }
    var requisitos by remember { mutableStateOf(habilidade?.requisitos ?: "") }
    var acao by remember { mutableStateOf(habilidade?.acao ?: "") }
    var alcance by remember { mutableStateOf(habilidade?.alcance ?: "") }
    var duracao by remember { mutableStateOf(habilidade?.duracao ?: "") }
    var acerto by remember { mutableStateOf(habilidade?.acerto ?: "") }
    var dano by remember { mutableStateOf(habilidade?.dano ?: "") }
    var expandedCategoria by remember { mutableStateOf(false) }

    val categorias = listOf("Raça", "Classe", "Poder", "Origem", "Geral")

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
                    label = { Text("Nome da Habilidade") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Dropdown de categoria
                Box {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoria") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Selecionar",
                                modifier = Modifier.clickable { expandedCategoria = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedCategoria = true }
                    )

                    DropdownMenu(
                        expanded = expandedCategoria,
                        onDismissRequest = { expandedCategoria = false }
                    ) {
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    categoria = cat
                                    expandedCategoria = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = custoPM,
                    onValueChange = { if (it.length <= 3) custoPM = it },
                    label = { Text("Custo em PM (0 = passiva)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                HorizontalDivider()

                Text(
                    "Informações Adicionais (opcional)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = requisitos,
                    onValueChange = { requisitos = it },
                    label = { Text("Requisitos") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ex: 5º nível, FOR 13") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = acao,
                        onValueChange = { acao = it },
                        label = { Text("Ação") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("Padrão") }
                    )

                    OutlinedTextField(
                        value = alcance,
                        onValueChange = { alcance = it },
                        label = { Text("Alcance") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("Pessoal") }
                    )
                }

                OutlinedTextField(
                    value = duracao,
                    onValueChange = { duracao = it },
                    label = { Text("Duração") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Instantâneo") }
                )

                HorizontalDivider()

                Text(
                    "Rolagens (opcional)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = acerto,
                        onValueChange = { acerto = it },
                        label = { Text("Acerto") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("1d20") }
                    )

                    OutlinedTextField(
                        value = dano,
                        onValueChange = { dano = it },
                        label = { Text("Dano/Cura..") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("1d6") }
                    )
                }

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 6
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nome.isNotBlank()) {
                        onConfirm(
                            nome,
                            categoria,
                            descricao,
                            custoPM.toIntOrNull() ?: 0,
                            requisitos,
                            acao,
                            alcance,
                            duracao,
                            acerto,
                            dano
                        )
                    }
                },
                enabled = nome.isNotBlank()
            ) {
                Text(
                    if (habilidade == null) "Adicionar" else "Salvar",
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
