package com.sikorokoro44.scientificcalculator

import com.sikorokoro44.scientificcalculator.ui.theme.ScientificCalculatorTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ScientificCalculatorTheme { Surface { CalculatorScreen() } } }
    }
}
