package app.allever.android.lib.common

import app.allever.android.lib.common.databinding.EmptyPageDarkBinding
import app.allever.android.lib.mvvm.base.BaseViewModel

class FullScreenDarkActivity : BaseActivity<EmptyPageDarkBinding, BaseViewModel>() {
    override fun init() {

    }

    override fun inflateChildBinding() = EmptyPageDarkBinding.inflate(layoutInflater)

    override fun showTopBar(): Boolean {
        return false
    }

    override fun isDarkMode(): Boolean {
        return true
    }
}