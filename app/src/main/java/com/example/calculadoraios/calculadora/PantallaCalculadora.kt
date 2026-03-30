package com.example.calculadoraios.calculadora

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val colorFondo      = Color(0xFF000000)
private val colorGrisOscuro = Color(0xFF2A2928)
private val colorGrisClaro  = Color(0xFF5E5D59)
private val colorNaranja    = Color(0xFFFFA70E)
private val colorBlanco     = Color.White

@Composable
fun PantallaCalculadora(
    viewModel: CalculadoraViewModel,
    onVerHistorial: () -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.displayValue.value) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Botón historial (ícono reloj, arriba a la derecha)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onVerHistorial,
                modifier = Modifier
                    .size(52.dp)
                    .background(colorGrisClaro, CircleShape)
            ) {
                Text("⏱", fontSize = 22.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Expresión encadenada (pequeña, gris)
        Text(
            text = viewModel.expresion.value,
            color = Color.Gray,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            textAlign = TextAlign.End,
            maxLines = 1
        )

        // Display principal con scroll horizontal
        Text(
            text = viewModel.displayValue.value,
            color = colorBlanco,
            fontSize = if (viewModel.displayValue.value.length > 9) 52.sp else 80.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 8.dp)
                .horizontalScroll(scrollState),
            textAlign = TextAlign.End,
            softWrap = false,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Fila 1: ⌫  AC  %  ÷
        FilaBotones {
            BotonCalc("⌫", colorGrisClaro) { viewModel.borrarUltimo() }
            BotonCalc("AC", colorGrisClaro) { viewModel.limpiar() }
            BotonCalc("%",  colorGrisClaro) { viewModel.porcentaje() }
            BotonCalc("÷",  colorNaranja)   { viewModel.presionarOperador("÷") }
        }
        // Fila 2: 7  8  9  ×
        FilaBotones {
            BotonCalc("7", colorGrisOscuro) { viewModel.presionarDigito("7") }
            BotonCalc("8", colorGrisOscuro) { viewModel.presionarDigito("8") }
            BotonCalc("9", colorGrisOscuro) { viewModel.presionarDigito("9") }
            BotonCalc("×", colorNaranja)    { viewModel.presionarOperador("×") }
        }
        // Fila 3: 4  5  6  -
        FilaBotones {
            BotonCalc("4", colorGrisOscuro) { viewModel.presionarDigito("4") }
            BotonCalc("5", colorGrisOscuro) { viewModel.presionarDigito("5") }
            BotonCalc("6", colorGrisOscuro) { viewModel.presionarDigito("6") }
            BotonCalc("-", colorNaranja)    { viewModel.presionarOperador("-") }
        }
        // Fila 4: 1  2  3  +
        FilaBotones {
            BotonCalc("1", colorGrisOscuro) { viewModel.presionarDigito("1") }
            BotonCalc("2", colorGrisOscuro) { viewModel.presionarDigito("2") }
            BotonCalc("3", colorGrisOscuro) { viewModel.presionarDigito("3") }
            BotonCalc("+", colorNaranja)    { viewModel.presionarOperador("+") }
        }
        // Fila 5: +/-  0  .  =  (todos iguales, como iOS real)
        FilaBotones {
            BotonCalc("+/-", colorGrisClaro)  { viewModel.cambiarSigno() }
            BotonCalc("0",   colorGrisOscuro) { viewModel.presionarDigito("0") }
            BotonCalc(".",   colorGrisOscuro) { viewModel.presionarDecimal() }
            BotonCalc("=",   colorNaranja)    { viewModel.calcularResultado() }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FilaBotones(contenido: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = contenido
    )
}

@Composable
fun RowScope.BotonCalc(
    texto: String,
    color: Color,
    modifier: Modifier = Modifier.weight(1f),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(88.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(texto, color = colorBlanco, fontSize = 28.sp, fontWeight = FontWeight.Normal)
    }
}