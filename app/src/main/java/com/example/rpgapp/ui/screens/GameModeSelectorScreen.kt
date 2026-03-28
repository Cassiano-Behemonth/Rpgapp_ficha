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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameModeSelectorScreen(
    currentMode: GameMode?,
    onModeSelected: (GameMode) -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "🎲 RPG records ",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            "Escolha o Modo de Jogo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            "Selecione qual sistema você quer jogar",
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(9.dp))

        // Card Investigação Horror
        GameModeCard(
            title = "👁️ INVESTIGAÇÃO HORROR",
            basedOn = "Baseado em: Ordem Paranormal e Call of Cthulhu",
            description = "Sistema de horror cósmico e investigação paranormal",
            features = listOf(
                "Atributos: FOR, AGI, PRE",
                "Perícias especializadas",
                "Sistema de Sanidade",
                "Rolagem de dados múltiplos"
            ),
            accentColor = Color(0xFF4CAF50),
            isSelected = currentMode == GameMode.INVESTIGACAO_HORROR,
            onClick = { onModeSelected(GameMode.INVESTIGACAO_HORROR) }
        )

        // Card Velho Oeste
        GameModeCard(
            title = "💀 VELHO OESTE",
            basedOn = "Baseado em: Sacramento",
            description = "Aventuras no coração do velho oeste americano",
            features = listOf(
                "Atributos: Físico, Velocidade, Intelecto, Coragem, Defesa",
                "Sistema de Dor e Selo da Morte",
                "Antecedentes personalizados",
                "Habilidades e equipamentos"
            ),
            accentColor = Color(0xFFD2691E),
            isSelected = currentMode == GameMode.VELHO_OESTE,
            onClick = { onModeSelected(GameMode.VELHO_OESTE) }
        )

        // Card Assimilação
        GameModeCard(
            title = "🧬 SOBREVIVÊNCIA",
            basedOn = "Baseado em: Assimilação",
            description = "RPG pós-apocalíptico de sobrevivência e mutação",
            features = listOf(
                "Aptidões: Instintos, Conhecimentos e Práticas",
                "Cabo de Guerra: Determinação vs Assimilação",
                "Sistema de Saúde narrativo em 6 condições",
                "Mutações e Características únicas"
            ),
            accentColor = Color(0xFF00BFA5),
            isSelected = currentMode == GameMode.ASSIMILACAO,
            onClick = { onModeSelected(GameMode.ASSIMILACAO) }
        )

        // Card Fantasia
        GameModeCard(
            title = "🐉 FANTASIA",
            basedOn = "Baseado em: Tormenta",
            description = "Aventuras épicas em um mundo de magia e monstros",
            features = listOf(
                "Atributos: FOR, DES, CON, INT, SAB, CAR",
                "Magias, Habilidades e Inventário detalhado",
                "Sistema de classes e raças",
                "Rolagens baseadas em d20"
            ),
            accentColor = Color(0xFFE53935),
            isSelected = currentMode == GameMode.FANTASIA,
            onClick = { onModeSelected(GameMode.FANTASIA) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Você pode trocar de modo a qualquer momento",
            style = MaterialTheme.typography.bodySmall,
            color = onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun GameModeCard(
    title: String,
    basedOn: String? = null,
    description: String,
    features: List<String>,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val surface = MaterialTheme.colorScheme.surface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    // Gradiente que usa as cores do tema — o acento aparece levemente na base do card
    val gradientColors = listOf(
        surface,
        accentColor.copy(alpha = 0.08f).compositeOver(surface)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 3.dp,
                        color = accentColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(colors = gradientColors)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor,
                    fontSize = 22.sp
                )
                if (basedOn != null) {
                    Text(
                        basedOn,
                        style = MaterialTheme.typography.labelSmall,
                        color = onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Descrição
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = onSurface,
                lineHeight = 20.sp
            )

            // Lista de features
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                features.forEach { feature ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            "•",
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            feature,
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceVariant
                        )
                    }
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = accentColor.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "✓ MODO SELECIONADO",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }
            }
        }
    }
}