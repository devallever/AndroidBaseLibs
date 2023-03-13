package app.allever.android.lib.common

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import app.allever.android.lib.common.databinding.ActivityBaseFragmentBinding
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.widget.fragment.EmptyFragment

class FragmentActivity :
    BaseFragmentActivity<ActivityBaseFragmentBinding, BaseFragmentViewModel>() {

    private var mShowTopBar = true
    private var mDarkMode = false

    companion object {
//        fun <T : Class<*>> start(title: String, clz: T) {
//            ActivityHelper.startActivity<FragmentActivity> {
//                putExtra("fragmentName", clz.name)
//                putExtra("title", title)
//            }
//        }

        inline fun <reified T> start(
            title: String,
            showTopBar: Boolean = true,
            darkMode: Boolean = false
        ) {
            ActivityHelper.startActivity<FragmentActivity> {
                putExtra("fragmentName", T::class.java.name)
                putExtra("title", title)
                putExtra("darkMode", darkMode)
                putExtra("showTopBar", showTopBar)
            }
        }

        inline fun <reified T> start(
            title: String,
            showTopBar: Boolean = true,
            darkMode: Boolean = false,
            block: (fragmentArgs: Bundle) -> Unit
        ) {
            val bundle = Bundle()
            block.invoke(bundle)
            ActivityHelper.startActivity<FragmentActivity> {
                putExtra("fragmentName", T::class.java.name)
                putExtra("title", title)
                putExtra("showTopBar", showTopBar)
                putExtra("darkMode", darkMode)
                putExtra("fragmentArgs", bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mShowTopBar = intent?.getBooleanExtra("showTopBar", true) ?: true
        mDarkMode = intent?.getBooleanExtra("darkMode", false) ?: false
        super.onCreate(savedInstanceState)
    }

    override fun init() {
        super.init()
        initTopBar(intent?.getStringExtra("title") ?: "FragmentActivity")
    }

    override fun showTopBar(): Boolean {
        return mShowTopBar
    }

    override fun isDarkMode(): Boolean {
        return mDarkMode
    }

    override fun attachFragment(): Fragment {
        val fragmentArgs = intent?.getBundleExtra("fragmentArgs")
        try {
            val clzName = intent.getStringExtra("fragmentName")
            if (TextUtils.isEmpty(clzName)) {
                return EmptyFragment()
            }
            val fragment = Class.forName(clzName!!).getConstructor().newInstance() as Fragment
            fragment.arguments = fragmentArgs
            return fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return EmptyFragment()
    }
}