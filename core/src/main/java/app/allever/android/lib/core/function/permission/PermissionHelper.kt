package app.allever.android.lib.core.function.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.AppOpsManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.ActivityHelper
import java.util.*

object PermissionHelper : IPermissionEngine {

    const val RC_CODE_JUMP_SETTING = 1000

    private var mEngine: IPermissionEngine = DefaultPermissionEngine

    fun init(engine: IPermissionEngine) {
        mEngine = engine
    }


    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
        mEngine.requestPermission(context, listener, *permissions)
    }

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        mEngine.requestPermission(listener, *permissions)
    }

    override fun jumpSetting(context: Context, requestCode: Int) {
        mEngine.jumpSetting(context, requestCode)
    }


    override fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        listener: PermissionListener
    ) {
        mEngine.handlePermissionResult(requestCode, permissions, grantResults, listener)
    }

    fun hasPermission(vararg permissions: String): Boolean =
        PermissionCompat.hasPermission(App.context, *permissions)


    fun hasAlwaysDeny(vararg permissions: String): Boolean =
        PermissionCompat.hasAlwaysDeniedPermission(
            ActivityHelper.getTopActivity() as FragmentActivity,
            *permissions
        )

    @Deprecated("")
    fun gotoSetting() {
        PermissionUtil.GoToSetting(ActivityHelper.getTopActivity())
    }

    private fun hasPermission(context: Context, vararg permissions: String): Boolean {
        return hasPermission(context, Arrays.asList(*permissions))
    }

    private fun hasPermission(context: Context, permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            var result = ContextCompat.checkSelfPermission(context, permission)
            if (result == PackageManager.PERMISSION_DENIED) {
                return false
            }

            val op = AppOpsManagerCompat.permissionToOp(permission)
            if (TextUtils.isEmpty(op)) {
                continue
            }
            result = AppOpsManagerCompat.noteProxyOp(context, op!!, context.packageName)
            if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                return false
            }
        }
        return true
    }

    fun hasAlwaysDeniedPermission(activity: Activity, vararg deniedPermissions: String): Boolean {
        return hasAlwaysDeniedPermission(activity, Arrays.asList(*deniedPermissions))
    }

    fun hasAlwaysDeniedPermission(activity: Activity, deniedPermissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }

        if (deniedPermissions.isEmpty()) {
            return false
        }

        for (permission in deniedPermissions) {
            val rationale = activity.shouldShowRequestPermissionRationale(permission)
            if (!rationale) {
                return true
            }
        }
        return false
    }
}