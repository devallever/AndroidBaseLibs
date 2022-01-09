package app.allever.android.lib.widget

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Widget {
    lateinit var context: Context
    fun init(context: Context) {
        this.context = context
    }
}