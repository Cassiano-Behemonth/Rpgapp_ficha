package com.example.rpgapp.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.rpgapp.ui.screens.*
import com.example.rpgapp.viewmodel.FichaViewModel

@Composable
fun AppNavigation(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit
) {
    val nav = rememberNavController()
    val context = LocalContext.current
    val viewModel: FichaViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    NavHost(navController = nav, startDestination = "theme_selector") {
        composable("theme_selector") {
            ThemeSelectorScreen(
                currentTheme = currentTheme,
                onThemeSelected = { theme ->
                    onThemeChange(theme)
                    nav.navigate("ficha") {
                        popUpTo("theme_selector") { inclusive = true }
                    }
                }
            )
        }

        composable("ficha") {
            FichaRpgScreen(
                onSalvar = {},
                onInventario = {},
                onDescricao = {},
                onPericias = {},
                viewModel = viewModel,
                onThemeChange = { nav.navigate("theme_selector") }
            )
        }
    }
}