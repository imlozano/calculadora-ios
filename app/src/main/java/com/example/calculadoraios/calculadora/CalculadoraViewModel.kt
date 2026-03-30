package com.example.calculadoraios.calculadora

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculadoraios.historial.HistorialViewModel

class CalculadoraViewModel : ViewModel() {

    private val logica = CalculadoraLogica()
    val historialViewModel = HistorialViewModel()

    val displayValue = mutableStateOf("0")
    val expresion = mutableStateOf("")

    private var primerOperando: Double? = null
    private var operadorActual: String = ""
    private var esperandoSegundoOperando: Boolean = false

    fun presionarDigito(digito: String) {
        if (esperandoSegundoOperando) {
            displayValue.value = digito
            esperandoSegundoOperando = false
        } else {
            displayValue.value = if (displayValue.value == "0") digito
            else displayValue.value + digito
        }
        if (primerOperando != null && operadorActual.isNotEmpty() && !esperandoSegundoOperando) {
            expresion.value = "${logica.formatearNumero(primerOperando!!)} $operadorActual ${displayValue.value}"
        }
    }

    fun presionarDecimal() {
        if (esperandoSegundoOperando) {
            displayValue.value = "0."
            esperandoSegundoOperando = false
            return
        }
        if (!displayValue.value.contains(".")) {
            displayValue.value += "."
        }
        if (primerOperando != null && operadorActual.isNotEmpty()) {
            expresion.value = "${logica.formatearNumero(primerOperando!!)} $operadorActual ${displayValue.value}"
        }
    }

    fun presionarOperador(operador: String) {
        if (esperandoSegundoOperando && operador == "-") {
            displayValue.value = "-"
            esperandoSegundoOperando = false
            return
        }

        val valorActual = displayValue.value.toDoubleOrNull() ?: return

        if (primerOperando != null && !esperandoSegundoOperando) {
            try {
                val resultado = logica.calcular(primerOperando!!, operadorActual, valorActual)
                displayValue.value = logica.formatearNumero(resultado)
                primerOperando = resultado
            } catch (e: ArithmeticException) {
                displayValue.value = "Error"
                limpiar()
                return
            }
        } else {
            primerOperando = valorActual
        }

        operadorActual = operador
        expresion.value = "${logica.formatearNumero(primerOperando!!)} $operador"
        esperandoSegundoOperando = true
    }

    fun calcularResultado() {
        val segundoOperando = displayValue.value.toDoubleOrNull() ?: return
        if (primerOperando == null || operadorActual.isEmpty()) return

        try {
            val resultado = logica.calcular(primerOperando!!, operadorActual, segundoOperando)
            val resultadoStr = logica.formatearNumero(resultado)
            val expStr = "${logica.formatearNumero(primerOperando!!)} $operadorActual ${logica.formatearNumero(segundoOperando)}"

            historialViewModel.agregarOperacion(expStr, resultadoStr)

            expresion.value = "$expStr ="
            displayValue.value = resultadoStr
            primerOperando = resultado
            operadorActual = ""
            esperandoSegundoOperando = true

        } catch (e: ArithmeticException) {
            displayValue.value = "Error"
            expresion.value = ""
            limpiar()
        }
    }

    fun limpiar() {
        displayValue.value = "0"
        expresion.value = ""
        primerOperando = null
        operadorActual = ""
        esperandoSegundoOperando = false
    }

    fun cambiarSigno() {
        val valor = displayValue.value.toDoubleOrNull() ?: return
        displayValue.value = logica.formatearNumero(-valor)
    }

    fun porcentaje() {
        val valor = displayValue.value.toDoubleOrNull() ?: return
        displayValue.value = logica.formatearNumero(valor / 100)
    }

    fun borrarUltimo() {
        if (esperandoSegundoOperando) return
        val actual = displayValue.value
        displayValue.value = if (actual.length <= 1) "0" else actual.dropLast(1)
        if (primerOperando != null && operadorActual.isNotEmpty()) {
            expresion.value = "${logica.formatearNumero(primerOperando!!)} $operadorActual ${displayValue.value}"
        }
    }


}