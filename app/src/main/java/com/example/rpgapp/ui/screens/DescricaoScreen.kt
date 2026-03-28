package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpgapp.viewmodel.FichaViewModel
import kotlinx.coroutines.delay

@Composable
fun DescricaoScreen(
    onBack: () -> Unit,
    viewModel: FichaViewModel = viewModel()
) {
    val ficha by viewModel.ficha.collectAsState()

    var nome by remember { mutableStateOf("") }
    var jogador by remember { mutableStateOf("") }
    var origem by remember { mutableStateOf("") }
    var classe by remember { mutableStateOf("") }
    var trilha by remember { mutableStateOf("") }
    var patente by remember { mutableStateOf("") }
    var aparencia by remember { mutableStateOf("") }
    var personalidade by remember { mutableStateOf("") }
    var historia by remember { mutableStateOf("") }
    var anotacoes by remember { mutableStateOf("") }


    // Carrega dados quando ficha está disponível
    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            jogador = it.jogador
            origem = it.origem
            classe = it.classe
            trilha = it.trilha
            patente = it.patente
            aparencia = it.aparencia
            personalidade = it.personalidade
            historia = it.historia
            anotacoes = it.anotacoes
        }
    }

    // Salva automaticamente após 1 segundo se houver mudança
    LaunchedEffect(nome, jogador, origem, classe, trilha, patente, aparencia, personalidade, historia, anotacoes) {
        kotlinx.coroutines.delay(1000)
        viewModel.salvarDescricao(
            nome, jogador, origem, classe, trilha, patente,
            aparencia, personalidade, historia, anotacoes
        )
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
                    label = { Text("Nome do Personagem") },
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
                        value = origem,
                        onValueChange = { origem = it },
                        label = { Text("Origem") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = classe,
                        onValueChange = { classe = it },
                        label = { Text("Classe") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = trilha,
                        onValueChange = { trilha = it },
                        label = { Text("Trilha") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = patente,
                        onValueChange = { patente = it },
                        label = { Text("Patente") },
                        modifier = Modifier.weight(1f)
                    )
                }
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
                    placeholder = { Text("Descreva a aparência física do personagem...") },
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
                    placeholder = { Text("Descreva a personalidade e comportamento...") },
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
                    placeholder = { Text("Conte o passado do personagem...") },
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
                    placeholder = { Text("Anotações gerais, objetivos, contatos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 7
                )
            }
        }
    }
}