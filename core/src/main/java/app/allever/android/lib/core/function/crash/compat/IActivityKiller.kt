package app.allever.android.lib.core.function.crash.compat

import android.os.Message

/**
 * Created by wanjian on 2018/5/24.
 */
interface IActivityKiller {
    fun finishLaunchActivity(message: Message)
    fun finishResumeActivity(message: Message)
    fun finishPauseActivity(message: Message)
    fun finishStopActivity(message: Message)
}