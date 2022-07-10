package app.allever.android.lib.widget.mediapicker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.allever.android.lib.core.base.AbstractFragment
import app.allever.android.lib.core.function.media.FolderBean
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentPickerListBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.MediaPicker
import app.allever.android.lib.widget.mediapicker.ui.adapter.AudioAdapter
import app.allever.android.lib.widget.mediapicker.ui.adapter.ClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioFragment : AbstractFragment(), IMediaPicker {
    private lateinit var mBinding: FragmentPickerListBinding
    private val mViewModel by viewModels<AudioFragmentViewModel>()

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
        mViewModel.adapter = AudioAdapter()
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerView.adapter = mViewModel.adapter
        mViewModel.adapter.setOptionListener(object : ClickListener {
            override fun onItemClick(mediaItem: MediaItem, position: Int): Boolean {
                mSelectListener?.onItemSelected(mediaItem)
                return true
            }

            override fun onItemLongClick(mediaItem: MediaItem, position: Int) {
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

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.adapter.release()
    }
}

class AudioFragmentViewModel : ViewModel() {
    val list = mutableListOf<MediaItem>()
    var mediaType: String = ""
    lateinit var adapter: AudioAdapter
    fun init() {

    }

    fun initExtra(bundle: Bundle?) {
        mediaType = bundle?.getString("MEDIA_TYPE") ?: ""
    }

    suspend fun fetchData(context: Context, path: String = "") =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MediaItem>()

            if (path.isNotEmpty()) {
                var folderBean: FolderBean? = null
                MediaPicker.cacheFolderList.map {
                    if (it.dir == path) {
                        folderBean = it
                        return@map
                    }
                }

                folderBean?.let {
                    it.audioMediaList.map {
                        val mediaItem = MediaItem(it)
                        result.add(mediaItem)
                    }
                    if (result.isNotEmpty()) {
                        return@withContext result
                    }
                }
            }

            val isAll = path.isEmpty()
            if (isAll && MediaPicker.cacheAllAudioBeanList.isNotEmpty()) {
                MediaPicker.cacheAllAudioBeanList.map {
                    val mediaItem = MediaItem(it)
                    result.add(mediaItem)
                }
                return@withContext result
            }

            val list = MediaHelper.getAudioMedia(context, path, 0)
            if (path.isEmpty()) {
                MediaPicker.cacheAllAudioBeanList.addAll(list)
            }
            list.map {
                val mediaItem = MediaItem(it)
                result.add(mediaItem)
            }
            result
        }
}