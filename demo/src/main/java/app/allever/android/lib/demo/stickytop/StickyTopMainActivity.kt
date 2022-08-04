package app.allever.android.lib.demo.stickytop

import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.demo.BR
import app.allever.android.lib.demo.R
import app.allever.android.lib.demo.databinding.ActivityStickyTopMainBinding
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig

class StickyTopMainActivity : BaseActivity<ActivityStickyTopMainBinding, StickyTopMainViewModel>() {
    override fun init() {
        initTopBar("吸顶Demo")
        binding.btnBaseStickyTopWithTwoView.setOnClickListener {
            ActivityHelper.startActivity<BaseTwoViewStickyTopActivity> { }
        }
        binding.btnStickyTopWithRemoveAddView.setOnClickListener {
            ActivityHelper.startActivity<RemoveAddViewStickyTopActivity> {  }
        }
        binding.btnRecyclerViewHeader.setOnClickListener {
            ActivityHelper.startActivity<RecyclerViewHeaderStickyTopActivity> { }
        }

        binding.btnCoordinator.setOnClickListener {
            ActivityHelper.startActivity<CoordinatorStickyTopActivity> { }
        }
    }

    override fun getContentMvvmConfig() =
        MvvmConfig(R.layout.activity_sticky_top_main, BR.stickyTopMainVM)
}

class StickyTopMainViewModel : BaseViewModel() {
    override fun init() {

    }
}