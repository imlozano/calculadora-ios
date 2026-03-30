package com.example.calculadoraios.calculadora

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculadoraios.historial.HistorialViewModel

class CalculadoraViewModel : ViewModel() {

    private val logica = CalculadoraLogica()
    val historialViewModel = HistorialViewModel()

    val displayValue = mutableStateOf("0")
    val expresion = mutableStateOf("")

    private val tokens = mutableListOf<String>()
    private var trasPulsarIgual = false
    private var signoNegativoPendiente = false
    private var enEstadoDeError = false

    private fun esOperador(token: String) = token == "+" || token == "-" || token == "×" || token == "÷"

    private fun formatearToken(token: String) = if (token.startsWith("-") && token.length > 1) "($token)" else token

    private fun reconstruirExpresion(): String {
        if (tokens.isEmpty()) return if (signoNegativoPendiente) "-" else "0"

        val expresionActual = buildString {
            for (token in tokens) {
                if (esOperador(token)) append(" $token ") else append(formatearToken(token))
            }
        }

        val expresionLimpia = expresionActual.trimEnd()
        return if (signoNegativoPendiente) "$expresionLimpia -" else expresionLimpia
    }

    private fun mostrarEstadoActual() {
        displayValue.value = reconstruirExpresion()
    }

    private fun reiniciarEstadoSiHayError() {
        if (!enEstadoDeError) return
        limpiar()
    }

    private fun mostrarError() {
        tokens.clear()
        trasPulsarIgual = false
        signoNegativoPendiente = false
        enEstadoDeError = true
        expresion.value = ""
        displayValue.value = "Error"
    }

    fun presionarDigito(digito: String) {
        reiniciarEstadoSiHayError()
        expresion.value = ""
        if (trasPulsarIgual) {
            tokens.clear()
            trasPulsarIgual = false
        }

        val numeroActual = if (signoNegativoPendiente) "-$digito" else digito
        signoNegativoPendiente = false

        val ultimoToken = tokens.lastOrNull()
        when {
            tokens.isEmpty() -> tokens.add(numeroActual)
            ultimoToken == null -> tokens.add(numeroActual)
            esOperador(ultimoToken) -> tokens.add(numeroActual)
            ultimoToken == "0" -> tokens[tokens.lastIndex] = numeroActual
            ultimoToken == "-0" -> tokens[tokens.lastIndex] = "-$digito"
            else -> tokens[tokens.lastIndex] = ultimoToken + digito
        }
        mostrarEstadoActual()
    }

    fun presionarDecimal() {
        reiniciarEstadoSiHayError()
        expresion.value = ""
        if (trasPulsarIgual) {
            tokens.clear()
            trasPulsarIgual = false
        }

        val ultimoToken = tokens.lastOrNull()
        val nuevoTokenDecimal = if (signoNegativoPendiente) "-0." else "0."

        when {
            tokens.isEmpty() -> tokens.add(nuevoTokenDecimal)
            ultimoToken == null -> tokens.add(nuevoTokenDecimal)
            esOperador(ultimoToken) -> tokens.add(nuevoTokenDecimal)
            !ultimoToken.contains(".") -> tokens[tokens.lastIndex] = "$ultimoToken."
        }

        signoNegativoPendiente = false
        mostrarEstadoActual()
    }

    fun presionarOperador(operador: String) {
        if (enEstadoDeError) return
        expresion.value = ""
        trasPulsarIgual = false
        if (tokens.isEmpty()) return

        val ultimoToken = tokens.last()
        when {
            esOperador(ultimoToken) -> {
                if (operador == "-") {
                    signoNegativoPendiente = true
                } else {
                    signoNegativoPendiente = false
                    tokens[tokens.lastIndex] = operador
                }
            }
            signoNegativoPendiente -> {
                if (operador != "-") {
                    signoNegativoPendiente = false
                    val indiceUltimoOperador = tokens.indexOfLast { esOperador(it) }
                    if (indiceUltimoOperador >= 0) {
                        tokens[indiceUltimoOperador] = operador
                    }
                }
            }
            else -> {
                signoNegativoPendiente = false
                tokens.add(operador)
            }
        }
        mostrarEstadoActual()
    }

    fun calcularResultado() {
        if (enEstadoDeError) return

        signoNegativoPendiente = false
        val tokensParaCalculo = tokens.dropLastWhile { esOperador(it) }
        if (tokensParaCalculo.size < 3) return

        try {
            val tokensReducidos = tokensParaCalculo.toMutableList()
            var indice = 1
            while (indice < tokensReducidos.size) {
                val operador = tokensReducidos[indice]
                if (operador == "×" || operador == "÷") {
                    val numeroIzquierdo = tokensReducidos[indice - 1].toDoubleOrNull() ?: break
                    val numeroDerecho = tokensReducidos[indice + 1].toDoubleOrNull() ?: break
                    val resultadoParcial = logica.calcular(numeroIzquierdo, operador, numeroDerecho)
                    tokensReducidos[indice - 1] = logica.formatearNumero(resultadoParcial)
                    tokensReducidos.removeAt(indice)
                    tokensReducidos.removeAt(indice)
                } else {
                    indice += 2
                }
            }

            var resultadoFinal = tokensReducidos[0].toDoubleOrNull() ?: return
            var indiceLectura = 1
            while (indiceLectura + 1 < tokensReducidos.size) {
                val operador = tokensReducidos[indiceLectura]
                val siguienteNumero = tokensReducidos[indiceLectura + 1].toDoubleOrNull() ?: break
                resultadoFinal = logica.calcular(resultadoFinal, operador, siguienteNumero)
                indiceLectura += 2
            }

            val resultadoTexto = logica.formatearNumero(resultadoFinal)
            val operacionTexto = tokensParaCalculo.joinToString("") { token ->
                if (esOperador(token)) " $token " else formatearToken(token)
            }

            historialViewModel.agregarOperacion(operacionTexto, resultadoTexto)
            expresion.value = "$operacionTexto ="
            displayValue.value = resultadoTexto

            tokens.clear()
            tokens.add(resultadoTexto)
            trasPulsarIgual = true
            enEstadoDeError = false

        } catch (_: ArithmeticException) {
            mostrarError()
        }
    }

    fun limpiar() {
        tokens.clear()
        signoNegativoPendiente = false
        displayValue.value = "0"
        expresion.value = ""
        trasPulsarIgual = false
        enEstadoDeError = false
    }

    fun borrarUltimo() {
        if (enEstadoDeError) return
        if (trasPulsarIgual) return
        if (signoNegativoPendiente) {
            signoNegativoPendiente = false
            mostrarEstadoActual()
            return
        }

        val ultimoToken = tokens.lastOrNull() ?: return
        when {
            esOperador(ultimoToken) -> tokens.removeAt(tokens.lastIndex)
            ultimoToken.startsWith("-") && ultimoToken.length == 2 -> {
                tokens.removeAt(tokens.lastIndex)
                signoNegativoPendiente = tokens.lastOrNull()?.let(::esOperador) == true
            }
            ultimoToken.length <= 1 -> tokens.removeAt(tokens.lastIndex)
            else -> tokens[tokens.lastIndex] = ultimoToken.dropLast(1)
        }
        mostrarEstadoActual()
    }

    fun cambiarSigno() {
        if (enEstadoDeError) return

        val ultimoToken = tokens.lastOrNull() ?: return
        if (esOperador(ultimoToken) || ultimoToken == "0") return

        tokens[tokens.lastIndex] = if (ultimoToken.startsWith("-")) {
            ultimoToken.substring(1)
        } else {
            "-$ultimoToken"
        }
        mostrarEstadoActual()
    }

    fun porcentaje() {
        if (enEstadoDeError) return

        val ultimoToken = tokens.lastOrNull() ?: return
        if (esOperador(ultimoToken)) return

        val valorActual = ultimoToken.toDoubleOrNull() ?: return
        tokens[tokens.lastIndex] = logica.formatearNumero(valorActual / 100)
        mostrarEstadoActual()
    }
}
