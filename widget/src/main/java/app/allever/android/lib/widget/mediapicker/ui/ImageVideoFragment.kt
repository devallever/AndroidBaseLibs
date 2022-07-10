package app.allever.android.lib.widget.mediapicker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import app.allever.android.lib.core.base.AbstractFragment
import app.allever.android.lib.core.function.media.FolderBean
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentPickerListBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.MediaPicker
import app.allever.android.lib.widget.mediapicker.ui.adapter.ClickListener
import app.allever.android.lib.widget.mediapicker.ui.adapter.ImageVideoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageVideoFragment : AbstractFragment(), IMediaPicker {
    private lateinit var mBinding: FragmentPickerListBinding
    private val mViewModel by viewModels<ImageVideoFragmentViewModel>()
    private var mSelectListener: SelectListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_picker_list,
            container,
            false
        )
        mViewModel.initExtra(arguments)
        initView()
        initData()
        return mBinding.root
    }

    private fun initView() {
        val MAX_COL = 4
        mViewModel.adapter = ImageVideoAdapter()
        mBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), MAX_COL)
        mBinding.recyclerView.adapter = mViewModel.adapter
        mViewModel.adapter.setOptionListener(object : ClickListener {
            override fun onItemClick(mediaItem: MediaItem, position: Int): Boolean {
                mSelectListener?.onItemSelected(mediaItem)
                return true
            }

            override fun onItemLongClick(mediaItem: MediaItem, position: Int) {
                try {
                    val list = mutableListOf<MediaBean>()
                    mViewModel.list.map {
                        list.add(it.data)
                    }
                    MediaPicker.extraMap[PreviewActivity.EXTRA_THUMBNAIL_LIST] = list
                    PreviewActivity.startActivity(activity!!, position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        })
        val firstTopSpacing = DisplayHelper.dip2px(2)
        mBinding.recyclerView.addItemDecoration(object :
            androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: androidx.recyclerview.widget.RecyclerView,
                state: androidx.recyclerview.widget.RecyclerView.State
            ) {
                val pos = parent.getChildLayoutPosition(view)
                if (pos / MAX_COL == 0) {
                    //设置第一行
                    outRect.top = firstTopSpacing
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        lifecycleScope.launch {
            mViewModel.list.clear()
            mViewModel.adapter.setList(null)
            mViewModel.list.addAll(mViewModel.fetchData(requireContext()))
            mViewModel.adapter.setList(mViewModel.list)
        }
    }

    override fun update(path: String) {
        lifecycleScope.launch {
            mViewModel.list.clear()
            mViewModel.adapter.setList(null)
            mViewModel.list.addAll(mViewModel.fetchData(requireContext(), path))
            mViewModel.adapter.setList(mViewModel.list)
        }
    }

    fun setSelectListener(selectListener: SelectListener?) {
        mSelectListener = selectListener
    }
}

class ImageVideoFragmentViewModel : ViewModel() {
    val list = mutableListOf<MediaItem>()
    var mediaType: String = ""
    lateinit var adapter: ImageVideoAdapter
    fun init() {

    }

    fun initExtra(bundle: Bundle?) {
        mediaType = bundle?.getString("MEDIA_TYPE") ?: ""
    }

    suspend fun fetchData(context: Context, path: String = "") =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MediaItem>()

            var folderBean: FolderBean? = null
            if (path.isNotEmpty()) {
                MediaPicker.cacheFolderList.map {
                    if (it.dir == path) {
                        folderBean = it
                        return@map
                    }
                }
                folderBean?.let {
                    if (mediaType == MediaHelper.TYPE_IMAGE) {
                        it.imageMediaList.map {
                            val mediaItem = MediaItem(it)
                            result.add(mediaItem)
                        }
                        if (result.isNotEmpty()) {
                            return@withContext result
                        }
                    } else if (mediaType == MediaHelper.TYPE_VIDEO) {
                        it.videoMediaList.map {
                            val mediaItem = MediaItem(it)
                            result.add(mediaItem)
                        }
                        if (result.isNotEmpty()) {
                            return@withContext result
                        }
                    }
                }
            }

            val isAll = path.isEmpty()
            if (isAll) {
                if (mediaType == MediaHelper.TYPE_IMAGE) {
                    if (MediaPicker.cacheAllImageBeanList.isNotEmpty()) {
                        MediaPicker.cacheAllImageBeanList.map {
                            val mediaItem = MediaItem(it)
                            result.add(mediaItem)
                        }
                        return@withContext result
                    }
                } else if (mediaType == MediaHelper.TYPE_VIDEO) {
                    if (MediaPicker.cacheAllVideoBeanList.isNotEmpty()) {
                        MediaPicker.cacheAllVideoBeanList.map {
                            val mediaItem = MediaItem(it)
                            result.add(mediaItem)
                        }
                        return@withContext result
                    }
                }

            }

            val list = if (mediaType == MediaHelper.TYPE_VIDEO) {
                MediaHelper.getVideoMedia(context, path, 0)
            } else {
                MediaHelper.getImageMedia(context, path)
            }

            if (path.isEmpty()) {
                if (mediaType == MediaHelper.TYPE_IMAGE) {
                    MediaPicker.cacheAllImageBeanList.addAll(list)
                } else if (mediaType == MediaHelper.TYPE_VIDEO) {
                    MediaPicker.cacheAllVideoBeanList.addAll(list)
                }
            }

            list.map {
                val mediaItem = MediaItem(it)
                result.add(mediaItem)
            }
            result
        }
}