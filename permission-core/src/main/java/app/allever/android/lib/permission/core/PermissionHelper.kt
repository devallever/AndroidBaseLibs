package app.allever.android.lib.permission.core

import androidx.fragment.app.FragmentActivity
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.ActivityHelper

object PermissionHelper : IPermissionEngine {
    private lateinit var mEngine: IPermissionEngine

    fun init(engine: IPermissionEngine) {
        mEngine = engine
    }

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        mEngine.requestPermission(listener, *permissions)
    }

    fun hasPermission(vararg permissions: String): Boolean =
        PermissionCompat.hasPermission(App.context, *permissions)


    fun hasAlwaysDeny(vararg permissions: String): Boolean =
        PermissionCompat.hasAlwaysDeniedPermission(
            ActivityHelper.getTopActivity() as FragmentActivity,
            *permissions
        )

    fun gotoSetting() {
        PermissionUtil.GoToSetting(ActivityHelper.getTopActivity())
    }
}