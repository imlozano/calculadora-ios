package com.example.calculadoraios.calculadora

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculadoraViewModelTest {

    @Test
    fun respeta_prioridad_y_guarda_historial() {
        val viewModel = CalculadoraViewModel()

        viewModel.presionarDigito("2")
        viewModel.presionarOperador("+")
        viewModel.presionarDigito("3")
        viewModel.presionarOperador("×")
        viewModel.presionarDigito("4")
        viewModel.calcularResultado()

        assertEquals("14", viewModel.displayValue.value)
        assertEquals("2 + 3 × 4 =", viewModel.expresion.value)
        assertEquals(1, viewModel.historialViewModel.operaciones.value.size)
        assertEquals("2 + 3 × 4", viewModel.historialViewModel.operaciones.value.first().expresion)
        assertEquals("14", viewModel.historialViewModel.operaciones.value.first().resultado)
    }

    @Test
    fun division_por_cero_muestra_error_y_no_guarda_historial() {
        val viewModel = CalculadoraViewModel()

        viewModel.presionarDigito("5")
        viewModel.presionarOperador("÷")
        viewModel.presionarDigito("0")
        viewModel.calcularResultado()

        assertEquals("Error", viewModel.displayValue.value)
        assertEquals("", viewModel.expresion.value)
        assertEquals(0, viewModel.historialViewModel.operaciones.value.size)
    }

    @Test
    fun permite_operando_negativo_despues_de_un_operador() {
        val viewModel = CalculadoraViewModel()

        viewModel.presionarDigito("8")
        viewModel.presionarOperador("×")
        viewModel.presionarOperador("-")
        viewModel.presionarDigito("3")

        assertEquals("8 × (-3)", viewModel.displayValue.value)
    }

    @Test
    fun permite_decimal_negativo_despues_de_un_operador() {
        val viewModel = CalculadoraViewModel()

        viewModel.presionarDigito("8")
        viewModel.presionarOperador("×")
        viewModel.presionarOperador("-")
        viewModel.presionarDecimal()
        viewModel.presionarDigito("5")

        assertEquals("8 × (-0.5)", viewModel.displayValue.value)
    }

    @Test
    fun cambiar_signo_alterna_el_numero_actual() {
        val viewModel = CalculadoraViewModel()

        viewModel.presionarDigito("3")
        viewModel.cambiarSigno()
        assertEquals("(-3)", viewModel.displayValue.value)

        viewModel.cambiarSigno()
        assertEquals("3", viewModel.displayValue.value)
    }

    @Test
    fun porcentaje_actualiza_el_valor_actual() {
        val viewModel = CalculadoraViewModel()

        viewModel.presionarDigito("5")
        viewModel.presionarDigito("0")
        viewModel.porcentaje()

        assertEquals("0.5", viewModel.displayValue.value)
    }
}
