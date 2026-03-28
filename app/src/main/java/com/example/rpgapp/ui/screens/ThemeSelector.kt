package com.example.rpgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppTheme {
    GREEN_BLACK,
    FANTASIA,
    GOLD_BLACK,
    PURPLE_GRAY,
    BLUE_WHITE,
    WILD_WEST,
    ASSIMILACAO,
    BLACK_RED
}

@Composable
fun ThemeSelectorScreen(
    currentTheme: AppTheme,
    currentMode: GameMode? = null,
    onThemeSelected: (AppTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Escolha o Tema",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Selecione as cores do aplicativo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeCard(
            title = "Verde e Preto",
            subtitle = "Tema padrão",
            primaryColor = Color(0xFF4CAF50),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.GREEN_BLACK,
            onClick = { onThemeSelected(AppTheme.GREEN_BLACK) }
        )

        ThemeCard(
            title = "🐉 Fantasia",
            subtitle = "Aventuras épicas",
            primaryColor = Color(0xFFE53935),
            backgroundColor = Color.White,
            isSelected = currentTheme == AppTheme.FANTASIA,
            onClick = { onThemeSelected(AppTheme.FANTASIA) }
        )

        ThemeCard(
            title = "Dourado e Preto",
            subtitle = "Elegante",
            primaryColor = Color(0xFFFFC107),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.GOLD_BLACK,
            onClick = { onThemeSelected(AppTheme.GOLD_BLACK) }
        )

        ThemeCard(
            title = "Roxo e Preto",
            subtitle = "Misterioso",
            primaryColor = Color(0xFF9C27B0),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.PURPLE_GRAY,
            onClick = { onThemeSelected(AppTheme.PURPLE_GRAY) }
        )

        ThemeCard(
            title = "Azul e Branco",
            subtitle = "Limpo e claro",
            primaryColor = Color(0xFF2196F3),
            backgroundColor = Color.White,
            isSelected = currentTheme == AppTheme.BLUE_WHITE,
            onClick = { onThemeSelected(AppTheme.BLUE_WHITE) }
        )

        ThemeCard(
            title = "🔫 Velho Oeste",
            subtitle = "Laranja queimado e dourado",
            primaryColor = Color(0xFFD2691E),
            backgroundColor = Color(0xFFFFF8DC),
            isSelected = currentTheme == AppTheme.WILD_WEST,
            onClick = { onThemeSelected(AppTheme.WILD_WEST) }
        )

        ThemeCard(
            title = "🧬 Assimilação",
            subtitle = "Verde musgo • Azul veia • Vermelho coagulado",
            primaryColor = Color(0xFF6B8F5E),
            backgroundColor = Color(0xFF080A08),
            isSelected = currentTheme == AppTheme.ASSIMILACAO,
            onClick = { onThemeSelected(AppTheme.ASSIMILACAO) }
        )

        ThemeCard(
            title = "🔴 Preto e Vermelho",
            subtitle = "Sombrio e intenso",
            primaryColor = Color(0xFFD32F2F),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.BLACK_RED,
            onClick = { onThemeSelected(AppTheme.BLACK_RED) }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ThemeCard(
    title: String,
    subtitle: String,
    primaryColor: Color,
    backgroundColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = primaryColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isSelected) {
                    Text(
                        "✓ Selecionado",
                        style = MaterialTheme.typography.labelSmall,
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .border(
                        width = 2.dp,
                        color = primaryColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "A",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            }
        }
    }
}