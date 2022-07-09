package app.allever.android.lib.widget.mediapicker.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.allever.android.lib.core.base.AbstractFragment
import app.allever.android.lib.core.base.adapter.PagerAdapter
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.media.FolderBean
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.function.media.MediaType
import app.allever.android.lib.core.helper.FragmentHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentMediaPickerBinding
import app.allever.android.lib.widget.fragment.EmptyFragment
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.MediaPicker
import app.allever.android.lib.widget.mediapicker.ui.adapter.FolderAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPickerFragment : AbstractFragment(), SelectListener {
    companion object {
        const val EXTRA_MEDIA_TYPE_LIST = "EXTRA_MEDIA_TYPE_LIST"
        private const val ANIMATION_DURATION = 200L
    }

    private val mViewModel by viewModels<MediaPickerFragmentViewModel>()

    private lateinit var mBinding: FragmentMediaPickerBinding

    private var mSelectAlbumContainerAnimShow: Animator? = null
    private var mSelectAlbumContainerAnimHide: Animator? = null
    private var mIvArrowRotateAnimUp: Animator? = null
    private var mIvArrowRotateAnimDown: Animator? = null

    private var mCallback: Callback? = null

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
        initFolder()
        initAnim()
        initListener()
    }

    private fun initListener() {
        mBinding.ivBack.setOnClickListener {
            mCallback?.onFinish()
        }

        mBinding.llAlbumTitleContainer.setOnClickListener {
            when (mBinding.flSelectAlbumContainer.visibility) {
                View.VISIBLE -> {
                    showSelectAlbumContainer(false)
                }
                View.GONE -> {
                    showSelectAlbumContainer(true)
                }
            }
        }

        mBinding.tvConfirm.setOnClickListener {
            MediaPicker.listeners().map {
                val all = mutableListOf<MediaBean>()
                val imageList = mutableListOf<MediaBean>()
                val videoList = mutableListOf<MediaBean>()
                val audioList = mutableListOf<MediaBean>()
                mViewModel.selectedList.map {
                    val bean = it.data
                    all.add(bean)
                    when {
                        MediaType.isAudio(bean.type) -> {
                            audioList.add(bean)
                        }
                        MediaType.isImage(bean.type) -> {
                            imageList.add(bean)
                        }
                        MediaType.isVideo(bean.type) -> {
                            videoList.add(bean)
                        }
                        else -> {

                        }
                    }

                }
                it.onPicked(all, imageList, videoList, audioList)
            }

            mCallback?.onFinish()
        }
    }

    private fun initFragment() {
        mViewModel.typeList.map {
            when (it) {
                MediaHelper.TYPE_IMAGE, MediaHelper.TYPE_VIDEO -> {
                    val fragment = FragmentHelper.newInstance(ImageVideoFragment::class.java) {
                        putString("MEDIA_TYPE", it)
                    }
                    fragment.setSelectListener(this)
                    mViewModel.fragmentList.add(fragment)
                }
                MediaHelper.TYPE_AUDIO -> {
                    val fragment = FragmentHelper.newInstance(AudioFragment::class.java) {
                        putString("MEDIA_TYPE", it)
                    }
                    fragment.setSelectListener(this)
                    mViewModel.fragmentList.add(fragment)
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

    private fun initFolder() {
        mBinding.recyclerViewFolder.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerViewFolder.adapter = mViewModel.folderAdapter
        mViewModel.folderAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mViewModel.folderList[position]
            val dir = item.dir
            mBinding.tvTitle.text = item.name
            showSelectAlbumContainer(false)
            val list = mutableListOf<FolderBean>()
            val folderBean = FolderBean()
            folderBean.dir = dir
            list.add(folderBean)
            mViewModel.fragmentList.map {
                (it as? IMediaPicker)?.update(list)
            }

            mViewModel.selectedList.clear()
            updateConfirm()
        }
    }

    private fun initAnim() {
        mSelectAlbumContainerAnimShow =
            ObjectAnimator.ofFloat(mBinding.flSelectAlbumContainer, "alpha", 0f, 1f)
        mSelectAlbumContainerAnimShow?.duration = ANIMATION_DURATION
        mSelectAlbumContainerAnimShow?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {
                mBinding.flSelectAlbumContainer.visibility = View.VISIBLE
            }
        })


        mSelectAlbumContainerAnimHide =
            ObjectAnimator.ofFloat(mBinding.flSelectAlbumContainer, "alpha", 1f, 0f)
        mSelectAlbumContainerAnimHide?.duration = ANIMATION_DURATION
        mSelectAlbumContainerAnimHide?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                mBinding.flSelectAlbumContainer.visibility = View.GONE
            }
        })

        mIvArrowRotateAnimUp = ObjectAnimator.ofFloat(mBinding.ivSelectAlbum, "rotation", 0f, 180f)
        mIvArrowRotateAnimUp?.duration = ANIMATION_DURATION

        mIvArrowRotateAnimDown =
            ObjectAnimator.ofFloat(mBinding.ivSelectAlbum, "rotation", 180f, 360f)
        mIvArrowRotateAnimDown?.duration = ANIMATION_DURATION
    }

    private fun showSelectAlbumContainer(show: Boolean) {
        if (show) {
            mSelectAlbumContainerAnimShow?.start()
            mIvArrowRotateAnimUp?.start()
        } else {
            mSelectAlbumContainerAnimHide?.start()
            mIvArrowRotateAnimDown?.start()
        }
    }


    private fun initData() {
        lifecycleScope.launch {
            val folderList = mViewModel.getFolder(requireContext())
            folderList.map {
                log("total count = ${it.total}")
            }
            mViewModel.folderAdapter.setList(folderList)
        }
    }

    override fun onItemSelected(mediaItem: MediaItem) {
        if (mediaItem.isChecked) {
            mViewModel.selectedList.add(mediaItem)
        } else {
            mViewModel.selectedList.remove(mediaItem)
        }

        updateConfirm()

    }

    private fun updateConfirm() {
        mBinding.tvConfirm.text = if (mViewModel.selectedList.isEmpty()) {
            "取消"
        } else {
            "使用(${mViewModel.selectedList.size})"
        }
    }

    override fun onConfirm() {
        activity?.finish()
    }

    fun setCallback(callback: Callback) {
        mCallback = callback
    }

    interface Callback {
        fun onFinish()
    }
}


class MediaPickerFragmentViewModel : ViewModel() {
    lateinit var typeList: ArrayList<String>
    val fragmentList = arrayListOf<Fragment>()

    //目录列表数据
    var folderList = mutableListOf<FolderBean>()
    val folderAdapter = FolderAdapter()

    val selectedList = mutableListOf<MediaItem>()

    fun initExtra(intent: Bundle?) {
        typeList =
            intent?.getStringArrayList(MediaPickerFragment.EXTRA_MEDIA_TYPE_LIST) as? ArrayList<String>
                ?: ArrayList()
        typeList.map {
            log("MediaPickerFragment 媒体类型：${it}")
        }
    }

    suspend fun getFolder(context: Context) = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val mediaFolderList = MediaHelper.getAllFolder(context)
        var imageCount = 0
        var videoCount = 0
        var audioCount = 0
        mediaFolderList.map {
            if (typeList.contains(MediaHelper.TYPE_IMAGE)) {
                val imageList = MediaHelper.getImageMedia(context, it.dir)
                it.photoCount = imageList.size
                if (imageList.isNotEmpty()) {
                    it.coverMediaBean = imageList[0]
                }
                imageCount += it.photoCount
            }

            if (typeList.contains(MediaHelper.TYPE_VIDEO)) {
                val videoList = MediaHelper.getVideoMedia(context, it.dir)
                if (videoList.isNotEmpty()) {
                    it.coverMediaBean = videoList[0]
                }
                it.videoCount = videoList.size
                videoCount += it.videoCount
            }

            if (typeList.contains(MediaHelper.TYPE_AUDIO)) {
                val audioList = MediaHelper.getAudioMedia(context, it.dir)
                it.audioCount = audioList.size
                audioCount += it.audioCount
            }
        }

        val endTime = System.currentTimeMillis()
        log("getFolder 耗时：${(endTime - startTime)}")

        val allFolderBean = FolderBean()
        allFolderBean.audioCount = audioCount
        allFolderBean.photoCount = imageCount
        allFolderBean.videoCount = videoCount
        allFolderBean.total = audioCount + imageCount + videoCount
        allFolderBean.name = "全部"
        mediaFolderList.add(0, allFolderBean)
        folderList.addAll(mediaFolderList)
        mediaFolderList
    }
}
