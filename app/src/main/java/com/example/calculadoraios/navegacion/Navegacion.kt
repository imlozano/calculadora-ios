package com.example.calculadoraios.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoraios.pantallas.PantallaCalculadora
import com.example.calculadoraios.pantallas.PantallaHistorial
import com.example.calculadoraios.viewmodel.CalculadoraViewModel

@Composable
fun Navegacion(viewModel: CalculadoraViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "calculadora") {
        composable("calculadora") {
            PantallaCalculadora(
                viewModel = viewModel,
                onVerHistorial = { navController.navigate("historial") }
            )
        }
        composable("historial") {
            PantallaHistorial(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}