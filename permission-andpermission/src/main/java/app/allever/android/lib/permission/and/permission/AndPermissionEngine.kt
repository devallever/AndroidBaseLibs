package app.allever.android.lib.permission.and.permission

import android.content.Context
import android.util.Log
import app.allever.android.lib.core.function.permission.*
import app.allever.android.lib.core.helper.ActivityHelper
import com.yanzhenjie.permission.AndPermission

class AndPermissionEngine : IPermissionEngine {
    private val TAG = AndPermissionEngine::class.java.simpleName

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
        val requestTask = Runnable {
            AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .onGranted {
                    if (it.size == permissions.size) {
                        listener.onAllGranted()
                    }
                }
                .onDenied {
                    //判断是否总是拒绝
                    if (PermissionHelper.hasAlwaysDeniedPermission(
                            ActivityHelper.getTopActivity()!!,
                            it
                        )
                    ) {
                        permissions.map {
                            Log.e(TAG, "总是拒绝权限：$it")
                        }
                        listener.alwaysDenied(it)
                        var jumpSettingDialog = listener.getSettingDialog()
                        if (jumpSettingDialog == null) {
                            jumpSettingDialog = JumpPermissionSettingDialog(context)
                        }
                        jumpSettingDialog.show()
                    } else {
                        permissions.map {
                            Log.e(TAG, "拒绝权限：$it")
                        }
                        listener.onDenied(it)
                    }
                }
                .start()
        }

        if (listener.needShowWhyRequestPermissionDialog()) {
            var dialog = listener.getWhyRequestPermissionDialog()
            if (dialog == null) {
                dialog = WhyRequestPermissionDialog(context, requestTask = requestTask)
            }
            dialog.show()
        } else {
            requestTask.run()
        }
    }

}