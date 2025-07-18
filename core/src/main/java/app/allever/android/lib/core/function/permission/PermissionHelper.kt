package app.allever.android.lib.core.function.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.AppOpsManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.helper.ActivityHelper

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
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        mEngine.handlePermissionResult(context, requestCode, permissions, grantResults)
    }

    fun hasPermissionOrigin(context: Context?, permissions: List<String>): Boolean {
        context ?: return false
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

    fun requestPermissionOrigin(obj: Any, requestCode: Int, vararg permissions: String) {
        val activity = when (obj) {
            is Activity -> obj
            is Fragment -> obj.activity
            else -> ActivityHelper.getTopActivity()
        }
        ActivityCompat.requestPermissions(activity ?: return, permissions, requestCode)
    }

    fun requestPermissionOrigin(obj: Any, requestCode: Int,  permissions: MutableList<String>) {
        val activity = when (obj) {
            is Activity -> obj
            is Fragment -> obj.activity
            else -> ActivityHelper.getTopActivity()
        }
        ActivityCompat.requestPermissions(activity ?: return,
            permissions.toTypedArray(), requestCode)
    }

    fun gotoSettingOrigin(context: Context? = null) {
        PermissionUtil.GoToSetting(context ?: ActivityHelper.getTopActivity())
    }

    fun hasAlwaysDeniedPermissionOrigin(
        context: Context? = null,
        deniedPermissions: List<String>
    ): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }

        if (deniedPermissions.isEmpty()) {
            return false
        }

        val activity = when (context) {
            is Activity -> {
                context
            }
            is Fragment -> {
                context.activity
            }
            else -> {
                null
            }
        }
        for (permission in deniedPermissions) {
            val rationale = activity?.shouldShowRequestPermissionRationale(permission)
            if (rationale == false) {
                return true
            }
        }
        return false
    }
}