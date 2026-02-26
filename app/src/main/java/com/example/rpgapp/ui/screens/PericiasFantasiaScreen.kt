package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpgapp.viewmodel.FichaFantasiaViewModel
import com.example.rpgapp.data.entity.PericiaFantasiaEntity
import kotlinx.coroutines.delay

@Composable
fun PericiasFantasiaScreen(
    onBack: () -> Unit,
    viewModel: FichaFantasiaViewModel = viewModel()
) {
    val pericias by viewModel.pericias.collectAsState()
    val ficha by viewModel.ficha.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val historicoRolagens by viewModel.historicoRolagens.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    "▸ PERÍCIAS",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${pericias.size} perícia(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (pericias.isEmpty()) {
                    FilledTonalButton(
                        onClick = {
                            viewModel.adicionarPericiaPadrao()
                        }
                    ) {
                        Text("Padrão", fontSize = 12.sp)
                    }
                }

                FilledTonalButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Adicionar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Nova", fontSize = 12.sp)
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
                        "📜 Últimas Rolagens",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    historicoRolagens.take(3).forEach { rolagem ->
                        Text(
                            "▹ $rolagem",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (pericias.isEmpty()) {
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
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "🎲",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        "Nenhuma perícia cadastrada",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Adicione perícias manualmente ou use o conjunto padrão",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Lista de perícias
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pericias.forEach { pericia ->
                    PericiaFantasiaCard(
                        pericia = pericia,
                        ficha = ficha,
                        onTreinoChange = {
                            viewModel.atualizarPericia(pericia.copy(treinada = it))
                        },
                        onVantagemChange = {
                            viewModel.atualizarPericia(
                                pericia.copy(
                                    vantagem = it,
                                    desvantagem = if (it) false else pericia.desvantagem
                                )
                            )
                        },
                        onDesvantagemChange = {
                            viewModel.atualizarPericia(
                                pericia.copy(
                                    desvantagem = it,
                                    vantagem = if (it) false else pericia.vantagem
                                )
                            )
                        },
                        onBonusChange = {
                            viewModel.atualizarPericia(pericia.copy(bonus = it.toIntOrNull() ?: 0))
                        },
                        onDelete = { viewModel.deletarPericia(pericia) },
                        onRolar = { resultado ->
                            viewModel.adicionarRolagem(resultado)
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        PericiaFantasiaDialog(
            title = "Adicionar Perícia",
            pericia = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { nome, atributo ->
                viewModel.adicionarPericia(nome, atributo)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PericiaFantasiaCard(
    pericia: PericiaFantasiaEntity,
    ficha: com.example.rpgapp.data.entity.FichaFantasiaEntity?,
    onTreinoChange: (Boolean) -> Unit,
    onVantagemChange: (Boolean) -> Unit,
    onDesvantagemChange: (Boolean) -> Unit,
    onBonusChange: (String) -> Unit,
    onDelete: () -> Unit,
    onRolar: (String) -> Unit
) {
    val modAtributo = when (pericia.atributo) {
        "FOR" -> ficha?.modForca() ?: 0
        "DES" -> ficha?.modDestreza() ?: 0
        "CON" -> ficha?.modConstituicao() ?: 0
        "INT" -> ficha?.modInteligencia() ?: 0
        "SAB" -> ficha?.modSabedoria() ?: 0
        "CAR" -> ficha?.modCarisma() ?: 0
        else -> 0
    }

    val nivel = ficha?.nivel ?: 1
    val modificador = pericia.calcularModificador(modAtributo, nivel)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Linha 1: Nome, Atributo, Modificador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        pericia.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${pericia.atributo} • Mod: ${pericia.formatarModificador(modAtributo, nivel)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Botão de rolar
                FilledTonalButton(
                    onClick = {
                        val resultado = rolarPericia(pericia, modificador)
                        onRolar(resultado)
                    },
                    modifier = Modifier.height(40.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("🎲", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Linha 2: Controles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bônus
                OutlinedTextField(
                    value = if (pericia.bonus == 0) "" else pericia.bonus.toString(),
                    onValueChange = { if (it.length <= 3) onBonusChange(it) },
                    label = { Text("Bônus", fontSize = 10.sp) },
                    modifier = Modifier.width(70.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Checkboxes: Treino, Vantagem, Desvantagem
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Treino (checkbox vermelha quando marcada)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onTreinoChange(!pericia.treinada) }
                    ) {
                        Checkbox(
                            checked = pericia.treinada,
                            onCheckedChange = onTreinoChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        Text("T", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Vantagem
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onVantagemChange(!pericia.vantagem) }
                    ) {
                        Checkbox(
                            checked = pericia.vantagem,
                            onCheckedChange = onVantagemChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier.size(32.dp)
                        )
                        Text("V", fontSize = 11.sp)
                    }

                    // Desvantagem
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onDesvantagemChange(!pericia.desvantagem) }
                    ) {
                        Checkbox(
                            checked = pericia.desvantagem,
                            onCheckedChange = onDesvantagemChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.size(32.dp)
                        )
                        Text("D", fontSize = 11.sp)
                    }
                }

                // Deletar
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

fun rolarPericia(pericia: PericiaFantasiaEntity, modificador: Int): String {
    val dado1 = (1..20).random()
    val dado2 = (1..20).random()

    return when {
        pericia.vantagem -> {
            val maior = maxOf(dado1, dado2)
            val total = maior + modificador
            "${pericia.nome} (Vantagem): $dado1, $dado2 → $maior + $modificador = $total"
        }
        pericia.desvantagem -> {
            val menor = minOf(dado1, dado2)
            val total = menor + modificador
            "${pericia.nome} (Desvantagem): $dado1, $dado2 → $menor + $modificador = $total"
        }
        else -> {
            val total = dado1 + modificador
            "${pericia.nome}: $dado1 + $modificador = $total"
        }
    }
}

@Composable
fun PericiaFantasiaDialog(
    title: String,
    pericia: PericiaFantasiaEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var nome by remember { mutableStateOf(pericia?.nome ?: "") }
    var atributoSelecionado by remember { mutableStateOf(pericia?.atributo ?: "FOR") }
    var expandedAtributo by remember { mutableStateOf(false) }

    val atributos = listOf("FOR", "DES", "CON", "INT", "SAB", "CAR")

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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da Perícia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Box {
                    OutlinedTextField(
                        value = atributoSelecionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Atributo") },
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
                        atributos.forEach { atributo ->
                            DropdownMenuItem(
                                text = { Text(atributo) },
                                onClick = {
                                    atributoSelecionado = atributo
                                    expandedAtributo = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nome.isNotBlank()) {
                        onConfirm(nome, atributoSelecionado)
                    }
                },
                enabled = nome.isNotBlank()
            ) {
                Text(
                    "Salvar",
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
