package com.example.rpgapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiceButtonRow(
    onRoll: (Int) -> Unit
) {
    val diceList = listOf(6, 8, 10, 12, 20)

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        diceList.forEach { faces ->
            Button(onClick = { onRoll(faces) }) {
                Text(text = "d$faces")
            }
        }
    }
}
