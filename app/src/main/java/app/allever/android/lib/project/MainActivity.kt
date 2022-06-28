package app.allever.android.lib.project

import android.os.Bundle
import android.view.View
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.function.businessinterceptor.demo.BusinessInterceptorActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.mvp.demo.MvpActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.demo.MvvmActivity
import app.allever.android.lib.network.demo.NetworkActivity
import app.allever.android.lib.permission.permissiox.demo.PermissionXActivity
import app.allever.android.lib.widget.demo.RefreshRVActivity
import app.allever.android.lib.widget.ripple.RippleHelper

class MainActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RippleHelper.addRippleView(findViewById(R.id.btnMvvm))
        RippleHelper.addRippleView(findViewById(R.id.btnMvp))
        RippleHelper.addRippleView(findViewById(R.id.btnNetwork))
        RippleHelper.addRippleView(findViewById(R.id.btnDialog))
        RippleHelper.addRippleView(findViewById(R.id.btnPermission))
        RippleHelper.addRippleView(findViewById(R.id.btnRefreshRV))
        RippleHelper.addRippleView(findViewById(R.id.btnImageLoader))
        RippleHelper.addRippleView(findViewById(R.id.btnBaseActivity))

        findViewById<View>(R.id.btnMvvm).setOnClickListener {
            ActivityHelper.startActivity(MvvmActivity::class.java)
//            ActivityHelper.startActivity(CropMainActivity::class.java)
//            ActivityHelper.startActivity(AndPermissionActivity::class.java)
        }

        findViewById<View>(R.id.btnMvp).setOnClickListener {
            ActivityHelper.startActivity(MvpActivity::class.java)
        }

        findViewById<View>(R.id.btnNetwork).setOnClickListener {
            ActivityHelper.startActivity(NetworkActivity::class.java)
        }

        findViewById<View>(R.id.btnDialog).setOnClickListener {
            ActivityHelper.startActivity(DialogActivity::class.java)
        }

        findViewById<View>(R.id.btnPermission).setOnClickListener {
//            ActivityHelper.startActivity(AndPermissionActivity::class.java)
            ActivityHelper.startActivity(PermissionXActivity::class.java)
        }

        findViewById<View>(R.id.btnRefreshRV).setOnClickListener {
            ActivityHelper.startActivity(RefreshRVActivity::class.java)
        }

        findViewById<View>(R.id.btnImageLoader).setOnClickListener {
            ActivityHelper.startActivity(ImageLoaderActivity::class.java)
        }

        findViewById<View>(R.id.btnBusinessInterceptor).setOnClickListener {
            ActivityHelper.startActivity(BusinessInterceptorActivity::class.java)
        }

        findViewById<View>(R.id.btnBaseActivity).setOnClickListener {
            ActivityHelper.startActivity(UserActivity::class.java)
        }

    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}

class MainViewModel: BaseViewModel() {
    override fun init() {

    }
}