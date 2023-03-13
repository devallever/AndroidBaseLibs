package app.allever.android.lib.common

import app.allever.android.lib.common.databinding.EmptyPageBinding
import app.allever.android.lib.core.base.BaseSimpleActivity

class FullScreenActivity : BaseSimpleActivity<EmptyPageBinding>() {
    override fun init() {
        adaptStatusBar(mBinding.btnTopView)
    }

    override fun inflate() = EmptyPageBinding.inflate(layoutInflater)
}