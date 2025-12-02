package com.example.rpgapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cores temáticas de RPG - Paranormal/Horror
private val RpgBloodRed = Color(0xFFB91C1C)
private val RpgDarkPurple = Color(0xFF5B21B6)
private val RpgDeepBlack = Color(0xFF0F0F0F)
private val CharcoalGray = Color(0xFF1F1F1F)
private val RpgMistGray = Color(0xFF9CA3AF)
private val RpgLightPurple = Color(0xFF9333EA)
private val DarkRed = Color(0xFF7F1D1D)
private val GhostWhite = Color(0xFFF3F4F6)

private val DarkColorScheme = darkColorScheme(
    primary = RpgLightPurple,
    onPrimary = Color.White,
    primaryContainer = RpgDarkPurple,
    onPrimaryContainer = GhostWhite,

    secondary = RpgBloodRed,
    onSecondary = Color.White,
    secondaryContainer = DarkRed,
    onSecondaryContainer = GhostWhite,

    tertiary = Color(0xFF6366F1),
    onTertiary = Color.White,

    background = RpgDeepBlack,
    onBackground = GhostWhite,

    surface = CharcoalGray,
    onSurface = GhostWhite,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = RpgMistGray,

    error = RpgBloodRed,
    onError = Color.White,

    outline = Color(0xFF404040),
    outlineVariant = Color(0xFF2A2A2A)
)

@Composable
fun RpgTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}