package com.github.dakyskye.calkulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app = App(
            findViewById(R.id.resultTextView),
            ClearTextView(
                findViewById(R.id.clearTextView),
                getString(R.string.text_clear_base),
                getString(R.string.text_clear)
            )
        )
    }

    private fun flashView(view: View) {
        val animation = AlphaAnimation(0.7F, 1F)
        animation.duration = 400
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = 0
        view.startAnimation(animation)
    }

    fun onNumberButtonClick(clickedView: View) {
        if (clickedView !is TextView) {
            return
        }

        flashView(clickedView)

        app.display(app.handleNumberButtonClick(clickedView.text.toString()))
    }

    fun onClearButtonClick(clickedView: View) {
        flashView(clickedView)

        app.clear()
    }

    fun onReverseNumberButtonClick(clickedView: View) {
        flashView(clickedView)

        app.reverseCurrentNumber()
    }

    fun onPercentageButtonClick(clickedView: View) {
        flashView(clickedView)

        app.display(app.percentageOfCurrentNumber())
    }

    fun onOperatorButtonClick(clickedView: View) {
        if (clickedView !is TextView) {
            return
        }

        flashView(clickedView)

        app.handleOperatorButtonClick(clickedView.text.toString())
    }

    fun onEqualsButtonClick(clickedView: View) {
        flashView(clickedView)

        app.display(app.calculate())
    }
}