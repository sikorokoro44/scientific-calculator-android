package com.sikorokoro44.scientificcalculator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var degrees by remember { mutableStateOf(true) }
    val engine = remember { CalculatorEngine() }

    Column(Modifier.fillMaxSize().padding(8.dp)) {
        Text(expression, Modifier.fillMaxWidth(), textAlign = TextAlign.End, style = MaterialTheme.typography.headlineMedium)
        Text(result, Modifier.fillMaxWidth(), textAlign = TextAlign.End, style = MaterialTheme.typography.titleLarge)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { degrees = !degrees }) { Text(if (degrees) "DEG" else "RAD") }
        }
        val buttons = listOf(
            listOf("C","(",")","/"),
            listOf("7","8","9","*"),
            listOf("4","5","6","-"),
            listOf("1","2","3","+"),
            listOf("0",".","%","="),
            listOf("sin","cos","tan","ln"),
            listOf("log","sqrt","^","!"),
            listOf("pi","e","abs","DEL")
        )
        buttons.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                row.forEach { label ->
                    Button(onClick = {
                        when (label) {
                            "C" -> { expression = ""; result = "" }
                            "DEL" -> { if (expression.isNotEmpty()) expression = expression.dropLast(1) }
                            "=" -> {
                                try { result = engine.evaluate(expression, degrees).toString() }
                                catch (e: Exception) { result = "Error: ${e.message}" }
                            }
                            "pi" -> expression += "pi"
                            "e" -> expression += "e"
                            "!" -> expression += "fact("
                            else -> expression += if (label in listOf("sin","cos","tan","log","ln","sqrt","abs")) "$label(" else label
                        }
                    }, Modifier.weight(1f)) { Text(label) }
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}
