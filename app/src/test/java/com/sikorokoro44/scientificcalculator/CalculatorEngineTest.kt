package com.sikorokoro44.scientificcalculator

import org.junit.Assert.*
import org.junit.Test

class CalculatorEngineTest {
    private val e = CalculatorEngine()

    @Test fun basicAdd() = assertEquals(5.0, e.evaluate("2+3"), 1e-9)
    @Test fun precedence() = assertEquals(14.0, e.evaluate("2+3*4"), 1e-9)
    @Test fun parens() = assertEquals(20.0, e.evaluate("(2+3)*4"), 1e-9)
    @Test fun nested() = assertEquals(23.0, e.evaluate("2*(3+4)+5*(1+2)"), 1e-9)
    @Test fun division() = assertEquals(2.5, e.evaluate("5/2"), 1e-9)
    @Test fun percent() = assertEquals(50.0, e.evaluate("50%100"), 1e-9)
    @Test fun power() = assertEquals(8.0, e.evaluate("2^3"), 1e-9)
    @Test fun sqrt() = assertEquals(3.0, e.evaluate("sqrt(9)"), 1e-9)
    @Test fun sinDeg() = assertEquals(1.0, e.evaluate("sin(90)"), 1e-9)
    @Test fun cosDeg() = assertEquals(1.0, e.evaluate("cos(0)"), 1e-9)
    @Test fun log10() = assertEquals(2.0, e.evaluate("log(100)"), 1e-9)
    @Test fun ln() = assertEquals(1.0, e.evaluate("ln(e)"), 1e-9)
    @Test fun pi() = assertEquals(Math.PI, e.evaluate("pi"), 1e-9)
    @Test fun factorial() = assertEquals(120.0, e.evaluate("fact(5)"), 1e-9)
    @Test fun abs() = assertEquals(5.0, e.evaluate("abs(-5)"), 1e-9)
    @Test fun unary() = assertEquals(-3.0, e.evaluate("-(1+2)"), 1e-9)
    @Test fun sciNotation() = assertEquals(1000.0, e.evaluate("1e3"), 1e-9)
    @Test fun modZero() { try { e.evaluate("1%0"); fail() } catch (ex: ArithmeticException) {} }
    @Test fun divZero() { try { e.evaluate("1/0"); fail() } catch (ex: ArithmeticException) {} }
    @Test fun invalid() { try { e.evaluate("2++"); fail() } catch (ex: Exception) {} }
    @Test fun radMode() = assertEquals(Math.sin(1.0), e.evaluate("sin(1)", false), 1e-9)
    @Test fun asinDeg() = assertEquals(30.0, e.evaluate("asin(0.5)"), 1e-9)
    @Test fun logBase() = assertEquals(2.0, e.evaluate("log(8,2)"), 1e-9)
    @Test fun root() = assertEquals(3.0, e.evaluate("root(27,3)"), 1e-9)
}
