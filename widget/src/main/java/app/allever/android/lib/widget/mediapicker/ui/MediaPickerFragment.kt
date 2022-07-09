package app.allever.android.lib.widget.mediapicker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.AbstractFragment
import app.allever.android.lib.core.base.adapter.PagerAdapter
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.helper.FragmentHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentMediaPickerBinding
import app.allever.android.lib.widget.fragment.EmptyFragment
import kotlinx.coroutines.launch

class MediaPickerFragment : AbstractFragment() {
    companion object {
        const val EXTRA_MEDIA_TYPE_LIST = "EXTRA_MEDIA_TYPE_LIST"
    }

    private val mViewModel by viewModels<MediaPickerFragmentViewModel>()

    private lateinit var mBinding: FragmentMediaPickerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_media_picker,
            container,
            false
        )
        mViewModel.initExtra(arguments)
        initView()
        initData()
        return mBinding.root
    }

    private fun initView() {
        initFragment()
        initViewPager()
        initTab()
    }

    private fun initFragment() {
        mViewModel.typeList.map {
            when (it) {
                MediaHelper.TYPE_IMAGE, MediaHelper.TYPE_VIDEO -> {
                    mViewModel.fragmentList.add(FragmentHelper.newInstance(ImageVideoFragment::class.java) {
                        putString("MEDIA_TYPE", it)
                    })
                }
                MediaHelper.TYPE_AUDIO -> {
                    mViewModel.fragmentList.add(FragmentHelper.newInstance(AudioFragment::class.java) {
                        putString("MEDIA_TYPE", it)
                    })
                }
                else -> {
                    mViewModel.fragmentList.add(FragmentHelper.newInstance(EmptyFragment::class.java))
                }
            }
        }
    }

    private fun initViewPager() {
        val adapter =
            fragmentManager?.let { PagerAdapter(it, mViewModel.fragmentList, mViewModel.typeList) }
        mBinding.viewPager.adapter = adapter
        mBinding.viewPager.offscreenPageLimit = mViewModel.typeList.size
    }

    private fun initTab() {
        setVisibility(mBinding.tabLayout, mViewModel.typeList.size > 1)
        mViewModel.typeList.map {
            val tab = mBinding.tabLayout.newTab()
            tab.text = it
            mBinding.tabLayout.addTab(tab)
        }
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

    private fun initData() {
        lifecycleScope.launch {

        }
    }
}


class MediaPickerFragmentViewModel : ViewModel() {
    lateinit var typeList: ArrayList<String>
    val fragmentList = arrayListOf<Fragment>()
    fun initExtra(intent: Bundle?) {
        typeList =
            intent?.getStringArrayList(MediaPickerFragment.EXTRA_MEDIA_TYPE_LIST) as? ArrayList<String>
                ?: ArrayList()
        typeList.map {
            log("MediaPickerFragment 媒体类型：${it}")
        }
    }
}
