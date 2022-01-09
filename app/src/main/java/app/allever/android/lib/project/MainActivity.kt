package app.allever.android.lib.project

import android.os.Bundle
import android.view.View
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.mvp.demo.MvpActivity
import app.allever.android.lib.mvvm.demo.MvvmActivity
import app.allever.android.lib.widget.ripple.RippleHelper

class MainActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RippleHelper.addRippleView(findViewById(R.id.tvClickMvp))
        RippleHelper.addRippleView(findViewById(R.id.tvClickMvvm))

        findViewById<View>(R.id.tvClickMvvm).setOnClickListener {
            ActivityHelper.startActivity(MvvmActivity::class.java)
        }

        findViewById<View>(R.id.tvClickMvp).setOnClickListener {
            ActivityHelper.startActivity(MvpActivity::class.java)
        }
    }
}