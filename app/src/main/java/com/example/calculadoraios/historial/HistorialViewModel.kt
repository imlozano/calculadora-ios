package com.example.calculadoraios.historial

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HistorialViewModel : ViewModel() {
    val operaciones = mutableStateOf<List<OperacionHistorial>>(emptyList())

    fun agregarOperacion(expresion: String, resultado: String) {
        val nuevaOperacion = OperacionHistorial(
            expresion = expresion,
            resultado = resultado
        )
        operaciones.value = listOf(nuevaOperacion) + operaciones.value
    }
}
