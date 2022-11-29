package app.allever.android.lib.core.function.work

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log

class TimerTask2(
    lifecycleOwner: LifecycleOwner? = null,
    private val delay: Long,
    loop: Boolean = false,
    block: () -> Unit
) : DefaultLifecycleObserver {
    private val task = Runnable {
        block()
        if (loop) {
            log("开始: 间隔 = $delay")
            start()
        }
    }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    fun start() {
        cancel()
        App.mainHandler.postDelayed(task, delay)
    }

    fun cancel() {
        App.mainHandler.removeCallbacks(task)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
    }
}