package com.example.rpgapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel
import kotlinx.coroutines.delay

@Composable
fun DescricaoAssimilacaoScreen(
    viewModel: FichaAssimilacaoViewModel
) {
    val ficha by viewModel.ficha.collectAsState()

    // ── Estado local ─────────────────────────────────────────
    var nome              by remember { mutableStateOf("") }
    var jogador           by remember { mutableStateOf("") }
    var eventoMarcante    by remember { mutableStateOf("") }
    var ocupacao          by remember { mutableStateOf("") }
    var proposito1        by remember { mutableStateOf("") }
    var proposito2        by remember { mutableStateOf("") }
    var propositoColetivo by remember { mutableStateOf("") }
    var aparencia         by remember { mutableStateOf("") }
    var personalidade     by remember { mutableStateOf("") }
    var historia          by remember { mutableStateOf("") }
    var anotacoes         by remember { mutableStateOf("") }
    var showSavedMessage  by remember { mutableStateOf(false) }

    LaunchedEffect(ficha) {
        ficha?.let {
            nome              = it.nome
            jogador           = it.jogador
            eventoMarcante    = it.eventoMarcante
            ocupacao          = it.ocupacao
            proposito1        = it.proposito1
            proposito2        = it.proposito2
            propositoColetivo = it.propositoColetivo
            aparencia         = it.aparencia
            personalidade     = it.personalidade
            historia          = it.historia
            anotacoes         = it.anotacoes
        }
    }

    // Salvamento automático
    LaunchedEffect(
        nome, jogador, eventoMarcante, ocupacao,
        proposito1, proposito2, propositoColetivo,
        aparencia, personalidade, historia, anotacoes
    ) {
        delay(1000)
        viewModel.salvarDescricao(
            nome, jogador, eventoMarcante, ocupacao,
            proposito1, proposito2, propositoColetivo,
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

            // ── Identidade ───────────────────────────────────
            DescricaoSection(titulo = "IDENTIDADE", emoji = "👤") {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = jogador,
                    onValueChange = { jogador = it },
                    label = { Text("Jogador") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // ── Origens ──────────────────────────────────────
            DescricaoSection(titulo = "ORIGENS", emoji = "📖") {
                Text(
                    "As origens definem quem seu Infectado era antes do Colapso.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Evento Marcante",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Trauma ou experiência marcante do passado",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = eventoMarcante,
                    onValueChange = { eventoMarcante = it },
                    placeholder = { Text("O que marcou seu personagem para sempre?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Ocupação",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Atividade principal antes ou durante o Colapso",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = ocupacao,
                    onValueChange = { ocupacao = it },
                    placeholder = { Text("Médico, engenheiro, soldado, comerciante...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }

            // ── Propósitos ───────────────────────────────────
            DescricaoSection(titulo = "PROPÓSITOS", emoji = "🎯") {
                Text(
                    "Os propósitos motivam seu Infectado e concedem Clareza quando realizados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Propósito Pessoal 1",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = proposito1,
                    onValueChange = { proposito1 = it },
                    placeholder = { Text("Motivação íntima e secreta...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Propósito Pessoal 2",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = proposito2,
                    onValueChange = { proposito2 = it },
                    placeholder = { Text("Outra motivação íntima...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Propósito Coletivo",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Compartilhado com pelo menos 2 outros Infectados",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = propositoColetivo,
                    onValueChange = { propositoColetivo = it },
                    placeholder = { Text("O que une o grupo?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }

            // ── Aparência ─────────────────────────────────────
            DescricaoSection(titulo = "APARÊNCIA", emoji = "🪞") {
                OutlinedTextField(
                    value = aparencia,
                    onValueChange = { aparencia = it },
                    placeholder = { Text("Descreva a aparência física do personagem...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            // ── Personalidade ────────────────────────────────
            DescricaoSection(titulo = "PERSONALIDADE", emoji = "🧠") {
                OutlinedTextField(
                    value = personalidade,
                    onValueChange = { personalidade = it },
                    placeholder = { Text("Como o personagem age, pensa e se comporta?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            // ── História ──────────────────────────────────────
            DescricaoSection(titulo = "HISTÓRIA", emoji = "📜") {
                OutlinedTextField(
                    value = historia,
                    onValueChange = { historia = it },
                    placeholder = { Text("Conte o passado do personagem...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }

            // ── Anotações ─────────────────────────────────────
            DescricaoSection(titulo = "ANOTAÇÕES", emoji = "📝") {
                OutlinedTextField(
                    value = anotacoes,
                    onValueChange = { anotacoes = it },
                    placeholder = { Text("Anotações gerais, contatos, objetivos...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ── Notificação de salvamento ─────────────────────────
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

// ─────────────────────────────────────────────────────────────
// SEÇÃO DE DESCRIÇÃO
// ─────────────────────────────────────────────────────────────
@Composable
fun DescricaoSection(
    titulo: String,
    emoji: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(emoji, fontSize = 16.sp)
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}