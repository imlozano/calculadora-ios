package com.example.calculadoraios.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoraios.calculadora.PantallaCalculadora
import com.example.calculadoraios.historial.PantallaHistorial
import com.example.calculadoraios.calculadora.CalculadoraViewModel

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    val viewModel: CalculadoraViewModel = viewModel()

    NavHost(navController = navController, startDestination = "calculadora") {
        composable("calculadora") {
            PantallaCalculadora(viewModel = viewModel, onVerHistorial = { navController.navigate("historial") })
        }
        composable("historial") {
            PantallaHistorial(viewModel = viewModel.historialViewModel, onVolver = { navController.popBackStack() })
        }
    }
}