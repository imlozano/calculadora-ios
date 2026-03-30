package com.example.calculadoraios.calculadora

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculadoraios.historial.HistorialViewModel

class CalculadoraViewModel : ViewModel() {

    private val logica = CalculadoraLogica()
    val historialViewModel = HistorialViewModel()

    val displayValue = mutableStateOf("0")
    val expresion = mutableStateOf("")

    private val piezas = mutableListOf<String>() // ["8", "×", "5", "-", "9"]
    private var trasPulsarIgual = false
    private var signoNegativo = false // "-" pulsado tras un operador

    private fun esOp(s: String) = s == "+" || s == "-" || s == "×" || s == "÷"

    // Muestra (-8) si el número es negativo
    private fun fmtNum(s: String) = if (s.startsWith("-") && s.length > 1) "($s)" else s

    private fun reconstruir(): String {
        if (piezas.isEmpty()) return if (signoNegativo) "0 -" else "0"
        val sb = StringBuilder()
        for (p in piezas) {
            if (esOp(p)) sb.append(" $p ") else sb.append(fmtNum(p))
        }
        return if (signoNegativo) "$sb -" else sb.toString()
    }

    fun presionarDigito(digito: String) {
        expresion.value = ""
        if (trasPulsarIgual) { piezas.clear(); trasPulsarIgual = false }

        val digitoFinal = if (signoNegativo) "-$digito" else digito
        signoNegativo = false

        val ult = piezas.lastOrNull()
        when {
            piezas.isEmpty()  -> piezas.add(digitoFinal)
            esOp(ult!!)       -> piezas.add(digitoFinal)
            ult == "0"        -> piezas[piezas.lastIndex] = digitoFinal
            else              -> piezas[piezas.lastIndex] = ult + digito
        }
        displayValue.value = reconstruir()
    }

    fun presionarDecimal() {
        expresion.value = ""
        if (trasPulsarIgual) { piezas.clear(); trasPulsarIgual = false }
        signoNegativo = false
        val ult = piezas.lastOrNull()
        when {
            piezas.isEmpty() || esOp(ult!!) -> piezas.add("0.")
            !ult!!.contains(".")            -> piezas[piezas.lastIndex] = "$ult."
        }
        displayValue.value = reconstruir()
    }

    fun presionarOperador(operador: String) {
        expresion.value = ""
        trasPulsarIgual = false
        if (piezas.isEmpty()) return

        val ult = piezas.last()
        when {
            esOp(ult) -> {
                if (operador == "-") {
                    signoNegativo = true        // próximo número será negativo
                } else {
                    signoNegativo = false
                    piezas[piezas.lastIndex] = operador  // reemplaza operador
                }
            }
            signoNegativo -> {
                if (operador != "-") {
                    signoNegativo = false
                    val idxOp = piezas.indexOfLast { esOp(it) }
                    if (idxOp >= 0) piezas[idxOp] = operador
                }
            }
            else -> {
                signoNegativo = false
                piezas.add(operador)
            }
        }
        displayValue.value = reconstruir()
    }

    fun calcularResultado() {
        signoNegativo = false
        val trabajo = piezas.dropLastWhile { esOp(it) }
        if (trabajo.size < 3) return

        try {
            val temp = trabajo.toMutableList()
            var i = 1
            while (i < temp.size) {
                val op = temp[i]
                if (op == "×" || op == "÷") {
                    val izq = temp[i - 1].toDoubleOrNull() ?: break
                    val der = temp[i + 1].toDoubleOrNull() ?: break
                    val res = logica.calcular(izq, op, der)
                    temp[i - 1] = logica.formatearNumero(res)
                    temp.removeAt(i)     // elimina operador
                    temp.removeAt(i)     // elimina operando derecho
                } else {
                    i += 2
                }
            }

            var resultado = temp[0].toDoubleOrNull() ?: return
            var j = 1
            while (j + 1 < temp.size) {
                val op = temp[j]
                val sig = temp[j + 1].toDoubleOrNull() ?: break
                resultado = logica.calcular(resultado, op, sig)
                j += 2
            }

            val resultadoStr = logica.formatearNumero(resultado)
            val expStr = trabajo.joinToString("") { t ->
                if (esOp(t)) " $t " else fmtNum(t)
            }

            historialViewModel.agregarOperacion(expStr, resultadoStr)
            expresion.value = "$expStr ="
            displayValue.value = resultadoStr

            piezas.clear()
            piezas.add(resultadoStr)
            trasPulsarIgual = true

        } catch (e: ArithmeticException) {
            displayValue.value = "Error"
            limpiar()
        }
    }
    fun limpiar() {
        piezas.clear()
        signoNegativo = false
        displayValue.value = "0"
        expresion.value = ""
        trasPulsarIgual = false
    }

    fun borrarUltimo() {
        if (trasPulsarIgual) return
        if (signoNegativo) { signoNegativo = false; displayValue.value = reconstruir(); return }
        val ult = piezas.lastOrNull() ?: return
        if (ult.length <= 1) piezas.removeAt(piezas.lastIndex)
        else piezas[piezas.lastIndex] = ult.dropLast(1)
        displayValue.value = reconstruir()
    }

    fun cambiarSigno() {
        val ult = piezas.lastOrNull() ?: return
        if (esOp(ult)) return
        piezas[piezas.lastIndex] = if (ult.startsWith("-")) ult.substring(1) else "-$ult"
        displayValue.value = reconstruir()
    }

    fun porcentaje() {
        val ult = piezas.lastOrNull() ?: return
        if (esOp(ult)) return
        val v = ult.toDoubleOrNull() ?: return
        piezas[piezas.lastIndex] = logica.formatearNumero(v / 100)
        displayValue.value = reconstruir()
    }
}