package com.example.calculadoraios

import com.example.calculadoraios.calculadora.CalculadoraLogica
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CalculadoraLogicaTest {

    private lateinit var logica: CalculadoraLogica

    @Before
    fun setUp() {
        logica = CalculadoraLogica()
    }

    @Test
    fun suma() {
        assertEquals(5.0, logica.calcular(2.0, "+", 3.0), 0.0)
    }

    @Test
    fun resta() {
        assertEquals(1.0, logica.calcular(4.0, "-", 3.0), 0.0)
    }

    @Test
    fun multiplicacion() {
        assertEquals(12.0, logica.calcular(3.0, "×", 4.0), 0.0)
    }

    @Test
    fun division() {
        assertEquals(2.5, logica.calcular(5.0, "÷", 2.0), 0.0)
    }

    @Test(expected = ArithmeticException::class)
    fun divisionPorCeroLanzaExcepcion() {
        logica.calcular(5.0, "÷", 0.0)
    }

    @Test
    fun formatearNumeroEntero() {
        assertEquals("7", logica.formatearNumero(7.0))
    }

    @Test
    fun formatearNumeroDecimal() {
        assertEquals("3.14", logica.formatearNumero(3.14))
    }
}
