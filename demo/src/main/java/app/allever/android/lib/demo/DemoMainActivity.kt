package app.allever.android.lib.demo

import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.demo.databinding.ActivityDemoMainBinding
import app.allever.android.lib.demo.stickytop.StickyTopMainActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig

class DemoMainActivity : BaseActivity<ActivityDemoMainBinding, DemoMainViewModel>() {
    override fun getContentMvvmConfig(): MvvmConfig =
        MvvmConfig(R.layout.activity_demo_main, BR.demoMainVM)

    override fun init() {
        initTopBar("Android Demo")
        binding.btnStickyTop.setOnClickListener {
            ActivityHelper.startActivity<StickyTopMainActivity>()
        }
    }
}

class DemoMainViewModel : BaseViewModel() {
    override fun init() {

    }
}