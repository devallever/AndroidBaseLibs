package app.allever.android.lib.permission.and.permission

import android.Manifest
import androidx.fragment.app.FragmentActivity
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.core.log
import app.allever.android.lib.permission.core.IPermissionEngine
import app.allever.android.lib.permission.core.PermissionCompat
import app.allever.android.lib.permission.core.PermissionListener
import com.yanzhenjie.permission.AndPermission

class AndPermissionEngine : IPermissionEngine {

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        AndPermission.with(ActivityHelper.getTopActivity())
            .runtime()
            .permission(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
            .onGranted {
                if (it.size == permissions.size) {
                    listener.onAllGranted()
                }
            }
            .onDenied {
                //判断是否总是拒绝
                if (PermissionCompat.hasAlwaysDeniedPermission(
                        ActivityHelper.getTopActivity()!!,
                        it
                    )
                ) {
                    permissions.map {
                        log("总是拒绝权限：$it")
                    }
                    listener.alwaysDenied(it)
                } else {
                    permissions.map {
                        log("拒绝权限：$it")
                    }
                    listener.onDenied(it)
                }
            }
            .start()
    }


}