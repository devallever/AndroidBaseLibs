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
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentPickerListBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.ui.adapter.AudioAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioFragment : AbstractFragment() {
    private lateinit var mBinding: FragmentPickerListBinding
    private val mViewModel by viewModels<AudioFragmentViewModel>()

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
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        lifecycleScope.launch {
            mViewModel.list.addAll(mViewModel.fetchData(requireContext()))
            mViewModel.adapter.setList(mViewModel.list)
        }
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

    suspend fun fetchData(context: Context) = withContext(Dispatchers.IO) {
        val folderList = MediaHelper.getAllFolder(context, mediaType, true)
        val result = mutableListOf<MediaItem>()
        folderList.map {
            val list = MediaHelper.getAudioMedia(context, it.dir, 0)
            list.map {
                val mediaItem = MediaItem(it)
                result.add(mediaItem)
            }
        }
        result
    }
}