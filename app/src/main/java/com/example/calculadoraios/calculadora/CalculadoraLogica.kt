package com.example.calculadoraios.calculadora

class CalculadoraLogica {

    fun calcular(primerOperando: Double, operador: String, segundoOperando: Double): Double {
        return when (operador) {
            "+"  -> primerOperando + segundoOperando
            "-"  -> primerOperando - segundoOperando
            "×"  -> primerOperando * segundoOperando
            "÷"  -> {
                if (segundoOperando == 0.0) throw ArithmeticException("División por cero")
                primerOperando / segundoOperando
            }
            else -> segundoOperando
        }
    }

    fun formatearNumero(valor: Double): String {
        return if (valor == valor.toLong().toDouble()) {
            valor.toLong().toString()
        } else {
            valor.toBigDecimal().stripTrailingZeros().toPlainString()
        }
    }
}