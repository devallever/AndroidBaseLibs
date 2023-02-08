package app.allever.android.lib.project

import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.BaseSimpleActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.project.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity: BaseSimpleActivity<ActivitySplashBinding>() {
    override fun inflate() = ActivitySplashBinding.inflate(layoutInflater)

    override fun init() {
        lifecycleScope.launch {
            delay(1000)
            ActivityHelper.startActivity<MainActivity> {  }
            finish()
        }
    }

    override fun onBackPressed() {

    }
}