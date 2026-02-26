package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.data.entity.MagiaFantasiaEntity
import com.example.rpgapp.utils.DiceRoller

@Composable
fun MagiasFantasiaScreen(
    onBack: () -> Unit,
    viewModel: com.example.rpgapp.viewmodel.FichaFantasiaViewModel
) {
    val magias by viewModel.magias.collectAsState()
    val ficha by viewModel.ficha.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var magiaToEdit by remember { mutableStateOf<MagiaFantasiaEntity?>(null) }
    var circuloFiltro by remember { mutableStateOf(-1) } // -1 = Todos
    var atributoChave by remember { mutableStateOf("INT") }
    val historicoRolagens by viewModel.historicoRolagens.collectAsState()
    var dadoCustom by remember { mutableStateOf("") }

    // Agrupa magias por círculo
    val magiasPorCirculo = magias.groupBy { it.circulo }
    val circulos = listOf(-1) + magiasPorCirculo.keys.sorted()

    val magiasFiltradas = if (circuloFiltro == -1) {
        magias.sortedBy { it.circulo }
    } else {
        magias.filter { it.circulo == circuloFiltro }.sortedBy { it.nome }
    }

    // Calcula modificador do atributo-chave
    val modAtributoChave = when (atributoChave) {
        "INT" -> ficha?.modInteligencia() ?: 0
        "SAB" -> ficha?.modSabedoria() ?: 0
        "CAR" -> ficha?.modCarisma() ?: 0
        else -> 0
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
                        "▸ MAGIAS",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${magias.size} magia(s)",
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

            // Card de Atributo-chave e CD
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
                            "Atributo-chave",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("INT", "SAB", "CAR").forEach { attr ->
                                FilterChip(
                                    selected = atributoChave == attr,
                                    onClick = { atributoChave = attr },
                                    label = { Text(attr, fontSize = 12.sp) }
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "CD Base",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "10 + círculo + $modAtributoChave",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Histórico de rolagens
            if (historicoRolagens.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "📜 ÚLTIMAS ROLAGENS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        historicoRolagens.take(4).forEach { rolagem ->
                            Text(
                                "▹ $rolagem",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 2.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Filtro por círculo dentro do LazyColumn
                if (circulos.size > 1) {
                    item {
                        ScrollableTabRow(
                            selectedTabIndex = circulos.indexOf(circuloFiltro),
                            edgePadding = 0.dp,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            circulos.forEach { circulo ->
                                val label = if (circulo == -1) "Todas" else if (circulo == 0) "Truques" else "${circulo}º"
                                val count = if (circulo == -1) {
                                    magias.size
                                } else {
                                    magiasPorCirculo[circulo]?.size ?: 0
                                }

                                Tab(
                                    selected = circuloFiltro == circulo,
                                    onClick = { circuloFiltro = circulo },
                                    text = {
                                        Text(
                                            "$label ($count)",
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
                }

                if (magiasFiltradas.isEmpty()) {
                    item {
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
                                    "✨",
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Text(
                                    if (circuloFiltro == -1) "Nenhuma magia cadastrada" else "Nenhuma magia neste círculo",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Adicione magias e organize seu grimório",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(items = magiasFiltradas, key = { it.id }) { magia ->
                        MagiaFantasiaCard(
                            magia = magia,
                            modAtributo = modAtributoChave,
                            onEdit = { magiaToEdit = magia },
                            onDelete = { viewModel.deletarMagia(magia) },
                            onRolarAcerto = { acerto ->
                                val sucesso = viewModel.consumirPM(magia.custoPM)
                                if (sucesso) {
                                    val (resultado, texto) = DiceRoller.rolarAcerto(acerto)
                                    if (resultado > 0) {
                                        viewModel.adicionarRolagem("${magia.nome} - ACERTO - $texto (-${magia.custoPM} PM)")
                                    }
                                } else {
                                    viewModel.adicionarRolagem("❌ PM insuficiente para ${magia.nome}")
                                }
                            },
                            onRolarDano = { dano ->
                                val (resultado, texto) = DiceRoller.rolarDano(dano)
                                if (resultado > 0) {
                                    viewModel.adicionarRolagem("${magia.nome} - LANÇAMENTO - $texto")
                                }
                            },
                            onConjurar = {
                                val sucesso = viewModel.consumirPM(magia.custoPM)
                                if (sucesso) {
                                    viewModel.adicionarRolagem("Magia: ${magia.nome} (-${magia.custoPM} PM)")
                                } else {
                                    viewModel.adicionarRolagem("❌ PM insuficiente para ${magia.nome}")
                                }
                            }
                        )
                    }
                }
            }
        }

        // Dialog de adicionar
        if (showAddDialog) {
            MagiaFantasiaDialog(
                title = "Adicionar Magia",
                magia = null,
                atributoChaveDefault = atributoChave,
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, escola, circulo, custoPM, execucao, alcance, area, duracao, resistencia, atribChave, efeito, componentes, acerto, dano ->
                    viewModel.adicionarMagia(nome, escola, circulo, execucao, alcance, area, duracao, resistencia, atribChave, efeito, componentes, acerto, dano, custoPM)
                    showAddDialog = false
                }
            )
        }

        // Dialog de editar
        magiaToEdit?.let { magia ->
            MagiaFantasiaDialog(
                title = "Editar Magia",
                magia = magia,
                atributoChaveDefault = atributoChave,
                onDismiss = { magiaToEdit = null },
                onConfirm = { nome, escola, circulo, custoPM, execucao, alcance, area, duracao, resistencia, atribChave, efeito, componentes, acerto, dano ->
                    viewModel.atualizarMagia(
                        magia.copy(
                            nome = nome,
                            escola = escola,
                            circulo = circulo,
                            custoPM = custoPM,
                            execucao = execucao,
                            alcance = alcance,
                            area = area,
                            duracao = duracao,
                            resistencia = resistencia,
                            atributoChave = atribChave,
                            efeito = efeito,
                            componentes = componentes,
                            acerto = acerto,
                            dano = dano
                        )
                    )
                    magiaToEdit = null
                }
            )
        }
    }
}

@Composable
fun MagiaFantasiaCard(
    magia: MagiaFantasiaEntity,
    modAtributo: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onRolarAcerto: (String) -> Unit,
    onRolarDano: (String) -> Unit,
    onConjurar: () -> Unit
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
                            magia.getEmojiEscola(),
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            magia.nome,
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
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    magia.formatarCirculo(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    "${magia.custoPM} PM",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (magia.escola.isNotBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    magia.escola,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
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
                    
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            magia.formatarCD(modAtributo),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Informações da magia
            if (magia.getInfoResumo().isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    magia.getInfoResumo(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Área e Duração
            if (magia.area.isNotBlank() || magia.duracao.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                val info = mutableListOf<String>()
                if (magia.area.isNotBlank()) info.add("Área: ${magia.area}")
                if (magia.duracao.isNotBlank()) info.add("Duração: ${magia.duracao}")
                Text(
                    info.joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Resistência
            if (magia.temResistencia()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    magia.formatarResistencia(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }

            // Botões de rolagem
            if (magia.acerto.isNotBlank() || magia.dano.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (magia.acerto.isNotBlank()) {
                        FilledTonalButton(
                            onClick = { onRolarAcerto(magia.acerto) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🎯 Acerto: ${magia.acerto}", fontSize = 12.sp)
                        }
                    }
                    if (magia.dano.isNotBlank()) {
                        FilledTonalButton(
                            onClick = { onRolarDano(magia.dano) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Lançar: ${magia.dano}", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Efeito
            if (magia.efeito.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    magia.efeito,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Componentes
            if (magia.componentes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Componentes: ${magia.formatarComponentes()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun MagiaFantasiaDialog(
    title: String,
    magia: MagiaFantasiaEntity?,
    atributoChaveDefault: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Int, String, String, String, String, String, String, String, String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(magia?.nome ?: "") }
    var escola by remember { mutableStateOf(magia?.escola ?: "") }
    var circulo by remember { mutableStateOf(magia?.circulo?.toString() ?: "1") }
    var custoPM by remember { mutableStateOf(magia?.custoPM?.toString() ?: "1") }
    var execucao by remember { mutableStateOf(magia?.execucao ?: "") }
    var alcance by remember { mutableStateOf(magia?.alcance ?: "") }
    var area by remember { mutableStateOf(magia?.area ?: "") }
    var duracao by remember { mutableStateOf(magia?.duracao ?: "") }
    var resistencia by remember { mutableStateOf(magia?.resistencia ?: "") }
    var atributoChave by remember { mutableStateOf(magia?.atributoChave ?: atributoChaveDefault) }
    var efeito by remember { mutableStateOf(magia?.efeito ?: "") }
    var componentes by remember { mutableStateOf(magia?.componentes ?: "") }
    var acerto by remember { mutableStateOf(magia?.acerto ?: "") }
    var dano by remember { mutableStateOf(magia?.dano ?: "") }

    var expandedEscola by remember { mutableStateOf(false) }
    var expandedAtributo by remember { mutableStateOf(false) }

    val escolas = listOf("Abjuração", "Convocação", "Adivinhação", "Encantamento", "Evocação", "Ilusão", "Necromancia", "Transmutação", "Universal")
    val atributos = listOf("INT", "SAB", "CAR")

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
                    label = { Text("Nome da Magia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Dropdown escola
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = escola,
                            onValueChange = { escola = it },
                            label = { Text("Escola") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Selecionar",
                                    modifier = Modifier.clickable { expandedEscola = true }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedEscola = true },
                            singleLine = true
                        )

                        DropdownMenu(
                            expanded = expandedEscola,
                            onDismissRequest = { expandedEscola = false }
                        ) {
                            escolas.forEach { esc ->
                                DropdownMenuItem(
                                    text = { Text(esc) },
                                    onClick = {
                                        escola = esc
                                        expandedEscola = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = circulo,
                        onValueChange = { if (it.length <= 1) circulo = it },
                        label = { Text("Círculo") },
                        modifier = Modifier.width(90.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = custoPM,
                        onValueChange = { if (it.length <= 2) custoPM = it },
                        label = { Text("Custo PM") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                // Dropdown atributo-chave
                Box {
                    OutlinedTextField(
                        value = atributoChave,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Atributo-chave") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Selecionar",
                                modifier = Modifier.clickable { expandedAtributo = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedAtributo = true }
                    )

                    DropdownMenu(
                        expanded = expandedAtributo,
                        onDismissRequest = { expandedAtributo = false }
                    ) {
                        atributos.forEach { attr ->
                            DropdownMenuItem(
                                text = { Text(attr) },
                                onClick = {
                                    atributoChave = attr
                                    expandedAtributo = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = execucao,
                        onValueChange = { execucao = it },
                        label = { Text("Execução") },
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
                        placeholder = { Text("Médio") }
                    )
                }

                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Área") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ex: cone de 6m") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = duracao,
                        onValueChange = { duracao = it },
                        label = { Text("Duração") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("Cena") }
                    )

                    OutlinedTextField(
                        value = resistencia,
                        onValueChange = { resistencia = it },
                        label = { Text("Resistência") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("Vontade") }
                    )
                }

                OutlinedTextField(
                    value = componentes,
                    onValueChange = { componentes = it },
                    label = { Text("Componentes") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("V, G, M") }
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
                        label = { Text("Dano/cura..") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("3d6") }
                    )
                }

                OutlinedTextField(
                    value = efeito,
                    onValueChange = { efeito = it },
                    label = { Text("Efeito / Descrição") },
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
                            escola,
                            circulo.toIntOrNull() ?: 1,
                            custoPM.toIntOrNull() ?: 1,
                            execucao,
                            alcance,
                            area,
                            duracao,
                            resistencia,
                            atributoChave,
                            efeito,
                            componentes,
                            acerto,
                            dano
                        )
                    }
                },
                enabled = nome.isNotBlank()
            ) {
                Text(
                    if (magia == null) "Adicionar" else "Salvar",
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
