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
import com.example.rpgapp.ui.theme.RpgTheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que o conteúdo vá até as bordas
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
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

            // Atualiza a cor da barra de status baseada no tema
            LaunchedEffect(currentTheme) {
                val statusBarColor = when (currentTheme) {
                    AppTheme.GREEN_BLACK -> Color.Black
                    AppTheme.RED_WHITE -> Color(0xFFFAFAFA)
                    AppTheme.GOLD_BLACK -> Color.Black
                    AppTheme.PURPLE_GRAY -> Color.Black
                    AppTheme.BLUE_WHITE -> Color(0xFFFAFAFA)
                }

                window.statusBarColor = statusBarColor.toArgb()

                // Define se os ícones da status bar devem ser escuros ou claros
                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                insetsController.isAppearanceLightStatusBars = when (currentTheme) {
                    AppTheme.RED_WHITE, AppTheme.BLUE_WHITE -> true  // Ícones escuros para temas claros
                    else -> false  // Ícones claros para temas escuros
                }
            }

            RpgTheme(theme = currentTheme) {
                AppNavigation(
                    currentTheme = currentTheme,
                    onThemeChange = { theme ->
                        currentTheme = theme
                        sharedPrefs.edit().putString("selected_theme", theme.name).apply()
                    }
                )
            }
        }
    }
}