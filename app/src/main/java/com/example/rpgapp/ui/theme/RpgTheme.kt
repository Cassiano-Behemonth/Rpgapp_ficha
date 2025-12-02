package com.example.rpgapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cores temáticas - Preto e Verde (Matrix/Cyberpunk style)
private val MatrixNeonGreen = Color(0xFF00FF41)
private val MatrixDarkGreen = Color(0xFF00AA2E)
private val MatrixLimeGreen = Color(0xFF39FF14)
private val MatrixForestGreen = Color(0xFF228B22)
private val MatrixDeepBlack = Color(0xFF000000)
private val MatrixCharcoalBlack = Color(0xFF0A0A0A)
private val MatrixDarkGray = Color(0xFF1A1A1A)
private val MatrixMediumGray = Color(0xFF2A2A2A)
private val MatrixLightGray = Color(0xFF4A4A4A)
private val MatrixGhostGreen = Color(0xFFCCFFDD)

private val DarkColorScheme = darkColorScheme(
    primary = MatrixNeonGreen,
    onPrimary = MatrixDeepBlack,
    primaryContainer = MatrixDarkGreen,
    onPrimaryContainer = MatrixGhostGreen,

    secondary = MatrixLimeGreen,
    onSecondary = MatrixDeepBlack,
    secondaryContainer = MatrixForestGreen,
    onSecondaryContainer = MatrixGhostGreen,

    tertiary = Color(0xFF00CED1),
    onTertiary = MatrixDeepBlack,

    background = MatrixDeepBlack,
    onBackground = MatrixNeonGreen,

    surface = MatrixCharcoalBlack,
    onSurface = MatrixNeonGreen,
    surfaceVariant = MatrixDarkGray,
    onSurfaceVariant = Color(0xFF88FF88),

    error = Color(0xFFFF3333),
    onError = Color.White,

    outline = Color(0xFF00AA2E),
    outlineVariant = MatrixMediumGray
)

@Composable
fun RpgTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}