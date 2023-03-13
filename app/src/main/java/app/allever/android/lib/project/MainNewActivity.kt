package app.allever.android.lib.project

import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.core.helper.FragmentHelper
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.project.databinding.ActivityMainNewBinding

class MainNewActivity : BaseActivity<ActivityMainNewBinding, BaseViewModel>() {
    override fun inflateChildBinding() = ActivityMainNewBinding.inflate(layoutInflater)

    override fun init() {
        initTopBar(getString(R.string.app_name), showBackIcon = false)
        FragmentHelper.addToContainer(
            supportFragmentManager,
            MainListFragment(),
            binding.fragmentContainer.id
        )
    }
}