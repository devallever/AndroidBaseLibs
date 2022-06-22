package app.allever.android.lib.core.function.permission

import android.content.Context
import android.util.Log

abstract class BasePermissionEngine: IPermissionEngine {

    private val TAG = "Permission"

    fun request(context: Context, listener: PermissionListener, block: () -> Unit) {
        if (listener.needShowWhyRequestPermissionDialog()) {
            var dialog = listener.getWhyRequestPermissionDialog()
            if (dialog == null) {
                dialog = WhyRequestPermissionDialog(context, requestTask = { block() })
            }
            dialog.show()
        } else {
            block()
        }
    }

    fun handleAllGranted(permissions: Array<out String>, it: List<String>, listener: PermissionListener) {
        if (it.size == permissions.size) {
            listener.onAllGranted()
        }
    }

    fun handleDenied(permissions: Array<out String>, context: Context, listener: PermissionListener, deniedList: MutableList<String>) {
        //判断是否总是拒绝
        if (PermissionHelper.hasAlwaysDeniedPermissionOrigin(
                context,
                deniedList
            )
        ) {
            permissions.map {
                Log.e(TAG, "总是拒绝权限：$it")
            }
            listener.alwaysDenied(deniedList)
            var jumpSettingDialog = listener.getSettingDialog()
            if (jumpSettingDialog == null) {
                jumpSettingDialog = JumpPermissionSettingDialog(context)
            }
            jumpSettingDialog.show()
        } else {
            permissions.map {
                Log.e(TAG, "拒绝权限：$it")
            }
            listener.onDenied(deniedList)
        }
    }
}