package app.allever.android.lib.core.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author allever
 */
class Pager2Adapter : FragmentStateAdapter {
    protected var mFragmentList: List<Fragment>?

    constructor(fragmentActivity: FragmentActivity, fragmentList: List<Fragment>?) : super(
        fragmentActivity
    ) {
        mFragmentList = fragmentList
    }

    constructor(fragment: Fragment, fragmentList: List<Fragment>?) : super(fragment) {
        mFragmentList = fragmentList
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList!![position]
    }

    override fun getItemCount(): Int {
        return if (mFragmentList == null) {
            0
        } else mFragmentList?.size ?: 0
    }
}