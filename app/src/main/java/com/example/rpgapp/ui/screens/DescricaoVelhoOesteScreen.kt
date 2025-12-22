package com.example.rpgapp.ui.screens.velhooeste

import androidx.compose.animation.*
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
    var aparencia by remember { mutableStateOf("") }
    var personalidade by remember { mutableStateOf("") }
    var historia by remember { mutableStateOf("") }
    var anotacoes by remember { mutableStateOf("") }
    var showSavedMessage by remember { mutableStateOf(false) }

    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            jogador = it.jogador
            aparencia = it.aparencia
            personalidade = it.personalidade
            historia = it.historia
            anotacoes = it.anotacoes
        }
    }

    // Salvamento automático
    LaunchedEffect(nome, jogador, aparencia, personalidade, historia, anotacoes) {
        delay(1000)
        viewModel.salvarDescricao(
            nome, jogador, "", "", "", // arquetipo, origem, reputacao vazios
            aparencia, personalidade, historia, anotacoes
        )
        showSavedMessage = true
        delay(2000)
        showSavedMessage = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "▸ DESCRIÇÃO",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Jogador
            OutlinedTextField(
                value = jogador,
                onValueChange = { jogador = it },
                label = { Text("Jogador") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Aparência
            OutlinedTextField(
                value = aparencia,
                onValueChange = { aparencia = it },
                label = { Text("Aparência") },
                placeholder = { Text("Descreva a aparência...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Personalidade
            OutlinedTextField(
                value = personalidade,
                onValueChange = { personalidade = it },
                label = { Text("Personalidade") },
                placeholder = { Text("Descreva a personalidade...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // História
            OutlinedTextField(
                value = historia,
                onValueChange = { historia = it },
                label = { Text("História") },
                placeholder = { Text("Conte a história do personagem...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            // Anotações
            OutlinedTextField(
                value = anotacoes,
                onValueChange = { anotacoes = it },
                label = { Text("Anotações") },
                placeholder = { Text("Anotações gerais...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Notificação de salvamento
        AnimatedVisibility(
            visible = showSavedMessage,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    "✓ Salvo automaticamente",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}