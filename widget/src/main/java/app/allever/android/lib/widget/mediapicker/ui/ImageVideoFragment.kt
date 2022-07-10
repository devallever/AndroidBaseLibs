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
import app.allever.android.lib.core.function.work.PollingTask
import app.allever.android.lib.core.helper.ActivityHelper
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

class ImageVideoFragment : AbstractFragment(), IMediaPicker, PreviewActivity.Callback {
    private lateinit var mBinding: FragmentPickerListBinding
    private val mViewModel by viewModels<ImageVideoFragmentViewModel>()
    private var mSelectListener: SelectListener? = null

    private val mSetCallbackTask by lazy {
        object : PollingTask() {
            override fun interval() = 100L
            override fun condition(): Boolean {
                return ActivityHelper.getTopActivity() as? PreviewActivity != null
            }

            override fun execute() {
                val previewActivity = ActivityHelper.getTopActivity() as? PreviewActivity
                previewActivity?.setCallback(this@ImageVideoFragment)
            }
        }
    }

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
            override fun onItemClick(mediaItem: MediaItem, position: Int) {
                mSelectListener?.onItemSelected(mediaItem)
            }

            override fun onItemLongClick(mediaItem: MediaItem, position: Int) {
                MediaPicker.extraMap[PreviewActivity.EXTRA_THUMBNAIL_LIST] = mViewModel.list
                PreviewActivity.startActivity(activity!!, position)
                mSetCallbackTask.start()
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
        fetchData()
    }

    override fun update(path: String) {
        fetchData(path)
    }

    private fun fetchData(path: String = "") {
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

    override fun updateSelected(mediaItem: MediaItem) {
        val index = mViewModel.list.indexOf(mediaItem)
        mViewModel.adapter.setData(index, mediaItem)
        mSelectListener?.onItemSelected(mediaItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSetCallbackTask.cancel()
    }
}

class ImageVideoFragmentViewModel : ViewModel() {
    val list = mutableListOf<MediaItem>()
    var mediaType: String = ""
    lateinit var adapter: ImageVideoAdapter

    fun initExtra(bundle: Bundle?) {
        mediaType = bundle?.getString("MEDIA_TYPE") ?: ""
    }

    suspend fun fetchData(context: Context, path: String = "") =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MediaItem>()

            result.addAll(MediaPicker.fetchFromFolderCache(mediaType, path))
            if (result.isNotEmpty()) {
                return@withContext result
            }

            result.addAll(MediaPicker.fetchFromCache(mediaType, path))
            if (result.isNotEmpty()) {
                return@withContext result
            }

            result.addAll(MediaPicker.fetchFromPhone(context, mediaType, path))

            result
        }
}