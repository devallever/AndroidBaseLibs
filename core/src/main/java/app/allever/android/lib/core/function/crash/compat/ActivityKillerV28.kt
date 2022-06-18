package app.allever.android.lib.core.function.crash.compat

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.os.IBinder
import android.os.Message

class ActivityKillerV28 : IActivityKiller {
    override fun finishLaunchActivity(message: Message) {
        try {
            tryFinish1(message)
            return
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
        try {
            tryFinish2(message)
            return
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
        try {
            tryFinish3(message)
            return
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    @Throws(Throwable::class)
    private fun tryFinish1(message: Message) {
        val clientTransaction = message.obj as ClientTransaction
        val binder = clientTransaction.activityToken
        finish(binder)
    }

    @Throws(Throwable::class)
    private fun tryFinish3(message: Message) {
        val clientTransaction = message.obj
        val mActivityTokenField = clientTransaction.javaClass.getDeclaredField("mActivityToken")
        val binder = mActivityTokenField[clientTransaction] as IBinder
        finish(binder)
    }

    @Throws(Throwable::class)
    private fun tryFinish2(message: Message) {
        val clientTransaction = message.obj
        val getActivityTokenMethod =
            clientTransaction.javaClass.getDeclaredMethod("getActivityToken")
        val binder = getActivityTokenMethod.invoke(clientTransaction) as IBinder
        finish(binder)
    }

    override fun finishResumeActivity(message: Message) {}
    override fun finishPauseActivity(message: Message) {}
    override fun finishStopActivity(message: Message) {}
    @Throws(Exception::class)
    private fun finish(binder: IBinder?) {
        val getServiceMethod = ActivityManager::class.java.getDeclaredMethod("getService")
        val activityManager = getServiceMethod.invoke(null)
        val finishActivityMethod = activityManager.javaClass.getDeclaredMethod(
            "finishActivity",
            IBinder::class.java,
            Int::class.javaPrimitiveType,
            Intent::class.java,
            Int::class.javaPrimitiveType
        )
        finishActivityMethod.isAccessible = true
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