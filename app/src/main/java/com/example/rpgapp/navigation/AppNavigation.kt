package com.example.rpgapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.rpgapp.ui.screens.*
import com.example.rpgapp.viewmodel.FichaViewModel

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val context = LocalContext.current
    val viewModel: FichaViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    NavHost(navController = nav, startDestination = "ficha") {
        composable("ficha") {
            FichaRpgScreen(
                onSalvar = {},
                onInventario = {},
                onDescricao = {},
                onPericias = {},
                viewModel = viewModel
            )
        }
    }
}