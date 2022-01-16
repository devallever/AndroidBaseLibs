package app.allever.android.lib.project

import android.os.Bundle
import android.view.View
import app.allever.androd.lib.cropper.CropMainActivity
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.mvp.demo.MvpActivity
import app.allever.android.lib.network.demo.NetActivityJava
import app.allever.android.lib.network.demo.NetworkActivity
import app.allever.android.lib.widget.ripple.RippleHelper

class MainActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RippleHelper.addRippleView(findViewById(R.id.tvClickMvp))
        RippleHelper.addRippleView(findViewById(R.id.tvClickMvvm))

        findViewById<View>(R.id.tvClickMvvm).setOnClickListener {
//            ActivityHelper.startActivity(MvvmActivity::class.java)
            ActivityHelper.startActivity(NetworkActivity::class.java)
//            ActivityHelper.startActivity(CropMainActivity::class.java)
        }

        findViewById<View>(R.id.tvClickMvp).setOnClickListener {
            ActivityHelper.startActivity(MvpActivity::class.java)
        }
    }
}