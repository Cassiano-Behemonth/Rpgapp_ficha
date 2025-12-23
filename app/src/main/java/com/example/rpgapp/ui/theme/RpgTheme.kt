package com.example.rpgapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.rpgapp.ui.screens.AppTheme

// Verde e Preto (Simples)
private val GreenBlack_Primary = Color(0xFF4CAF50)
private val GreenBlack_Light = Color(0xFF81C784)
private val GreenBlack_Dark = Color(0xFF388E3C)
private val GreenBlack_Background = Color(0xFF000000)
private val GreenBlack_Surface = Color(0xFF121212)

// Vermelho e Branco
private val RedWhite_Primary = Color(0xFFE53935)
private val RedWhite_Light = Color(0xFFEF5350)
private val RedWhite_Dark = Color(0xFFC62828)
private val RedWhite_Background = Color(0xFFFAFAFA)
private val RedWhite_Surface = Color(0xFFFFFFFF)

// Dourado e Preto
private val GoldBlack_Primary = Color(0xFFFFC107)
private val GoldBlack_Light = Color(0xFFFFD54F)
private val GoldBlack_Dark = Color(0xFFFFA000)
private val GoldBlack_Background = Color(0xFF000000)
private val GoldBlack_Surface = Color(0xFF121212)

// Roxo e Preto
private val PurpleBlack_Primary = Color(0xFF9C27B0)
private val PurpleBlack_Light = Color(0xFFBA68C8)
private val PurpleBlack_Dark = Color(0xFF7B1FA2)
private val PurpleBlack_Background = Color(0xFF000000)
private val PurpleBlack_Surface = Color(0xFF121212)

// Azul e Branco
private val BlueWhite_Primary = Color(0xFF2196F3)
private val BlueWhite_Light = Color(0xFF64B5F6)
private val BlueWhite_Dark = Color(0xFF1976D2)
private val BlueWhite_Background = Color(0xFFFAFAFA)
private val BlueWhite_Surface = Color(0xFFFFFFFF)

// Velho Oeste 🔫
private val WildWest_Primary = Color(0xFFD2691E)         // Laranja queimado
private val WildWest_Light = Color(0xFFDAA520)           // Dourado claro
private val WildWest_Dark = Color(0xFF8B4513)            // Marrom sela
private val WildWest_Background = Color(0xFFFFF8DC)      // Bege claro
private val WildWest_Surface = Color(0xFFFAF0E6)         // Creme

@Composable
fun getColorScheme(theme: AppTheme): ColorScheme {
    return when (theme) {
        AppTheme.GREEN_BLACK -> darkColorScheme(
            primary = GreenBlack_Primary,
            onPrimary = Color.Black,
            primaryContainer = GreenBlack_Dark,
            onPrimaryContainer = GreenBlack_Light,
            secondary = GreenBlack_Light,
            onSecondary = Color.Black,
            background = GreenBlack_Background,
            onBackground = GreenBlack_Light,
            surface = GreenBlack_Surface,
            onSurface = GreenBlack_Primary,
            surfaceVariant = Color(0xFF1E1E1E),
            onSurfaceVariant = GreenBlack_Light,
            error = Color(0xFFEF5350),
            outline = GreenBlack_Dark,
            inverseSurface = GreenBlack_Light,
            inverseOnSurface = Color.Black
        )

        AppTheme.RED_WHITE -> lightColorScheme(
            primary = RedWhite_Primary,
            onPrimary = Color.White,
            primaryContainer = RedWhite_Light,
            onPrimaryContainer = RedWhite_Dark,
            secondary = Color(0xFFD32F2F),
            onSecondary = Color.White,
            background = RedWhite_Background,
            onBackground = Color(0xFF212121),
            surface = RedWhite_Surface,
            onSurface = RedWhite_Primary,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF424242),
            error = Color(0xFFB71C1C),
            outline = RedWhite_Dark,
            inverseSurface = RedWhite_Primary,
            inverseOnSurface = Color.White
        )

        AppTheme.GOLD_BLACK -> darkColorScheme(
            primary = GoldBlack_Primary,
            onPrimary = Color.Black,
            primaryContainer = GoldBlack_Dark,
            onPrimaryContainer = GoldBlack_Light,
            secondary = GoldBlack_Light,
            onSecondary = Color.Black,
            background = GoldBlack_Background,
            onBackground = GoldBlack_Light,
            surface = GoldBlack_Surface,
            onSurface = GoldBlack_Primary,
            surfaceVariant = Color(0xFF1E1E1E),
            onSurfaceVariant = GoldBlack_Light,
            error = Color(0xFFFF6F00),
            outline = GoldBlack_Dark,
            inverseSurface = GoldBlack_Light,
            inverseOnSurface = Color.Black
        )

        AppTheme.PURPLE_GRAY -> darkColorScheme(
            primary = PurpleBlack_Primary,
            onPrimary = Color.White,
            primaryContainer = PurpleBlack_Dark,
            onPrimaryContainer = PurpleBlack_Light,
            secondary = PurpleBlack_Light,
            onSecondary = Color.White,
            background = PurpleBlack_Background,
            onBackground = PurpleBlack_Light,
            surface = PurpleBlack_Surface,
            onSurface = PurpleBlack_Primary,
            surfaceVariant = Color(0xFF1E1E1E),
            onSurfaceVariant = PurpleBlack_Light,
            error = Color(0xFFE040FB),
            outline = PurpleBlack_Dark,
            inverseSurface = PurpleBlack_Light,
            inverseOnSurface = Color.Black
        )

        AppTheme.BLUE_WHITE -> lightColorScheme(
            primary = BlueWhite_Primary,
            onPrimary = Color.White,
            primaryContainer = BlueWhite_Light,
            onPrimaryContainer = BlueWhite_Dark,
            secondary = Color(0xFF1E88E5),
            onSecondary = Color.White,
            background = BlueWhite_Background,
            onBackground = Color(0xFF212121),
            surface = BlueWhite_Surface,
            onSurface = BlueWhite_Primary,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF424242),
            error = Color(0xFFD32F2F),
            outline = BlueWhite_Dark,
            inverseSurface = BlueWhite_Primary,
            inverseOnSurface = Color.White
        )

        AppTheme.WILD_WEST -> lightColorScheme(
            primary = WildWest_Primary,              // Laranja queimado
            onPrimary = Color.White,
            primaryContainer = WildWest_Light,       // Dourado
            onPrimaryContainer = WildWest_Dark,      // Marrom escuro
            secondary = WildWest_Light,              // Dourado
            onSecondary = Color.White,
            background = WildWest_Background,        // Bege claro
            onBackground = Color(0xFF3E2723),        // Marrom escuro
            surface = WildWest_Surface,              // Creme
            onSurface = WildWest_Primary,            // Laranja queimado
            surfaceVariant = Color(0xFFFFE4B5),      // Bege médio
            onSurfaceVariant = Color(0xFF3E2723),    // Marrom escuro
            error = Color(0xFFD32F2F),
            outline = Color(0xFFCD853F),             // Bronze
            inverseSurface = WildWest_Primary,
            inverseOnSurface = Color.White
        )
    }
}

@Composable
fun RpgTheme(
    theme: AppTheme = AppTheme.GREEN_BLACK,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getColorScheme(theme),
        typography = Typography,
        content = content
    )
}