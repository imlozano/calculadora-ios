package com.example.calculadoraios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.calculadoraios.navegacion.Navegacion
import com.example.calculadoraios.ui.theme.CalculadoraiOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraiOSTheme {
               Navegacion()
            }
        }
    }
}
