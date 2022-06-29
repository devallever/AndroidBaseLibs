package app.allever.android.lib.core.function.work

import app.allever.android.lib.core.app.App

/**
 * 定时任务
 */
abstract class TimerTask {

    private val task = Runnable {
        execute()
        if (loop()) {
            start()
        }
    }

    fun start() {
        cancel()
        App.mainHandler.postDelayed(task, delay())
    }

    fun cancel() {
        App.mainHandler.removeCallbacks(task)
    }

    protected open fun loop(): Boolean = false
    abstract fun delay(): Long
    abstract fun execute()
}