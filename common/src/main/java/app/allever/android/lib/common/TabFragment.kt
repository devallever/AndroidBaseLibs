package app.allever.android.lib.common

import androidx.fragment.app.Fragment
import app.allever.android.lib.common.databinding.FragmentTabBinding
import app.allever.android.lib.core.base.adapter.Pager2Adapter
import app.allever.android.lib.widget.ext.modifyTouchSlop

abstract class TabFragment<DB, VM> : BaseFragment<FragmentTabBinding, TabViewModel>() {
    abstract fun getTabTitles(): MutableList<String>
    abstract fun getFragments(): MutableList<Fragment>
    override fun inflate() = FragmentTabBinding.inflate(layoutInflater)
    override fun init() {
        mBinding.viewPager.adapter = Pager2Adapter(this, getFragments())
        mBinding.viewPager.modifyTouchSlop()
        mBinding.tabLayout.setViewPager2(mBinding.viewPager, getTabTitles() as ArrayList<String>?)
    }
}