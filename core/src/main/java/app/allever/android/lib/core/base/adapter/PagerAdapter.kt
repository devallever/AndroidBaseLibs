package app.allever.android.lib.core.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author Allever
 * @date 18/5/21
 */
class PagerAdapter(
    fragmentManager: FragmentManager,
    private val mFragmentList: List<Fragment>,
    private val mTitles: ArrayList<String> = arrayListOf()
) :
    FragmentStatePagerAdapter(
        fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (mTitles.isEmpty()) {
            return ""
        }
        return mTitles[position]
    }
}