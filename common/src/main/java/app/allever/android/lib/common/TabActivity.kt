package app.allever.android.lib.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import app.allever.android.lib.common.databinding.ActivityTabBinding
import app.allever.android.lib.core.base.adapter.Pager2Adapter
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.widget.ext.modifyTouchSlop

abstract class TabActivity<DB, VM> : BaseActivity<ActivityTabBinding, TabViewModel>() {
    abstract fun getPageTitle(): String
    abstract fun getTabTitles(): MutableList<String>
    abstract fun getFragments(): MutableList<Fragment>
    open fun onPageChanged(position: Int) {}
    override fun inflateChildBinding() = ActivityTabBinding.inflate(layoutInflater)
    override fun init() {
        initTopBar(getPageTitle())
        binding.viewPager.adapter = Pager2Adapter(this, getFragments())
        binding.viewPager.modifyTouchSlop()
        binding.tabLayout.setViewPager2(binding.viewPager, getTabTitles() as ArrayList<String>?)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                onPageChanged(position)
            }
        })
    }
}

class TabViewModel : BaseViewModel() {
    override fun init() {

    }
}