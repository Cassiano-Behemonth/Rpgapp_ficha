package com.example.rpgapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de cores melhorada - Preto com Verde suave
private val NeonGreen = Color(0xFF00E676)           // Verde neon suave
private val LightGreen = Color(0xFF69F0AE)          // Verde claro para destaques
private val DarkGreen = Color(0xFF00C853)           // Verde escuro para containers
private val MintGreen = Color(0xFF1DE9B6)           // Verde menta para secundário
private val ForestGreen = Color(0xFF00BFA5)         // Verde floresta

// Tons de preto e cinza - Lovecraftiano
private val AbyssBlack = Color(0xFF000000)          // Preto abissal
private val VoidBlack = Color(0xFF050505)           // Preto do vazio
private val DeepDarkGray = Color(0xFF0A0A0A)        // Cinza profundo
private val ShadowGray = Color(0xFF121212)          // Cinza sombrio
private val DarkGray = Color(0xFF1C1C1C)            // Cinza escuro
private val LightGray = Color(0xFF2A2A2A)           // Cinza claro

// Cores de suporte
private val SoftWhite = Color(0xFFE0FFE0)           // Branco esverdeado suave
private val WarningRed = Color(0xFFFF5252)          // Vermelho para erros

private val DarkColorScheme = darkColorScheme(
    // Cores primárias
    primary = NeonGreen,
    onPrimary = AbyssBlack,
    primaryContainer = DarkGreen,
    onPrimaryContainer = LightGreen,

    // Cores secundárias
    secondary = MintGreen,
    onSecondary = AbyssBlack,
    secondaryContainer = ForestGreen,
    onSecondaryContainer = SoftWhite,

    // Cores terciárias
    tertiary = Color(0xFF26C6DA),                    // Ciano
    onTertiary = AbyssBlack,

    // Backgrounds
    background = AbyssBlack,
    onBackground = LightGreen,

    // Superfícies
    surface = VoidBlack,
    onSurface = NeonGreen,
    surfaceVariant = DeepDarkGray,
    onSurfaceVariant = LightGreen,

    // Erros
    error = WarningRed,
    onError = Color.White,

    // Bordas e outlines
    outline = DarkGreen,
    outlineVariant = ShadowGray,

    // Cores adicionais
    inverseSurface = LightGreen,
    inverseOnSurface = AbyssBlack,
    inversePrimary = DarkGreen,

    surfaceTint = NeonGreen,

    scrim = Color(0xDD000000)                        // Overlay mais escuro
)

@Composable
fun RpgTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}