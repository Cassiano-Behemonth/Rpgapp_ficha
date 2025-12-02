package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DescricaoScreen(onBack: () -> Unit) {
    var anotacao by remember { mutableStateOf("") }
    var aparencia by remember { mutableStateOf("") }
    var personalidade by remember { mutableStateOf("") }
    var historia by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {

        Text("Descrição do Personagem", style = MaterialTheme.typography.headlineMedium)

        TextField(value = anotacao, onValueChange = { anotacao = it }, label = { Text("Anotações") }, modifier = Modifier.fillMaxWidth())
        TextField(value = aparencia, onValueChange = { aparencia = it }, label = { Text("Aparência") }, modifier = Modifier.fillMaxWidth())
        TextField(value = personalidade, onValueChange = { personalidade = it }, label = { Text("Personalidade") }, modifier = Modifier.fillMaxWidth())
        TextField(value = historia, onValueChange = { historia = it }, label = { Text("História") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
