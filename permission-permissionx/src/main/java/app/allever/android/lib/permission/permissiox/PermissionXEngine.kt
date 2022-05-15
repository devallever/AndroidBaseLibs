package app.allever.android.lib.permission.permissiox

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.permission.*
import app.allever.android.lib.core.helper.ActivityHelper
import com.permissionx.guolindev.PermissionX

class PermissionXEngine : IPermissionEngine {

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        request(ActivityHelper.getTopActivity()!!, listener, *permissions)
    }

    override fun jumpSetting(context: Context, requestCode: Int) {
        PermissionUtil.GoToSetting(ActivityHelper.getTopActivity())
    }

    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
        request(context, listener, *permissions)
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
                    if (PermissionCompat.hasAlwaysDeniedPermission(
                            ActivityHelper.getTopActivity()!!,
                            deniedList
                        )
                    ) {
                        permissions.map {
                            log("总是拒绝权限：$it")
                        }
                        listener.alwaysDenied(deniedList)
                        var jumpSettingDialog = listener.getSettingDialog()
                        if (jumpSettingDialog == null) {
                            jumpSettingDialog = JumpPermissionSettingDialog(context)
                        }
                        jumpSettingDialog.show()
                    } else {
                        permissions.map {
                            log("拒绝权限：$it")
                        }
                        listener.onDenied(deniedList)
                    }
                }
            }
    }
}