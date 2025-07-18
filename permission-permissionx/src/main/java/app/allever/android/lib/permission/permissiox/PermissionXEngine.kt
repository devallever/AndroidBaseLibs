package app.allever.android.lib.permission.permissiox

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.permission.BasePermissionEngine
import app.allever.android.lib.core.function.permission.PermissionListener
import app.allever.android.lib.core.function.permission.PermissionUtil
import app.allever.android.lib.core.helper.ActivityHelper
import com.permissionx.guolindev.PermissionX

class PermissionXEngine : BasePermissionEngine() {

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        requestPermission(ActivityHelper.getTopActivity() ?: return, listener, *permissions)
    }

    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
        request(context, listener) {
            request(context, listener, *permissions)
        }
    }

    override fun jumpSetting(context: Context, requestCode: Int) {
        PermissionUtil.GoToSetting(context)
    }

    private fun request(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
        val permissionMediator = if (context is FragmentActivity) {
            PermissionX.init(context)
        } else {
            if (context is Fragment) {
                PermissionX.init(context)
            } else {
                PermissionX.init(ActivityHelper.getTopActivity() as? FragmentActivity)
            }
        }
        permissionMediator.permissions(*permissions)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    log("同意了所有权限")
                    listener.onAllGranted()
                } else {
                    handleDenied(permissions, context, listener, deniedList)
                }
            }
    }

    private fun request(
        context: Context,
        listener: PermissionListener,
        permissions: List<String>
    ) {
        val permissionMediator = if (context is FragmentActivity) {
            PermissionX.init(context)
        } else {
            if (context is Fragment) {
                PermissionX.init(context)
            } else {
                PermissionX.init(ActivityHelper.getTopActivity() as? FragmentActivity)
            }
        }
        permissionMediator.permissions(permissions)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    log("同意了所有权限")
                    listener.onAllGranted()
                } else {
                    handleDenied(permissions.toTypedArray(), context, listener, deniedList)
                }
            }
    }
}