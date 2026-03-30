package com.example.calculadoraios.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraios.viewmodel.CalculadoraViewModel

private val colorFondo       = Color(0xFF000000)
private val colorGrisOscuro  = Color(0xFF505050)
private val colorGrisClaro   = Color(0xFFA5A5A5)
private val colorNaranja     = Color(0xFFFF9F0A)
private val colorBlanco      = Color.White

@Composable
fun PantallaCalculadora(
    viewModel: CalculadoraViewModel,
    onVerHistorial: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Botón historial arriba a la derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onVerHistorial) {
                Text("Historial", color = colorNaranja, fontSize = 16.sp)
            }
        }

        // Expresión encadenada
        Text(
            text = viewModel.expresion,
            color = Color.Gray,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            textAlign = TextAlign.End
        )

        // Display principal
        Text(
            text = viewModel.displayValue,
            color = colorBlanco,
            fontSize = if (viewModel.displayValue.length > 9) 52.sp else 80.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 8.dp),
            textAlign = TextAlign.End,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Fila 1: AC  +/-  %  ÷
        FilaBotones {
            BotonCalc("AC",  colorGrisClaro)  { viewModel.limpiar() }
            BotonCalc("+/-", colorGrisClaro)  { viewModel.cambiarSigno() }
            BotonCalc("%",   colorGrisClaro)  { viewModel.porcentaje() }
            BotonCalc("÷",   colorNaranja)    { viewModel.presionarOperador("÷") }
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

        // Fila 5: 0 (ancho doble)  .  =
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.presionarDigito("0") },
                modifier = Modifier
                    .weight(2f)
                    .height(80.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorGrisOscuro)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "0",
                        color = colorBlanco,
                        fontSize = 32.sp,
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
                    )
                }
            }

            BotonCalc(".", colorGrisOscuro, Modifier.weight(1f)) { viewModel.presionarDecimal() }
            BotonCalc("=", colorNaranja,    Modifier.weight(1f)) { viewModel.calcularResultado() }
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
        modifier = modifier.height(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = texto,
            color = colorBlanco,
            fontSize = 30.sp,
            fontWeight = FontWeight.Normal
        )
    }
}