package com.github.dakyskye.calkulator

interface IOperator {
    fun operate(operand1: Double, operand2: Double): Double
}

enum class Operator(val sign: String) : IOperator {
    ADD("+") {
        override fun operate(operand1: Double, operand2: Double): Double = operand1 + operand2
    },
    SUBTRACT("-") {
        override fun operate(operand1: Double, operand2: Double): Double = operand1 - operand2
    },
    MULTIPLY("×") {
        override fun operate(operand1: Double, operand2: Double): Double = operand1 * operand2
    },
    DIVIDE("÷") {
        override fun operate(operand1: Double, operand2: Double): Double = operand1 / operand2
    }
}
