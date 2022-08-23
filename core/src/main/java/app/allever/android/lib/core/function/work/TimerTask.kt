package app.allever.android.lib.core.function.work

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.app.App

/**
 * 定时任务
 */
abstract class TimerTask(lifecycleOwner: LifecycleOwner? = null) : DefaultLifecycleObserver {

    private val task = Runnable {
        execute()
        if (loop()) {
            start()
        }
    }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
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

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
    }
}