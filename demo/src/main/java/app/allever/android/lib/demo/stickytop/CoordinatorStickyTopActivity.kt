package app.allever.android.lib.demo.stickytop

import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.demo.BR
import app.allever.android.lib.demo.R
import app.allever.android.lib.demo.databinding.ActivityCoordinatorStickyTopBinding
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig

class CoordinatorStickyTopActivity :
    BaseActivity<ActivityCoordinatorStickyTopBinding, CoordinatorStickyTopViewModel>() {
    override fun getContentMvvmConfig() =
        MvvmConfig(R.layout.activity_coordinator_sticky_top, BR.coordinatorStickyTopVM)

    override fun init() {
        initTopBar("CoordinatorLayout + AppbarLayout")

    }


}

class CoordinatorStickyTopViewModel : BaseViewModel() {
    override fun init() {

    }
}