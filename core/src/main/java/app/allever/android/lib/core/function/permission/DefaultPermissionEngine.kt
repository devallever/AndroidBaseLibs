package app.allever.android.lib.core.function.permission

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.helper.ActivityHelper

object DefaultPermissionEngine : IPermissionEngine {
    private const val RC_CODE = 0x01
    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {

        val requestTask = Runnable {
            when (context) {
                is Activity -> {
                    PermissionCompat.requestPermission(context, RC_CODE, *permissions)
                }
                is Fragment -> {
                    PermissionCompat.requestPermission(
                        context.requireActivity(),
                        RC_CODE,
                        *permissions
                    )
                }
                else -> {
                    PermissionCompat.requestPermission(
                        ActivityHelper.getTopActivity()!!,
                        RC_CODE,
                        *permissions
                    )
                }
            }
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

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {

        val requestTask = Runnable {
            PermissionCompat.requestPermission(
                ActivityHelper.getTopActivity()!!,
                RC_CODE,
                *permissions
            )
        }

        if (listener.needShowWhyRequestPermissionDialog()) {
            var dialog = listener.getWhyRequestPermissionDialog()
            if (dialog == null) {
                dialog = WhyRequestPermissionDialog(
                    ActivityHelper.getTopActivity()!!,
                    requestTask = requestTask
                )
            }
            dialog.show()
        } else {
            requestTask.run()
        }
    }

    override fun jumpSetting(context: Context, requestCode: Int) {
        PermissionUtil.GoToSetting(ActivityHelper.getTopActivity())
    }

    override fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        listener: PermissionListener
    ) {
        if (PermissionCompat.hasPermission(ActivityHelper.getTopActivity()!!, *permissions)) {
            listener.onAllGranted()
        } else {

            if (PermissionHelper.hasAlwaysDeny(*permissions)) {
                //总是拒绝
                listener.alwaysDenied(permissions.toMutableList())

                var jumpSettingDialog = listener.getSettingDialog()
                if (jumpSettingDialog == null) {
                    jumpSettingDialog =
                        JumpPermissionSettingDialog(ActivityHelper.getTopActivity()!!)
                }
                jumpSettingDialog.show()

            } else {
                //拒绝
                val deniedList = mutableListOf<String>()
                permissions.map {
                    if (!PermissionCompat.hasPermission(ActivityHelper.getTopActivity()!!, it)) {
                        deniedList.add(it)
                    }
                    log("拒绝权限：$it")
                }
                listener.onDenied(deniedList)
            }
        }
    }
}