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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameModeSelectorScreen(
    currentMode: GameMode?,
    onModeSelected: (GameMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
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
            color = Color(0xFF4CAF50),
            textAlign = TextAlign.Center
        )

        Text(
            "Escolha o Modo de Jogo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            "Selecione qual sistema você quer jogar",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(9.dp))

        // Card Investigação Horror
        GameModeCard(
            title = "👁️ INVESTIGAÇÃO HORROR",
            description = "Sistema de horror cósmico e investigação paranormal",
            features = listOf(
                "Atributos: FOR, AGI, PRE",
                "Perícias especializadas",
                "Sistema de Sanidade",
                "Rolagem de dados múltiplos"
            ),
            gradientColors = listOf(Color(0xFF1A1A1A), Color(0xFF0D4D0D)),
            accentColor = Color(0xFF4CAF50),
            isSelected = currentMode == GameMode.INVESTIGACAO_HORROR,
            onClick = { onModeSelected(GameMode.INVESTIGACAO_HORROR) }
        )

        // Card Velho Oeste
        GameModeCard(
            title = "💀 VELHO OESTE",
            description = "Aventuras no coração do velho oeste americano",
            features = listOf(
                "Atributos: Físico, Velocidade, Intelecto, Coragem, Defesa",
                "Sistema de Dor e Selo da Morte",
                "Antecedentes personalizados",
                "Habilidades e equipamentos"
            ),
            gradientColors = listOf(Color(0xFF1A1A1A), Color(0xFF4D2600)),
            accentColor = Color(0xFFD2691E),
            isSelected = currentMode == GameMode.VELHO_OESTE,
            onClick = { onModeSelected(GameMode.VELHO_OESTE) }
        )

        // Card Assimilação
        GameModeCard(
            title = "🧬 SOBREVIVÊNCIA",
            description = "RPG pós-apocalíptico de sobrevivência e mutação",
            features = listOf(
                "Aptidões: Instintos, Conhecimentos e Práticas",
                "Cabo de Guerra: Determinação vs Assimilação",
                "Sistema de Saúde narrativo em 6 condições",
                "Mutações e Características únicas"
            ),
            gradientColors = listOf(Color(0xFF1A1A1A), Color(0xFF0D3D2E)),
            accentColor = Color(0xFF00BFA5),
            isSelected = currentMode == GameMode.ASSIMILACAO,
            onClick = { onModeSelected(GameMode.ASSIMILACAO) }
        )

        // Card Fantasia
        GameModeCard(
            title = "🐉 FANTASIA",
            description = "Aventuras épicas em um mundo de magia e monstros",
            features = listOf(
                "Atributos: FOR, DES, CON, INT, SAB, CAR",
                "Magias, Habilidades e Inventário detalhado",
                "Sistema de classes e raças",
                "Rolagens baseadas em d20"
            ),
            gradientColors = listOf(Color(0xFF1A1A1A), Color(0xFF4D0000)),
            accentColor = Color(0xFFE53935),
            isSelected = currentMode == GameMode.FANTASIA,
            onClick = { onModeSelected(GameMode.FANTASIA) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Você pode trocar de modo a qualquer momento",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun GameModeCard(
    title: String,
    description: String,
    features: List<String>,
    gradientColors: List<Color>,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor,
                fontSize = 22.sp
            )

            // Descrição
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                lineHeight = 20.sp
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = accentColor.copy(alpha = 0.2f)
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