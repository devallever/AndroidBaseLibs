package app.allever.android.lib.widget.mediapicker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import app.allever.android.lib.core.base.AbstractFragment
import app.allever.android.lib.core.function.imageloader.load
import app.allever.android.lib.core.function.media.MediaType
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentPreviewBinding
import app.allever.android.lib.widget.mediapicker.MediaItem

class PreviewFragment : AbstractFragment() {

    private val mViewModel by viewModels<PreviewFragmentViewModel>()
    private lateinit var mBinding: FragmentPreviewBinding
    private var mMediaItem: MediaItem? = null
    private var mVideoViewHolder: VideoViewHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_preview,
            container,
            false
        )
        initView()
        return mBinding.root
    }

    private fun initView() {
        val data = mMediaItem?.data ?: return
        if (MediaType.isImage(data.type)) {
            //图片类型
            mBinding.idIvImage.visibility = View.VISIBLE
            mBinding.idIvImage.load(data.uri ?: data.path)
        } else if (MediaType.isVideo(data.type)) {
            //视频类型
            mBinding.idIvImage.visibility = View.GONE

            mVideoViewHolder = VideoViewHolder()
            mVideoViewHolder?.initVideo(
                mBinding.idVideoView,
                data.uri,
                data.path,
                mBinding.idIvVideoController
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mVideoViewHolder?.stop()
        mVideoViewHolder?.destroy()
        mVideoViewHolder = null
    }

    fun pause() {
        mVideoViewHolder?.pause()
    }

    fun setData(thumbnailBean: MediaItem?) {
        mMediaItem = thumbnailBean
    }
}

class PreviewFragmentViewModel : ViewModel() {

}