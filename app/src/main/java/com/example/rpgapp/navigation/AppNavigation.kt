package com.example.rpgapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.rpgapp.ui.screens.*

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "ficha") {
        composable("ficha") {
            FichaRpgScreen(
                onSalvar = {

                },
                onInventario = { /* Não mais necessário com TabRow */ },
                onDescricao = { /* Não mais necessário com TabRow */ },
                onPericias = { /* Não mais necessário com TabRow */ }
            )
        }
    }
}