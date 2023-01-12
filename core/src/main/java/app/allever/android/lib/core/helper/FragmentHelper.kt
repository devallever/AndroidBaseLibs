package app.allever.android.lib.core.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentHelper {

    fun <T : Fragment> newInstance(clz: Class<T>, block: Bundle.() -> Unit = {}): T {
        val fragment = clz.newInstance()
        val bundle = Bundle()
        bundle.block()
        fragment.arguments = bundle
        return fragment as T
    }

    fun addToContainer(fragmentManager: FragmentManager, fragment: Fragment, containerId: Int) {
        fragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }
}