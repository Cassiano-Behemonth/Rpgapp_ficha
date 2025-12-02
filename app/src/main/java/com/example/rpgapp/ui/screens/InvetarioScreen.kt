package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InventarioScreen(onBack: () -> Unit) {
    var texto by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {

        Text("Inventário", style = MaterialTheme.typography.headlineMedium)

        TextField(
            value = texto,
            onValueChange = { texto = it },
            modifier = Modifier.fillMaxWidth().height(300.dp),
            label = { Text("Itens, armas, equipamentos etc.") }
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
