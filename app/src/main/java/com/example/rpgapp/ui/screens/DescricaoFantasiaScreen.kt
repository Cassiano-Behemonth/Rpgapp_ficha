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
import com.example.rpgapp.viewmodel.FichaFantasiaViewModel
import kotlinx.coroutines.delay

@Composable
fun DescricaoFantasiaScreen(
    onBack: () -> Unit,
    viewModel: FichaFantasiaViewModel = viewModel()
) {
    val ficha by viewModel.ficha.collectAsState()

    var nome by remember { mutableStateOf("") }
    var jogador by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var origem by remember { mutableStateOf("") }
    var divindade by remember { mutableStateOf("") }
    var classes by remember { mutableStateOf("") }
    var aparencia by remember { mutableStateOf("") }
    var personalidade by remember { mutableStateOf("") }
    var historia by remember { mutableStateOf("") }
    var anotacoes by remember { mutableStateOf("") }
    var showSaveConfirmation by remember { mutableStateOf(false) }

    // Carrega dados quando ficha está disponível
    LaunchedEffect(ficha) {
        ficha?.let {
            nome = it.nome
            jogador = it.jogador
            raca = it.raca
            origem = it.origem
            divindade = it.divindade
            classes = it.classes
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
                containerColor = MaterialTheme.colorScheme.surface
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
                        value = raca,
                        onValueChange = { raca = it },
                        label = { Text("Raça") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ex: Humano") }
                    )
                    OutlinedTextField(
                        value = origem,
                        onValueChange = { origem = it },
                        label = { Text("Origem") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ex: Nobre") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = classes,
                    onValueChange = { classes = it },
                    label = { Text("Classes e Níveis") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ex: Guerreiro 5, Paladino 3") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = divindade,
                    onValueChange = { divindade = it },
                    label = { Text("Divindade") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ex: Khalmyr, Allihanna...") }
                )
            }
        }

        // Aparência
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
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
                    placeholder = { Text("Descreva a aparência física do personagem: altura, peso, cor de cabelo, olhos, roupas típicas, marcas distintivas...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    maxLines = 6
                )
            }
        }

        // Personalidade
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
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
                    placeholder = { Text("Descreva a personalidade: temperamento, valores, medos, motivações, objetivos, maneirismos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    maxLines = 6
                )
            }
        }

        // História
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
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
                    placeholder = { Text("Conte o passado do personagem: origem, família, eventos marcantes, como se tornou aventureiro...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    maxLines = 8
                )
            }
        }

        // Anotações
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
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
                    placeholder = { Text("Anotações gerais: objetivos atuais, contatos importantes, tesouros, plots em andamento...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    maxLines = 8
                )
            }
        }

        // Botão salvar
        Button(
            onClick = {
                viewModel.salvarDescricao(
                    nome, jogador, raca, origem, divindade, classes,
                    aparencia, personalidade, historia, anotacoes
                )
                showSaveConfirmation = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("💾 SALVAR DESCRIÇÃO", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Confirmação de salvamento
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
            Snackbar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("✓ Descrição salva com sucesso!")
            }
        }
    }
}
