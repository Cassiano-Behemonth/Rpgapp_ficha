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
private val Fantasia_Primary = Color(0xFFE53935)
private val Fantasia_Light = Color(0xFFEF5350)
private val Fantasia_Dark = Color(0xFFC62828)
private val Fantasia_Background = Color(0xFFFAFAFA)
private val Fantasia_Surface = Color(0xFFFFFFFF)

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
private val WildWest_Primary = Color(0xFFD2691E)
private val WildWest_Light = Color(0xFFDAA520)
private val WildWest_Dark = Color(0xFF8B4513)
private val WildWest_Background = Color(0xFFFFF8DC)
private val WildWest_Surface = Color(0xFFFAF0E6)

// 🧬 Assimilação — orgânico, sujo, perturbador
// Verde musgo podre  #4A6741 / #6B8F5E
// Vermelho coagulado #8B1A1A
// Azul veia profundo #1A3A5C / #2E5F8A
private val Assim_Green      = Color(0xFF4A6741)   // Verde musgo escuro
private val Assim_GreenLight = Color(0xFF6B8F5E)   // Verde musgo médio
private val Assim_GreenDark  = Color(0xFF2D4228)   // Verde floresta profundo
private val Assim_Red        = Color(0xFF8B1A1A)   // Vermelho sangue coagulado
private val Assim_Blue       = Color(0xFF1A3A5C)   // Azul veia profundo
private val Assim_BlueGlow   = Color(0xFF2E5F8A)   // Azul bioluminescente
private val Assim_Background = Color(0xFF080A08)   // Quase preto orgânico
private val Assim_Surface    = Color(0xFF0E130E)   // Superfície verde escuríssima
private val Assim_SurfaceVar = Color(0xFF131A13)   // Cards — floresta noturna

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
            surfaceContainer = GreenBlack_Surface,
            error = Color(0xFFEF5350),
            outline = GreenBlack_Dark,
            inverseSurface = GreenBlack_Light,
            inverseOnSurface = Color.Black
        )

        AppTheme.FANTASIA -> lightColorScheme(
            primary = Fantasia_Primary,
            onPrimary = Color.White,
            primaryContainer = Fantasia_Light,
            onPrimaryContainer = Fantasia_Dark,
            secondary = Color(0xFFD32F2F),
            onSecondary = Color.White,
            background = Fantasia_Background,
            onBackground = Color(0xFF212121),
            surface = Fantasia_Surface,
            onSurface = Fantasia_Primary,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF424242),
            surfaceContainer = Fantasia_Surface,
            error = Color(0xFFB71C1C),
            outline = Fantasia_Dark,
            inverseSurface = Fantasia_Primary,
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
            surfaceContainer = GoldBlack_Surface,
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
            surfaceContainer = PurpleBlack_Surface,
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
            surfaceContainer = BlueWhite_Surface,
            error = Color(0xFFD32F2F),
            outline = BlueWhite_Dark,
            inverseSurface = BlueWhite_Primary,
            inverseOnSurface = Color.White
        )

        AppTheme.WILD_WEST -> lightColorScheme(
            primary = WildWest_Primary,
            onPrimary = Color.White,
            primaryContainer = WildWest_Light,
            onPrimaryContainer = WildWest_Dark,
            secondary = WildWest_Light,
            onSecondary = Color.White,
            background = WildWest_Background,
            onBackground = Color(0xFF3E2723),
            surface = WildWest_Surface,
            onSurface = WildWest_Primary,
            surfaceVariant = Color(0xFFFFE4B5),
            onSurfaceVariant = Color(0xFF3E2723),
            surfaceContainer = Color(0xFFFFE4B5),
            error = Color(0xFFD32F2F),
            outline = Color(0xFFCD853F),
            inverseSurface = WildWest_Primary,
            inverseOnSurface = Color.White
        )

        AppTheme.ASSIMILACAO -> darkColorScheme(
            // ── Primária: Verde musgo ───────────────────────
            primary             = Assim_GreenLight,    // #6B8F5E — verde musgo médio
            onPrimary           = Color(0xFF080A08),
            primaryContainer    = Assim_GreenDark,     // #2D4228 — floresta profunda
            onPrimaryContainer  = Assim_GreenLight,

            // ── Secundária: Azul veia ───────────────────────
            secondary           = Assim_BlueGlow,      // #2E5F8A — azul bioluminescente
            onSecondary         = Color.White,
            secondaryContainer  = Assim_Blue,          // #1A3A5C — azul veia profundo
            onSecondaryContainer = Color(0xFF8FBFE0),

            // ── Fundo e superfícies ─────────────────────────
            background          = Assim_Background,    // #080A08 — quase preto orgânico
            onBackground        = Assim_GreenLight,

            surface             = Assim_Surface,       // #0E130E
            onSurface           = Assim_GreenLight,

            surfaceVariant      = Assim_SurfaceVar,    // #131A13 — cards
            onSurfaceVariant    = Color(0xFF7A9E72),   // verde acinzentado p/ textos sec.

            surfaceContainer    = Assim_Surface,       // tabs

            // ── Erros: Vermelho coagulado ───────────────────
            error               = Assim_Red,           // #8B1A1A
            onError             = Color(0xFFFFCDD2),
            errorContainer      = Color(0xFF4A0A0A),
            onErrorContainer    = Color(0xFFFFABAB),

            // ── Bordas ──────────────────────────────────────
            outline             = Assim_Green,         // #4A6741
            outlineVariant      = Color(0xFF1E2C1E),

            inverseSurface      = Assim_GreenLight,
            inverseOnSurface    = Color(0xFF080A08)
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