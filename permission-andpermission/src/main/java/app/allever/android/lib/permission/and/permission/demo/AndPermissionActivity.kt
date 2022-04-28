package app.allever.android.lib.permission.and.permission.demo

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.toast
import app.allever.android.lib.permission.and.permission.AndPermissionEngine
import app.allever.android.lib.permission.and.permission.R
import app.allever.android.lib.permission.core.PermissionHelper
import app.allever.android.lib.permission.core.PermissionListener

class AndPermissionActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_and_permission)

        PermissionHelper.init(AndPermissionEngine())

        findViewById<View>(R.id.btnRequestPermission).setOnClickListener {
            PermissionHelper.requestPermission( object : PermissionListener {
                override fun onAllGranted() {
                    toast("全部同意")
                }

                override fun needShowWhyRequestPermissionDialog(): Boolean {
                    return true
                }
            }, Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE)
        }
    }
}