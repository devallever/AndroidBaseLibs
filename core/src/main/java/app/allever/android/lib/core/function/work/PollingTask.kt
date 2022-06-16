package app.allever.android.lib.core.function.work

import app.allever.android.lib.core.app.App


/**
 * 轮询需要等待某些条件成立后才能执行的任务
 * 例如等待连接成功后才能做的事
 */
abstract class PollingTask {

    abstract fun condition(): Boolean
    abstract fun execute()
    protected open fun interval(): Long = 2000
    private val task = Runnable {
        start()
    }

    fun start() {
        if (!condition()) {
            App.mainHandler.postDelayed(task, interval())
            return
        }
        execute()
    }

    fun cancel() {
        App.mainHandler.removeCallbacks(task)
    }
}