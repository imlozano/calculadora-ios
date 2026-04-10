package com.example.calculadoraios

import com.example.calculadoraios.calculadora.CalculadoraLogica
import com.example.calculadoraios.calculadora.CalculadoraViewModel
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

class CalculadoraViewModelTest {

    private lateinit var vm: CalculadoraViewModel
@Before
    fun setUp() {
        vm = CalculadoraViewModel()
}

    @Test
    fun presionarDigitosFormaNumero() {
        vm.presionarDigito("1")
        vm.presionarDigito("2")
        vm.presionarDigito("3")
        assertEquals("123", vm.displayValue.value)
    }

    @Test
    fun presionarOperadorEncadenaOperacion() {
        vm.presionarDigito("5")
        vm.presionarOperador("+")
        vm.presionarDigito("3")
        assertEquals("5 + 3", vm.displayValue.value)
    }

    @Test
    fun calcularResultadoRespetaPrecedencia() {
        // 2 + 3 × 4 = 14 (multiplicación antes que suma)
        vm.presionarDigito("2")
        vm.presionarOperador("+")
        vm.presionarDigito("3")
        vm.presionarOperador("×")
        vm.presionarDigito("4")
        vm.calcularResultado()
        assertEquals("14", vm.displayValue.value)
    }

    @Test
    fun cambiarSignoInvierteSigno() {
        vm.presionarDigito("9")
        vm.cambiarSigno()
        assertEquals("(-9)", vm.displayValue.value)
    }

    @Test
    fun porcentajeDivideEntre100() {
        vm.presionarDigito("5")
        vm.presionarDigito("0")
        vm.porcentaje()
        assertEquals("0.5", vm.displayValue.value)
    }

    @Test
    fun borrarUltimoEliminaUltimoCaracter() {
        vm.presionarDigito("4")
        vm.presionarDigito("5")
        vm.borrarUltimo()
        assertEquals("4", vm.displayValue.value)
    }

    @Test
    fun limpiarReseteaACero() {
        vm.presionarDigito("7")
        vm.presionarOperador("+")
        vm.presionarDigito("3")
        vm.limpiar()
        assertEquals("0", vm.displayValue.value)
    }

    @Test
    fun divisionPorCeroMuestraSinDefinir() {
        vm.presionarDigito("5")
        vm.presionarOperador("÷")
        vm.presionarDigito("0")
        vm.calcularResultado()
        assertEquals("Sin definir", vm.displayValue.value)
    }
}
