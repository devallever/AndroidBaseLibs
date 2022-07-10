package app.allever.android.lib.widget.mediapicker.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.ui.PreviewFragment

class PreviewFragmentPagerAdapter(fragmentManager: androidx.fragment.app.FragmentManager, data: MutableList<MediaItem>) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var data: MutableList<MediaItem>? = data
    var currentFragment: Fragment? = null

    override fun getItem(position: Int): Fragment {
        return PreviewFragment()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        if (fragment is PreviewFragment) {
            val fragmentData = data
            if (fragmentData != null && position in 0 until fragmentData.size) {
                fragment.setData(fragmentData[position])
            }
        }
        currentFragment = fragment as Fragment
        return fragment
    }

    override fun getCount(): Int = data?.size ?: 0

}