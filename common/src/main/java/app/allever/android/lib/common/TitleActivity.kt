package app.allever.android.lib.common

import app.allever.android.lib.common.databinding.EmptyPageBinding
import app.allever.android.lib.mvvm.base.BaseViewModel

class TitleActivity : BaseActivity<EmptyPageBinding, BaseViewModel>() {
    override fun init() {
        initTopBar("TitleActivity")
    }

    override fun inflateChildBinding() = EmptyPageBinding.inflate(layoutInflater)
}