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
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentPickerListBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.ui.adapter.ImageVideoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageVideoFragment : AbstractFragment() {
    private lateinit var mBinding: FragmentPickerListBinding
    private val mViewModel by viewModels<ImageVideoFragmentViewModel>()
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
            mViewModel.list.addAll(mViewModel.fetchData(requireContext()))
            mViewModel.adapter.setList(mViewModel.list)
        }
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

    suspend fun fetchData(context: Context) = withContext(Dispatchers.IO) {
        val folderList = MediaHelper.getAllFolder(context, mediaType, true)
        val result = mutableListOf<MediaItem>()
        folderList.map {
            val list = if (mediaType == MediaHelper.TYPE_VIDEO) {
                MediaHelper.getVideoMedia(context, it.dir, 0)
            } else {
                MediaHelper.getImageMedia(context, it.dir)
            }

            list.map {
                val mediaItem = MediaItem(it)
                result.add(mediaItem)
            }
        }
        result
    }
}