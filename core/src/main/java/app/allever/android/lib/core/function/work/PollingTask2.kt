package app.allever.android.lib.core.function.work

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.app.App


/**
 * 轮询需要等待某些条件成立后才能执行的任务
 * 例如等待连接成功后才能做的事
 */
class PollingTask2(
    lifecycleOwner: LifecycleOwner? = null,
    private val interval: Long = 2000,
    private val maxRetry: Int = 3,
    private val condition: () -> Boolean,
    private val execute: () -> Unit = {}
) : DefaultLifecycleObserver {
    private var retryCount = 0

    private val task = Runnable {
        start()
    }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    fun start() {
        if (!condition()) {
            retryCount++
            if (retryCount > maxRetry) {
                return
            }
            App.mainHandler.postDelayed(task, interval)
            return
        }
        execute()
    }

    fun cancel() {
        App.mainHandler.removeCallbacks(task)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
    }
}