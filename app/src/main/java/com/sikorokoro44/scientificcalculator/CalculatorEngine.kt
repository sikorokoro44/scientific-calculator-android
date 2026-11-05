package com.sikorokoro44.scientificcalculator

import kotlin.math.*

class CalculatorEngine {
    private var pos = 0
    private var expr = ""
    private var degMode = true

    fun evaluate(expression: String, degrees: Boolean = true): Double {
        degMode = degrees
        expr = expression.replace(" ", "")
        pos = 0
        if (expr.isEmpty()) throw IllegalArgumentException("Empty expression")
        val result = parseExpression()
        if (pos < expr.length) throw IllegalArgumentException("Unexpected character: ${expr[pos]}")
        if (result.isNaN() || result.isInfinite()) throw ArithmeticException("Result is not finite")
        return result
    }

    private fun parseExpression(): Double = parseAddSub()

    private fun parseAddSub(): Double {
        var value = parseMulDiv()
        while (pos < expr.length && (expr[pos] == '+' || expr[pos] == '-')) {
            val op = expr[pos++]
            val rhs = parseMulDiv()
            value = if (op == '+') value + rhs else value - rhs
        }
        return value
    }

    private fun parseMulDiv(): Double {
        var value = parsePower()
        while (pos < expr.length && (expr[pos] == '*' || expr[pos] == '/' || expr[pos] == '%')) {
            val op = expr[pos++]
            val rhs = parsePower()
            value = when (op) {
                '*' -> value * rhs
                '/' -> { if (rhs == 0.0) throw ArithmeticException("Division by zero"); value / rhs }
                '%' -> { if (rhs == 0.0) throw ArithmeticException("Modulo by zero"); value % rhs }
                else -> value
            }
        }
        return value
    }

    private fun parsePower(): Double {
        val base = parseUnary()
        if (pos < expr.length && expr[pos] == '^') {
            pos++
            val exp = parsePower()
            return base.pow(exp)
        }
        return base
    }

    private fun parseUnary(): Double {
        if (pos < expr.length && (expr[pos] == '-' || expr[pos] == '+')) {
            val op = expr[pos++]
            val v = parseUnary()
            return if (op == '-') -v else v
        }
        return parsePrimary()
    }

    private fun parsePrimary(): Double {
        if (pos >= expr.length) throw IllegalArgumentException("Unexpected end of expression")
        val c = expr[pos]
        return when {
            c == '(' -> { pos++; val v = parseExpression(); if (pos >= expr.length || expr[pos] != ')') throw IllegalArgumentException("Missing closing parenthesis"); pos++; v }
            c.isDigit() || c == '.' -> parseNumber()
            c.isLetter() -> parseFunctionOrConstant()
            else -> throw IllegalArgumentException("Unexpected character: $c")
        }
    }

    private fun parseNumber(): Double {
        val start = pos
        while (pos < expr.length && (expr[pos].isDigit() || expr[pos] == '.' || expr[pos] == 'e' || expr[pos] == 'E' || (pos > start && (expr[pos] == '+' || expr[pos] == '-') && (expr[pos-1] == 'e' || expr[pos-1] == 'E')))) {
            pos++
        }
        val numStr = expr.substring(start, pos)
        return numStr.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: $numStr")
    }

    private fun parseFunctionOrConstant(): Double {
        val start = pos
        while (pos < expr.length && expr[pos].isLetter()) pos++
        val name = expr.substring(start, pos).lowercase()
        return when (name) {
            "pi" -> PI
            "e" -> E
            else -> {
                if (pos >= expr.length || expr[pos] != '(') throw IllegalArgumentException("Expected '(' after $name")
                pos++
                val arg = parseExpression()
                val second: Double? = if (pos < expr.length && expr[pos] == ',') { pos++; parseExpression() } else null
                if (pos >= expr.length || expr[pos] != ')') throw IllegalArgumentException("Missing closing parenthesis")
                pos++
                applyFunction(name, arg, second)
            }
        }
    }

    private fun applyFunction(name: String, arg: Double, second: Double?): Double {
        val rad = if (degMode) Math.toRadians(arg) else arg
        return when (name) {
            "sin" -> sin(rad)
            "cos" -> cos(rad)
            "tan" -> tan(rad)
            "asin" -> if (degMode) Math.toDegrees(asin(arg)) else asin(arg)
            "acos" -> if (degMode) Math.toDegrees(acos(arg)) else acos(arg)
            "atan" -> if (degMode) Math.toDegrees(atan(arg)) else atan(arg)
            "sinh" -> sinh(arg)
            "cosh" -> cosh(arg)
            "tanh" -> tanh(arg)
            "log" -> { if (arg <= 0) throw ArithmeticException("Log of non-positive"); if (second != null) { if (second <= 0 || second == 1.0) throw ArithmeticException("Invalid log base"); ln(arg) / ln(second) } else log10(arg) }
            "ln" -> { if (arg <= 0) throw ArithmeticException("Ln of non-positive"); ln(arg) }
            "sqrt" -> { if (arg < 0) throw ArithmeticException("Sqrt of negative"); sqrt(arg) }
            "root" -> { val n = second ?: 2.0; if (n == 0.0) throw ArithmeticException("Zero root"); arg.pow(1.0 / n) }
            "abs" -> abs(arg)
            "fact", "factorial" -> factorial(arg)
            else -> throw IllegalArgumentException("Unknown function: $name")
        }
    }

    private fun factorial(n: Double): Double {
        if (n < 0) throw ArithmeticException("Factorial of negative")
        if (n != n.toLong().toDouble()) throw ArithmeticException("Factorial requires integer")
        var r = 1.0
        var i = 2L
        while (i <= n.toLong()) { r *= i; i++ }
        return r
    }
}
