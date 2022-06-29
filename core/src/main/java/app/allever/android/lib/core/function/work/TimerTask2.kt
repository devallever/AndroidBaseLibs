package app.allever.android.lib.core.function.work

import app.allever.android.lib.core.app.App

class TimerTask2(private val delay: Long, loop: Boolean = false, block: () -> Unit) {
    private val task = Runnable {
        block()
        if (loop) {
            start()
        }
    }

    fun start() {
        cancel()
        App.mainHandler.postDelayed(task, delay)
    }

    fun cancel() {
        App.mainHandler.removeCallbacks(task)
    }
}