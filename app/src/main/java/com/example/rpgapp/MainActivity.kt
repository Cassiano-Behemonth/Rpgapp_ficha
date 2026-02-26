package com.example.rpgapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.rpgapp.navigation.AppNavigation
import com.example.rpgapp.ui.screens.AppTheme
import com.example.rpgapp.ui.screens.GameMode
import com.example.rpgapp.ui.theme.RpgTheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge nativo — substitui setDecorFitsSystemWindows
        enableEdgeToEdge()

        setContent {
            val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            // Gerencia o tema
            var currentTheme by remember {
                mutableStateOf(
                    try {
                        AppTheme.valueOf(
                            sharedPrefs.getString("selected_theme", AppTheme.GREEN_BLACK.name)
                                ?: AppTheme.GREEN_BLACK.name
                        )
                    } catch (e: Exception) {
                        AppTheme.GREEN_BLACK
                    }
                )
            }

            // Gerencia o modo de jogo
            var currentMode by remember {
                mutableStateOf(
                    try {
                        val modeName = sharedPrefs.getString("selected_mode", null)
                        modeName?.let { GameMode.valueOf(it) }
                    } catch (e: Exception) {
                        null
                    }
                )
            }

            // Atualiza as barras de sistema baseado no tema
            LaunchedEffect(currentTheme) {
                val statusBarColor = when (currentTheme) {
                    AppTheme.GREEN_BLACK  -> Color.Black
                    AppTheme.FANTASIA   -> Color(0xFFFAFAFA)
                    AppTheme.GOLD_BLACK   -> Color.Black
                    AppTheme.PURPLE_GRAY  -> Color.Black
                    AppTheme.BLUE_WHITE   -> Color(0xFFFAFAFA)
                    AppTheme.WILD_WEST    -> Color(0xFFFFF8DC)
                    AppTheme.ASSIMILACAO  -> Color(0xFF080C08)
                }

                val lightBars = when (currentTheme) {
                    AppTheme.FANTASIA, AppTheme.BLUE_WHITE, AppTheme.WILD_WEST -> true
                    else -> false
                }

                // Status bar
                window.statusBarColor = statusBarColor.toArgb()
                // Navigation bar (barra de baixo) — mesma cor do tema
                window.navigationBarColor = statusBarColor.toArgb()

                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                // Ícones da status bar
                insetsController.isAppearanceLightStatusBars = lightBars
                // Ícones da navigation bar
                insetsController.isAppearanceLightNavigationBars = lightBars
            }

            RpgTheme(theme = currentTheme) {
                AppNavigation(
                    currentTheme = currentTheme,
                    currentMode = currentMode,
                    onThemeChange = { theme ->
                        currentTheme = theme
                        sharedPrefs.edit().putString("selected_theme", theme.name).apply()
                    },
                    onModeChange = { mode ->
                        currentMode = mode
                        sharedPrefs.edit().putString("selected_mode", mode.name).apply()
                    }
                )
            }
        }
    }
}