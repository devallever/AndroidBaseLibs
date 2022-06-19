package app.allever.android.lib.core.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import app.allever.android.lib.core.BuildConfig
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.ext.toastDebug
import app.allever.android.lib.core.function.crash.Cockroach
import app.allever.android.lib.core.function.crash.ExceptionHandler
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper

abstract class App : Application() {
    override fun onCreate() {
        super.onCreate()
        init(this)
        init()
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null)

        initCrashHandler()
    }

    abstract fun init()

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var app: Application

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var mainHandler: Handler

        //        val mainHandler : Handler by  lazy {
//            Handler(Looper.getMainLooper())
//        }
        fun init(context: Context) {
            Companion.context = context.applicationContext
            mainHandler = Handler(Looper.getMainLooper())
            app = context as Application
        }
    }

    private val mExceptionHandler: ExceptionHandler by lazy {
        object : ExceptionHandler() {
            override fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
                logE("CrashHandler onUncaughtExceptionHappened")
                logE("CrashHandler", "--->onUncaughtExceptionHappened:$thread<---")
                toastDebug(throwable?.message)
            }

            override fun onBandageExceptionHappened(throwable: Throwable?) {
                toastDebug(throwable?.message)
                logE("CrashHandler onBandageExceptionHappened")
//                throwable.printStackTrace() //打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
            }

            override fun onEnterSafeMode() {
                toastDebug("onEnterSafeMode")
                logE("CrashHandler onEnterSafeMode")
            }

            override fun onMayBeBlackScreen(e: Throwable?) {
                toastDebug("onMayBeBlackScreen")
                logE("CrashHandler onMayBeBlackScreen")
//                val thread: Thread = Looper.getMainLooper().getThread()
//                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:$thread<---")
//                //黑屏时建议直接杀死app
//                sysExcepHandler.uncaughtException(thread, RuntimeException("black screen"))
            }
        }
    }

    protected open fun crashHandler(): ExceptionHandler? = mExceptionHandler

    private fun initCrashHandler() {
        if (BuildConfig.DEBUG) {
            return
        }

        Cockroach.install(this, crashHandler())

    }

}