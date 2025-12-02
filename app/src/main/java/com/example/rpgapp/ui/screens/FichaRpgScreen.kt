package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun FichaRpgScreen(
    onSalvar: () -> Unit,
    onInventario: () -> Unit,
    onDescricao: () -> Unit,
    onPericias: () -> Unit
) {
    val attrs = listOf(
        "FOR" to remember { mutableStateOf("") },
        "AGI" to remember { mutableStateOf("") },
        "PRE" to remember { mutableStateOf("") },
        "INT" to remember { mutableStateOf("") },
        "VIG" to remember { mutableStateOf("") },
    )

    var nex by remember { mutableStateOf("") }
    var vida by remember { mutableStateOf("") }
    var sanidade by remember { mutableStateOf("") }

    var dadoCustom by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("Ficha de Personagem", style = MaterialTheme.typography.headlineMedium)

        attrs.forEach { (nome, valor) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(nome, modifier = Modifier.weight(0.3f))

                TextField(
                    value = valor.value,
                    onValueChange = { valor.value = it },
                    modifier = Modifier.weight(0.7f),
                    singleLine = true
                )
            }
        }

        TextField(
            value = nex,
            onValueChange = { nex = it },
            label = { Text("NEX (Exposição)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = vida,
            onValueChange = { vida = it },
            label = { Text("Vida") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = sanidade,
            onValueChange = { sanidade = it },
            label = { Text("Sanidade") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Rolagem de Dados", style = MaterialTheme.typography.titleLarge)

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("d6", "d8", "d10", "d12", "d20").forEach { rotulo ->
                Button(
                    onClick = { resultado = rolarDadoSimples(rotulo) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(rotulo.uppercase())
                }
            }
        }

        TextField(
            value = dadoCustom,
            onValueChange = { dadoCustom = it },
            label = { Text("Ex: 1d8+2") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { resultado = rolarCustom(dadoCustom) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Rolar personalizado")
        }

        resultado?.let {
            Text("Resultado: $it", style = MaterialTheme.typography.headlineSmall)
        }

        Text("Outros menus", style = MaterialTheme.typography.titleLarge)

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Button(onClick = onPericias, modifier = Modifier.fillMaxWidth()) {
                Text("Perícias")
            }

            Button(onClick = onInventario, modifier = Modifier.fillMaxWidth()) {
                Text("Inventário")
            }

            Button(onClick = onDescricao, modifier = Modifier.fillMaxWidth()) {
                Text("Descrição / Anotações")
            }

            Button(
                onClick = onSalvar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Ficha")
            }
        }
    }
}

fun rolarDadoSimples(tipo: String): Int {
    val faces = tipo.removePrefix("d").toInt()
    return (1..faces).random()
}

fun rolarCustom(expr: String): Int {
    val regex = Regex("(\\d+)d(\\d+)([+-]\\d+)?")
    val match = regex.find(expr.lowercase()) ?: return 0

    val qtd = match.groupValues[1].toInt()
    val faces = match.groupValues[2].toInt()
    val mod = match.groupValues[3].ifEmpty { "+0" }.toInt()

    var total = 0
    repeat(qtd) { total += (1..faces).random() }

    return total + mod
}
