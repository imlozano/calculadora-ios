package com.example.calculadoraios.calculadora

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculadoraLogicaTest {

    private val logica = CalculadoraLogica()

    @Test
    fun calcula_operaciones_basicas() {
        assertEquals(8.0, logica.calcular(5.0, "+", 3.0), 0.0)
        assertEquals(2.0, logica.calcular(5.0, "-", 3.0), 0.0)
        assertEquals(15.0, logica.calcular(5.0, "×", 3.0), 0.0)
        assertEquals(2.5, logica.calcular(5.0, "÷", 2.0), 0.0)
    }

    @Test
    fun lanza_error_en_division_por_cero() {
        assertThrows(ArithmeticException::class.java) {
            logica.calcular(5.0, "÷", 0.0)
        }
    }

    @Test
    fun formatea_numeros_sin_ceros_innecesarios() {
        assertEquals("31", logica.formatearNumero(31.0))
        assertEquals("9.5", logica.formatearNumero(9.5))
    }
}
