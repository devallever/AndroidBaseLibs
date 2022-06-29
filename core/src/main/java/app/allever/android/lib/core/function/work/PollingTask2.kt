package app.allever.android.lib.core.function.work

import app.allever.android.lib.core.app.App


/**
 * 轮询需要等待某些条件成立后才能执行的任务
 * 例如等待连接成功后才能做的事
 */
class PollingTask2(
    private val interval: Long = 2000,
    private val condition: () -> Boolean,
    private val execute: () -> Unit = {}
) {
    private val task = Runnable {
        start()
    }

    fun start() {
        if (!condition()) {
            App.mainHandler.postDelayed(task, interval)
            return
        }
        execute()
    }

    fun cancel() {
        App.mainHandler.removeCallbacks(task)
    }
}