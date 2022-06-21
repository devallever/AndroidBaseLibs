package app.allever.android.lib.permission.and.permission.demo

//import app.allever.android.lib.core.ext.toast
import android.Manifest
import android.app.Dialog
import android.os.Bundle
import android.view.View
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.permission.PermissionHelper
import app.allever.android.lib.core.function.permission.PermissionListener
import app.allever.android.lib.permission.and.permission.AndPermissionEngine
import app.allever.android.lib.permission.and.permission.R

class AndPermissionActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_and_permission)

        PermissionHelper.init(AndPermissionEngine())

        findViewById<View>(R.id.btnRequestPermission).setOnClickListener {
            PermissionHelper.requestPermission(
                object : PermissionListener {
                    override fun onAllGranted() {
                        toast("全部同意")
                    }

                    override fun needShowWhyRequestPermissionDialog(): Boolean {
                        return true
                    }

                    override fun needShowJumpSettingDialog(): Boolean {
                        return true
                    }

                    override fun getWhyRequestPermissionDialog(): Dialog? {
                        return super.getWhyRequestPermissionDialog()
                    }
                }, Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE
            )
        }
    }
}