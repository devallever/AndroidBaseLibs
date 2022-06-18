package app.allever.android.lib.core.function.crash.compat

import app.allever.android.lib.core.function.crash.compat.IActivityKiller
import android.os.IBinder
import java.lang.Exception
import kotlin.Throws
import android.content.Intent
import android.app.Activity
import android.os.Message

/**
 * Created by wanjian on 2018/5/24.
 *
 *
 * android 7.1.1
 *
 *
 * ActivityManagerNative.getDefault().finishActivity(mToken, resultCode, resultData, finishTask))
 */
class ActivityKillerV24_V25 : IActivityKiller {
    override fun finishLaunchActivity(message: Message) {
        try {
            val activityClientRecord = message.obj
            val tokenField = activityClientRecord.javaClass.getDeclaredField("token")
            tokenField.isAccessible = true
            val binder = tokenField[activityClientRecord] as IBinder
            finish(binder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun finishResumeActivity(message: Message) {
        finishSomeArgs(message)
    }

    override fun finishPauseActivity(message: Message) {
        finishSomeArgs(message)
    }

    override fun finishStopActivity(message: Message) {
        finishSomeArgs(message)
    }

    private fun finishSomeArgs(message: Message) {
        try {
            val someArgs = message.obj
            val arg1Field = someArgs.javaClass.getDeclaredField("arg1")
            arg1Field.isAccessible = true
            val binder = arg1Field[someArgs] as IBinder
            finish(binder)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun finish(binder: IBinder) {

        /*

         ActivityManagerNative.getDefault()
                 .finishActivity(r.token, Activity.RESULT_CANCELED, null, Activity.DONT_FINISH_TASK_WITH_ACTIVITY);
         */
        val activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative")
        val getDefaultMethod = activityManagerNativeClass.getDeclaredMethod("getDefault")
        val activityManager = getDefaultMethod.invoke(null)
        val finishActivityMethod = activityManager.javaClass.getDeclaredMethod(
            "finishActivity",
            IBinder::class.java,
            Int::class.javaPrimitiveType,
            Intent::class.java,
            Int::class.javaPrimitiveType
        )
        val DONT_FINISH_TASK_WITH_ACTIVITY = 0
        finishActivityMethod.invoke(
            activityManager,
            binder,
            Activity.RESULT_CANCELED,
            null,
            DONT_FINISH_TASK_WITH_ACTIVITY
        )
    }
}