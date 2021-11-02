package com.github.dakyskye.calkulator

import android.widget.TextView

private const val ZERO = "0"
private const val COMMA = ","
private const val DOT = "."
private const val HYPHEN = "-"
private const val MAX_TEXT_LENGTH = 11
private const val SEPARATION_INDEX = 3

data class ClearTextView(
    val textView: TextView,
    val baseText: String,
    val nextText: String
)

class App(
    private val resultTextView: TextView,
    private val clearTextView: ClearTextView
) {
    private var state = State()

    init {
        resultTextView.text = ZERO
    }

    fun clear() {
        state = State()
        resultTextView.text = ZERO
        clearTextView.textView.text = clearTextView.baseText
    }

    private fun display(text: String) {
        var toApply = text.replace(COMMA, DOT).removeSuffix("$DOT$ZERO")

        val hyphenPrefix = if (toApply.startsWith(HYPHEN)) {
            toApply = toApply.removePrefix(HYPHEN)
            HYPHEN
        } else {
            ""
        }

        val dotIndex = toApply.indexOfFirst { it.toString() == DOT }
        when (dotIndex) {
            MAX_TEXT_LENGTH - 1 -> {
                if (toApply.length > MAX_TEXT_LENGTH + 1) {
                    toApply = toApply.take(MAX_TEXT_LENGTH + 1)
                }
            }
            MAX_TEXT_LENGTH - 2 -> {
                if (toApply.length > MAX_TEXT_LENGTH + 1) {
                    toApply = toApply.take(MAX_TEXT_LENGTH + 1)
                }
            }
            else -> toApply = toApply.take(MAX_TEXT_LENGTH)
        }

        toApply = toApply.replace(" ", "")

        fun separate(data: String): ArrayList<String> {
            var dataMut = data
            val chunks: ArrayList<String> = ArrayList()
            while (dataMut.length > SEPARATION_INDEX) {
                chunks.add(dataMut.take(SEPARATION_INDEX))
                dataMut = dataMut.drop(SEPARATION_INDEX)
            }
            chunks.add(dataMut)
            return chunks
        }

        if (toApply.contains(DOT)) {
            state.isInteger = false
        }

        toApply = hyphenPrefix + if (state.isInteger) {
            val chunks = separate(toApply.reversed())
            chunks.joinToString(" ").reversed()
        } else {
            if (dotIndex < 1) {
                toApply
            } else {
                val taken = toApply.take(dotIndex - 1)
                val chunks = separate(taken.reversed())
                toApply.replace(taken, chunks.joinToString(" ").reversed())
            }
        }.replace(DOT, COMMA)

        resultTextView.text = toApply
    }

    fun display(number: Any?) {
        when (number) {
            is String -> display(number)
            is Double -> display(number.toString())
        }
    }

    fun calculate(): Double? {
        return try {
            val current = getCurrentTransformed().replace(" ", "").toDouble()
            val ret = state.operand?.let { state.operator?.operate(it, current) } ?: return null
            state = State()
            ret
        } catch (e: Exception) {
            null
        }
    }

    fun reverseCurrentNumber() {
        val text = resultTextView.text

        display(
            if (text.startsWith(HYPHEN)) {
                text.removePrefix(HYPHEN)
            } else {
                "$HYPHEN$text"
            }
        )
    }

    fun percentageOfCurrentNumber(): Double? {
        return try {
            getCurrentTransformed().toDouble() / 100
        } catch (e: Exception) {
            null
        }
    }

    fun handleNumberButtonClick(number: String) {
        var current = getCurrentTransformed()

        if (state.expectingOperand) {
            state.expectingOperand = false
            state.isInteger = true

            if (number == COMMA) {
                display(ZERO + COMMA)
            } else {
                display(number)
            }

            return
        }

        if (current == ZERO) {
            when (number) {
                ZERO -> return
                COMMA -> {
                }
                else -> current = ""
            }
        }

        if (number == COMMA) {
            if (!state.isInteger) {
                return
            }

            state.isInteger = false
        }

        clearTextView.textView.text = clearTextView.nextText

        display(current + number)
    }

    fun handleOperatorButtonClick(sign: String) {
        val operator = Operator.values().find { it.sign == sign } ?: return

        display(calculate())

        state.operator = operator
        state.operand = getCurrentTransformed().replace(" ", "").toDouble()

        state.expectingOperand = true
    }

    private fun getCurrentTransformed(): String {
        return resultTextView.text.toString().replace(COMMA, DOT)
    }
}

private data class State(
    var isInteger: Boolean = true,
    var expectingOperand: Boolean = false,
    var operand: Double? = null,
    var operator: Operator? = null
)