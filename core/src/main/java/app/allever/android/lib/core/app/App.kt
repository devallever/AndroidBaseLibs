package app.allever.android.lib.core.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

abstract class App : Application() {
    override fun onCreate() {
        super.onCreate()
        init(this)
        init()
    }

    abstract fun init()

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        fun init(context: Context) {
            Companion.context = context.applicationContext
        }
    }

}