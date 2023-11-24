package com.example.quiz

import android.os.CountDownTimer

open class CustomTimer(private val millisInFuture: Long, private val countDownInterval: Long) {

    private var millisUntilFinished: Long = millisInFuture
    private var timer = InternalTimer(this, millisInFuture, countDownInterval)

    private var isRunning = false
    var onTick: ((millisUntilFinished: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null

    private class InternalTimer(
            private val parent: CustomTimer,
            millisInFuture: Long,
            countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        var millisUntilFinished: Long = parent.millisUntilFinished

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }
        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }
    }

    fun pause() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }

    fun resume() {
        if (!isRunning && timer.millisUntilFinished > 0) {
            timer = InternalTimer(this, timer.millisUntilFinished, countDownInterval)
            start()
        }
    }

    fun start() {
        timer.start()
        isRunning = true
    }

    fun restart() {
        timer = InternalTimer(this, millisInFuture, countDownInterval)
        start()
    }
}