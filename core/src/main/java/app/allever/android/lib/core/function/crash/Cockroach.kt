package app.allever.android.lib.core.function.crash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import app.allever.android.lib.core.function.crash.compat.*

/**
 * Created by wanjian on 2017/2/14.
 * https://github.com/android-notes/Cockroach
 */
object Cockroach {
    private var sActivityKiller: IActivityKiller? = null
    private var sExceptionHandler: ExceptionHandler? = null
    private var sInstalled = false //标记位，避免重复安装卸载
    var isSafeMode = false
        private set

    fun install(ctx: Context, exceptionHandler: ExceptionHandler?) {
        if (sInstalled) {
            return
        }
        try {
            //解除 android P 反射限制
            Reflection.unseal(ctx)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
        sInstalled = true
        sExceptionHandler = exceptionHandler
        initActivityKiller()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (sExceptionHandler != null) {
                sExceptionHandler!!.uncaughtExceptionHappened(t, e)
            }
            if (t === Looper.getMainLooper().thread) {
                isChoreographerException(e)
                safeMode()
            }
        }
    }

    /**
     * 替换ActivityThread.mH.mCallback，实现拦截Activity生命周期，直接忽略生命周期的异常的话会导致黑屏，目前
     * 会调用ActivityManager的finishActivity结束掉生命周期抛出异常的Activity
     */
    private fun initActivityKiller() {
        //各版本android的ActivityManager获取方式，finishActivity的参数，token(binder对象)的获取不一样
        if (Build.VERSION.SDK_INT >= 28) {
            sActivityKiller = ActivityKillerV28()
        } else if (Build.VERSION.SDK_INT >= 26) {
            sActivityKiller = ActivityKillerV26()
        } else if (Build.VERSION.SDK_INT == 25 || Build.VERSION.SDK_INT == 24) {
            sActivityKiller = ActivityKillerV24_V25()
        } else if (true && Build.VERSION.SDK_INT <= 23) {
            sActivityKiller = ActivityKillerV21_V23()
        } else if (true && Build.VERSION.SDK_INT <= 20) {
            sActivityKiller = ActivityKillerV15_V20()
        } else if (Build.VERSION.SDK_INT < 15) {
            sActivityKiller = ActivityKillerV15_V20()
        }
        try {
            hookmH()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    @Throws(Exception::class)
    private fun hookmH() {
        val LAUNCH_ACTIVITY = 100
        val PAUSE_ACTIVITY = 101
        val PAUSE_ACTIVITY_FINISHING = 102
        val STOP_ACTIVITY_HIDE = 104
        val RESUME_ACTIVITY = 107
        val DESTROY_ACTIVITY = 109
        val NEW_INTENT = 112
        val RELAUNCH_ACTIVITY = 126
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThread =
            activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null)
        val mhField = activityThreadClass.getDeclaredField("mH")
        mhField.isAccessible = true
        val mhHandler = mhField[activityThread] as Handler
        val callbackField = Handler::class.java.getDeclaredField("mCallback")
        callbackField.isAccessible = true
        callbackField[mhHandler] = Handler.Callback { msg ->
            if (Build.VERSION.SDK_INT >= 28) { //android P 生命周期全部走这
                val EXECUTE_TRANSACTION = 159
                if (msg.what == EXECUTE_TRANSACTION) {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        sActivityKiller!!.finishLaunchActivity(msg)
                        notifyException(throwable)
                    }
                    return@Callback true
                }
                return@Callback false
            }
            when (msg.what) {
                LAUNCH_ACTIVITY -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        sActivityKiller!!.finishLaunchActivity(msg)
                        notifyException(throwable)
                    }
                    return@Callback true
                }
                RESUME_ACTIVITY -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        sActivityKiller!!.finishResumeActivity(msg)
                        notifyException(throwable)
                    }
                    return@Callback true
                }
                PAUSE_ACTIVITY_FINISHING -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        sActivityKiller!!.finishPauseActivity(msg)
                        notifyException(throwable)
                    }
                    return@Callback true
                }
                PAUSE_ACTIVITY -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        sActivityKiller!!.finishPauseActivity(msg)
                        notifyException(throwable)
                    }
                    return@Callback true
                }
                STOP_ACTIVITY_HIDE -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        sActivityKiller!!.finishStopActivity(msg)
                        notifyException(throwable)
                    }
                    return@Callback true
                }
                DESTROY_ACTIVITY -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (throwable: Throwable) {
                        notifyException(throwable)
                    }
                    return@Callback true
                }
            }
            false
        }
    }

    private fun notifyException(throwable: Throwable) {
        if (sExceptionHandler == null) {
            return
        }
        if (isSafeMode) {
            sExceptionHandler!!.bandageExceptionHappened(throwable)
        } else {
            sExceptionHandler!!.uncaughtExceptionHappened(Looper.getMainLooper().thread, throwable)
            safeMode()
        }
    }

    private fun safeMode() {
        isSafeMode = true
        if (sExceptionHandler != null) {
            sExceptionHandler!!.enterSafeMode()
        }
        while (true) {
            try {
                Looper.loop()
            } catch (e: Throwable) {
                isChoreographerException(e)
                if (sExceptionHandler != null) {
                    sExceptionHandler!!.bandageExceptionHappened(e)
                }
            }
        }
    }

    /**
     * view measure layout draw时抛出异常会导致Choreographer挂掉
     *
     *
     * 建议直接杀死app。以后的版本会只关闭黑屏的Activity
     *
     * @param e
     */
    private fun isChoreographerException(e: Throwable?) {
        if (e == null || sExceptionHandler == null) {
            return
        }
        val elements = e.stackTrace ?: return
        for (i in elements.size - 1 downTo -1 + 1) {
            if (elements.size - i > 20) {
                return
            }
            val element = elements[i]
            if ("android.view.Choreographer" == element.className && "Choreographer.java" == element.fileName && "doFrame" == element.methodName) {
                sExceptionHandler!!.mayBeBlackScreen(e)
                return
            }
        }
    }
}