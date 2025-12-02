package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PericiasScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.padding(20.dp)) {

        Text("Perícias", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        val lista = listOf(
            "Acrobacia", "Adestramento", "Artes", "Atletismo", "Ciências",
            "Crime", "Diplomacia", "Enganação", "Fortitude", "Furtividade",
            "Iniciativa", "Intimidação", "Intuição", "Investigação", "Luta",
            "Medicina", "Ocultismo", "Percepção", "Pilotagem", "Pontaria",
            "Profissão", "Reflexos", "Religião", "Sobrevivência", "Tática",
            "Tecnologia", "Vontade"
        )

        lista.forEach {
            Text(it, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
