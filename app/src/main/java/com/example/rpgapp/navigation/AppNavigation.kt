package com.example.rpgapp.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.rpgapp.ui.screens.*
import com.example.rpgapp.ui.screens.FichaVelhoOesteScreen
import com.example.rpgapp.ui.screens.FichaAssimilacaoScreen
import com.example.rpgapp.viewmodel.FichaViewModel
import com.example.rpgapp.viewmodel.FichaVelhoOesteViewModel
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel

@Composable
fun AppNavigation(
    currentTheme: AppTheme,
    currentMode: GameMode?,
    onThemeChange: (AppTheme) -> Unit,
    onModeChange: (GameMode) -> Unit
) {
    val nav = rememberNavController()
    val context = LocalContext.current

    // ViewModels para todos os modos
    val viewModelHorror: FichaViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    val viewModelOeste: FichaVelhoOesteViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    val viewModelAssimilacao: FichaAssimilacaoViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    // Define a rota inicial baseada no modo selecionado (só na primeira composição)
    val startDestination = remember {
        when (currentMode) {
            GameMode.INVESTIGACAO_HORROR -> "ficha_horror"
            GameMode.VELHO_OESTE -> "ficha_oeste"
            GameMode.ASSIMILACAO -> "ficha_assimilacao"
            null -> "theme_selector"
        }
    }

    NavHost(navController = nav, startDestination = startDestination) {

        // ── Tela de seleção de tema ──────────────────────────
        composable("theme_selector") {
            ThemeSelectorScreen(
                currentTheme = currentTheme,
                currentMode = currentMode,
                onThemeSelected = { theme ->
                    onThemeChange(theme)
                    when (currentMode) {
                        GameMode.INVESTIGACAO_HORROR -> {
                            nav.navigate("ficha_horror") {
                                popUpTo("theme_selector") { inclusive = true }
                            }
                        }
                        GameMode.VELHO_OESTE -> {
                            nav.navigate("ficha_oeste") {
                                popUpTo("theme_selector") { inclusive = true }
                            }
                        }
                        GameMode.ASSIMILACAO -> {
                            nav.navigate("ficha_assimilacao") {
                                popUpTo("theme_selector") { inclusive = true }
                            }
                        }
                        null -> {
                            nav.navigate("game_mode_selector") {
                                popUpTo("theme_selector") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // ── Tela de seleção de modo de jogo ──────────────────
        composable("game_mode_selector") {
            GameModeSelectorScreen(
                currentMode = currentMode,
                onModeSelected = { mode ->
                    onModeChange(mode)
                    when (mode) {
                        GameMode.INVESTIGACAO_HORROR -> {
                            nav.navigate("ficha_horror") {
                                popUpTo("game_mode_selector") { inclusive = true }
                            }
                        }
                        GameMode.VELHO_OESTE -> {
                            nav.navigate("ficha_oeste") {
                                popUpTo("game_mode_selector") { inclusive = true }
                            }
                        }
                        GameMode.ASSIMILACAO -> {
                            nav.navigate("ficha_assimilacao") {
                                popUpTo("game_mode_selector") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // ── Ficha Investigação Horror ────────────────────────
        composable("ficha_horror") {
            FichaRpgScreen(
                onSalvar = {},
                onInventario = {},
                onDescricao = {},
                onPericias = {},
                viewModel = viewModelHorror,
                onThemeChange = { nav.navigate("theme_selector") },
                onModeChange = {
                    nav.navigate("game_mode_selector") {
                        popUpTo("ficha_horror") { inclusive = true }
                    }
                }
            )
        }

        // ── Ficha Velho Oeste ────────────────────────────────
        composable("ficha_oeste") {
            FichaVelhoOesteScreen(
                viewModel = viewModelOeste,
                onThemeChange = { nav.navigate("theme_selector") },
                onModeChange = {
                    nav.navigate("game_mode_selector") {
                        popUpTo("ficha_oeste") { inclusive = true }
                    }
                }
            )
        }

        // ── Ficha Assimilação ────────────────────────────────
        composable("ficha_assimilacao") {
            FichaAssimilacaoScreen(
                viewModel = viewModelAssimilacao,
                onThemeChange = { nav.navigate("theme_selector") },
                onModeChange = {
                    nav.navigate("game_mode_selector") {
                        popUpTo("ficha_assimilacao") { inclusive = true }
                    }
                }
            )
        }
    }
}