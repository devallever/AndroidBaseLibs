package app.allever.android.lib.core.helper

import android.content.Context
import android.os.Vibrator
import app.allever.android.lib.core.app.App

object VibratorHelper {
    private val mVibrator by lazy {
        App.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun start() {
        mVibrator.vibrate(longArrayOf(50, 50), -1)
    }

    fun cancel() {
        mVibrator.cancel()
    }
}