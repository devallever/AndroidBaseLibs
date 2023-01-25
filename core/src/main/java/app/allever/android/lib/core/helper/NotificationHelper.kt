package app.allever.android.lib.core.helper

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.allever.android.lib.core.app.App

object NotificationHelper {
    private val notificationManagerCompat by lazy {
        NotificationManagerCompat.from(App.context)
    }

    val manager = notificationManagerCompat


    fun notify(id: Int, @NonNull notification: Notification) {
        manager.notify(id, notification)
    }

    fun notify(
        context: Context,
        channel: String,
        notificationId: Int,
        block: (builder: NotificationCompat.Builder) -> Unit
    ) {
        val builder = NotificationCompat.Builder(context, channel)
        block.invoke(builder)
        val notification = builder.build()
        manager.notify(notificationId, notification)
    }

    fun jumpSetting() {
        try {
            App.context.let {
                val localIntent = Intent()
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, it.packageName)
                } else {
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts("package", it.packageName, null)
                }

                it.startActivity(localIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}