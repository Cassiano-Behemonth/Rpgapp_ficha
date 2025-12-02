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
                onSalvar = { /* implemente depois */ },
                onInventario = { nav.navigate("inventario") },
                onDescricao = { nav.navigate("descricao") },
                onPericias = { nav.navigate("pericias") }
            )
        }

        composable("pericias") { PericiasScreen(onBack = { nav.popBackStack() }) }
        composable("inventario") { InventarioScreen(onBack = { nav.popBackStack() }) }
        composable("descricao") { DescricaoScreen(onBack = { nav.popBackStack() }) }
    }
}
