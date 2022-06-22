package app.allever.android.lib.permission.and.permission

import android.content.Context
import app.allever.android.lib.core.function.permission.BasePermissionEngine
import app.allever.android.lib.core.function.permission.PermissionListener
import app.allever.android.lib.core.helper.ActivityHelper
import com.yanzhenjie.permission.AndPermission

class AndPermissionEngine : BasePermissionEngine() {
    /**
     * 解决哪些不在栈顶Activity请求的权限
     */
    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
        request(context, listener, *permissions)
    }

    /**
     * 默认使用栈顶Activity作为context，当Activity销毁后，同意或拒绝会崩溃
     */
    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        request(ActivityHelper.getTopActivity() ?: return, listener, *permissions)
    }

    override fun jumpSetting(context: Context, requestCode: Int) {
        AndPermission.with(context)
            .runtime()
            .setting()
            .start(requestCode)
    }

    private fun request(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
        request(context, listener) {
            AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .onGranted {
                    handleAllGranted(permissions, it, listener)
                }
                .onDenied {
                    handleDenied(permissions, context, listener, it)
                }
                .start()
        }
    }


}