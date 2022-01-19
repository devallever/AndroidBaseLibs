package app.allever.android.lib.permission.permissiox

import androidx.fragment.app.FragmentActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.core.log
import app.allever.android.lib.permission.core.IPermissionEngine
import app.allever.android.lib.permission.core.PermissionCompat
import app.allever.android.lib.permission.core.PermissionListener
import com.permissionx.guolindev.PermissionX

class PermissionXEngine : IPermissionEngine {

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        val activity = ActivityHelper.getTopActivity() as FragmentActivity
        PermissionX.init(activity)
            .permissions(*permissions)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    log("同意了所有权限")
                    listener.onAllGranted()
                } else {
                    if (PermissionCompat.hasAlwaysDeniedPermission(activity, deniedList)) {
                        permissions.map {
                            log("总是拒绝权限：$it")
                        }
                        listener.alwaysDenied(deniedList)
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