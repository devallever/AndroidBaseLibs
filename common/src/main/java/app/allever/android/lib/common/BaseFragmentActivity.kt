package app.allever.android.lib.common

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import app.allever.android.lib.common.databinding.ActivityBaseFragmentBinding
import app.allever.android.lib.mvvm.base.BaseViewModel

abstract class BaseFragmentActivity<DB : ViewBinding, VM : BaseViewModel> :
    BaseActivity<DB, VM>() {

    protected lateinit var mFragment: Fragment

    override fun init() {
        mFragment = attachFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mFragment)
            .commit()
    }

    override fun inflateChildBinding() = ActivityBaseFragmentBinding.inflate(layoutInflater) as DB

    abstract fun attachFragment(): Fragment
}

class BaseFragmentViewModel : BaseViewModel() {

}