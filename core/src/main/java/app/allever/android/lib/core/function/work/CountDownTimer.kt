package app.allever.android.lib.core.function.work

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.ext.log

/**
 * 倒计时定时器
 * @param delay 延迟多少毫秒后执行, 需要外部计算时间点和当前的时间差,
 * 启动后 倒计时
 */
class CountDownTimer(
    lifecycleOwner: LifecycleOwner? = null,
    private val delay: Long,
    block: () -> Unit
) : DefaultLifecycleObserver {

    private val mHandler = Handler(Looper.getMainLooper())

    /**
     * 剩余时间
     */
    private var leftTime = delay

    private val task = Runnable {
        leftTime -= 1000
        if (leftTime <= 0) {
            block()
            return@Runnable
        }
        log("倒计时定时器：总时间 $delay, 剩余时间:$leftTime")
        start()
    }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    fun start() {
        cancel()
        mHandler.postDelayed(task, 1000)
    }

    fun cancel() {
        mHandler.removeCallbacks(task)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
    }
}