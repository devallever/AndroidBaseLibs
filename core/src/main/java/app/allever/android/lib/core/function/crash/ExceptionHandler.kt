package app.allever.android.lib.core.function.crash

import java.lang.Thread

/**
 * Created by wanjian on 2018/5/29.
 */
abstract class ExceptionHandler {
    fun uncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
        try { //捕获监听中异常，防止使用方代码抛出异常时导致的反复调用
            onUncaughtExceptionHappened(thread, throwable)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun bandageExceptionHappened(throwable: Throwable?) {
        try { //捕获监听中异常，防止使用方代码抛出异常时导致的反复调用
            onBandageExceptionHappened(throwable)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun enterSafeMode() {
        try {
            onEnterSafeMode()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    fun mayBeBlackScreen(e: Throwable?) {
        try {
            onMayBeBlackScreen(e)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    /**
     * 子线程抛出异常时始终调用该方法。主线程只有第一次抛出异常时才会调用该方法，该方法中到的throwable都会上报到bugly。以后主线程的异常只调用 [.onBandageExceptionHappened]
     *
     * @param thread
     * @param throwable
     */
    protected abstract fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?)

    /**
     * 当原本导致app崩溃的主线程异常发生后，主线程再次抛出导致app崩溃异常时会调用该方法。（自己try catch住的异常不会导致app崩溃）
     * （该方法中到的throwable不会上报到bugly，也无需上报到bugly，因为本次异常可能是由于第一次主线程异常时app没有崩溃掉才发生的，只要修复了bug就不会发生该异常了)
     *
     * @param throwable 主线程的异常
     */
    protected abstract fun onBandageExceptionHappened(throwable: Throwable?)
    protected abstract fun onEnterSafeMode()
    protected open fun onMayBeBlackScreen(e: Throwable?) {}
}