package com.example.rpgapp.ui.screens.velhooeste

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel
import kotlinx.coroutines.delay

@Composable
fun DescricaoVelhoOesteScreen(
    viewModel: FichaVelhoOesteViewModel
) {
    val ficha by viewModel.ficha.collectAsState()

    var nome by remember { mutableStateOf("") }
    var jogador by remember { mutableStateOf("") }
    var arquetipo by remember { mutableStateOf("") }
    var origem by remember { mutableStateOf("") }
    var reputacao by remember { mutableStateOf("") }
    var aparencia by remember { mutableStateOf("") }
    var personalidade by remember { mutableStateOf("") }
    var historia by remember { mutableStateOf("") }
    var anotacoes by remember { mutableStateOf("") }
    var showSaveConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            jogador = it.jogador
            arquetipo = it.arquetipo
            origem = it.origem
            reputacao = it.reputacao
            aparencia = it.aparencia
            personalidade = it.personalidade
            historia = it.historia
            anotacoes = it.anotacoes
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "▸ DESCRIÇÃO DO PERSONAGEM",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Informações Básicas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "INFORMAÇÕES BÁSICAS",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = jogador,
                    onValueChange = { jogador = it },
                    label = { Text("Nome do Jogador") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = arquetipo,
                        onValueChange = { arquetipo = it },
                        label = { Text("Arquétipo") },
                        placeholder = { Text("Ex: Pistoleiro") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = origem,
                        onValueChange = { origem = it },
                        label = { Text("Origem") },
                        placeholder = { Text("Ex: Texas") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reputacao,
                    onValueChange = { reputacao = it },
                    label = { Text("Reputação") },
                    placeholder = { Text("Ex: Herói, Neutro, Bandido") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Aparência
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "APARÊNCIA",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = aparencia,
                    onValueChange = { aparencia = it },
                    placeholder = { Text("Descreva a aparência...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        }

        // Personalidade
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "PERSONALIDADE",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = personalidade,
                    onValueChange = { personalidade = it },
                    placeholder = { Text("Descreva a personalidade...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        }

        // História
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "HISTÓRIA",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = historia,
                    onValueChange = { historia = it },
                    placeholder = { Text("Conte o passado...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 7
                )
            }
        }

        // Anotações
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "ANOTAÇÕES",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = anotacoes,
                    onValueChange = { anotacoes = it },
                    placeholder = { Text("Anotações gerais...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 7
                )
            }
        }

        // Botão salvar
        Button(
            onClick = {
                viewModel.salvarDescricao(
                    nome, jogador, arquetipo, origem, reputacao,
                    aparencia, personalidade, historia, anotacoes
                )
                showSaveConfirmation = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 SALVAR DESCRIÇÃO", fontWeight = FontWeight.Bold)
        }
    }

    if (showSaveConfirmation) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSaveConfirmation = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar {
                Text("✓ Descrição salva!")
            }
        }
    }
}