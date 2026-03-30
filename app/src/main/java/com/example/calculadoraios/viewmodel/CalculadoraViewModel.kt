package com.example.calculadoraios.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calculadoraios.domain.CalculadoraLogica
import com.example.calculadoraios.modelo.Operacion

class CalculadoraViewModel : ViewModel() {

    private val logica = CalculadoraLogica()

    var displayValue by mutableStateOf("0")
    var expresion by mutableStateOf("")
    var historial by mutableStateOf<List<Operacion>>(emptyList())

    private var primerOperando: Double? = null
    private var operadorActual: String = ""
    private var esperandoSegundoOperando: Boolean = false

    fun presionarDigito(digito: String) {
        if (esperandoSegundoOperando) {
            displayValue = digito
            esperandoSegundoOperando = false
        } else {
            displayValue = if (displayValue == "0") digito else displayValue + digito
        }
    }

    fun presionarDecimal() {
        if (esperandoSegundoOperando) {
            displayValue = "0."
            esperandoSegundoOperando = false
            return
        }
        if (!displayValue.contains(".")) {
            displayValue += "."
        }
    }

    fun presionarOperador(operador: String) {
        val valorActual = displayValue.toDoubleOrNull() ?: return

        if (primerOperando != null && !esperandoSegundoOperando) {
            try {
                val resultado = logica.calcular(primerOperando!!, operadorActual, valorActual)
                displayValue = logica.formatearNumero(resultado)
                primerOperando = resultado
            } catch (e: ArithmeticException) {
                displayValue = "Error"
                limpiar()
                return
            }
        } else {
            primerOperando = valorActual
        }

        operadorActual = operador
        expresion = "${logica.formatearNumero(primerOperando!!)} $operador"
        esperandoSegundoOperando = true
    }

    fun calcularResultado() {
        val segundoOperando = displayValue.toDoubleOrNull() ?: return
        if (primerOperando == null || operadorActual.isEmpty()) return

        try {
            val resultado = logica.calcular(primerOperando!!, operadorActual, segundoOperando)
            val resultadoStr = logica.formatearNumero(resultado)

            val operacion = Operacion()
            operacion.expresion = "${logica.formatearNumero(primerOperando!!)} $operadorActual ${logica.formatearNumero(segundoOperando)}"
            operacion.resultado = resultadoStr

            historial = listOf(operacion) + historial

            expresion = "${operacion.expresion} ="
            displayValue = resultadoStr
            primerOperando = resultado
            operadorActual = ""
            esperandoSegundoOperando = true

        } catch (e: ArithmeticException) {
            displayValue = "Error"
            expresion = ""
            limpiar()
        }
    }

    fun limpiar() {
        displayValue = "0"
        expresion = ""
        primerOperando = null
        operadorActual = ""
        esperandoSegundoOperando = false
    }

    fun cambiarSigno() {
        val valor = displayValue.toDoubleOrNull() ?: return
        displayValue = logica.formatearNumero(-valor)
    }

    fun porcentaje() {
        val valor = displayValue.toDoubleOrNull() ?: return
        displayValue = logica.formatearNumero(valor / 100)
    }
}