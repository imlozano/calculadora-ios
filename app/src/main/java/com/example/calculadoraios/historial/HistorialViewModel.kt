package com.example.calculadoraios.historial

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculadoraios.historial.OperacionHistorial

class HistorialViewModel : ViewModel() {
    val operaciones = mutableStateOf<List<OperacionHistorial>>(emptyList())

    fun agregarOperacion(expresion: String, resultado: String) {
        val nueva = OperacionHistorial()
        nueva.expresion = expresion
        nueva.resultado = resultado
        operaciones.value = listOf(nueva) + operaciones.value
    }
}