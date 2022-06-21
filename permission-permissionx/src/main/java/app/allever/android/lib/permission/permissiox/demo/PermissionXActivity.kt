package app.allever.android.lib.permission.permissiox.demo

import android.Manifest
import android.os.Bundle
import android.view.View
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.permission.PermissionHelper
import app.allever.android.lib.core.function.permission.PermissionListener
import app.allever.android.lib.permission.permissiox.PermissionXEngine
import app.allever.android.lib.permission.permissiox.R

class PermissionXActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_permission_x)

        PermissionHelper.init(PermissionXEngine())

        findViewById<View>(R.id.btnRequestPermission).setOnClickListener {
//            PermissionX.init(this)
//                .permissions()
//                .request { allGranted, grantedList, deniedList ->
//                    if (allGranted) {
//                        log("同意了所有权限")
////                        listener.onAllGranted()
//                    } else {
//                        if (PermissionHelper.hasAlwaysDeniedPermissionOrigin(this, deniedList)) {
//                            deniedList.map {
//                                log("总是拒绝权限：$it")
//                            }
////                            listener.alwaysDenied(deniedList)
//                        } else {
//                            deniedList.map {
//                                log("拒绝权限：$it")
//                            }
////                            listener.onDenied(deniedList)
//                        }
//                    }
//                }
            PermissionHelper.requestPermission(
                this,
                object : PermissionListener {
                    override fun onAllGranted() {
                        toast("全部同意")
                    }

                    override fun onDenied(deniedList: MutableList<String>) {
                        toast("拒绝：${deniedList.size}")
                    }

                    override fun alwaysDenied(deniedList: MutableList<String>) {
                        toast("总是拒绝：${deniedList.size}")
                    }

                    override fun needShowJumpSettingDialog(): Boolean {
                        return true
                    }

                    override fun needShowWhyRequestPermissionDialog(): Boolean {
                        return true
                    }
                },
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO
            )
        }
    }
}