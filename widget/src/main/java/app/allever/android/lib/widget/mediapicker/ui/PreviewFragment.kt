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
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.media.MediaType
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentPreviewBinding
import app.allever.android.lib.widget.mediapicker.VideoViewHolder

class PreviewFragment: AbstractFragment() {

    private val mViewModel by viewModels<PreviewFragmentViewModel>()
    private lateinit var mBinding: FragmentPreviewBinding

    //    private var mVideoMark: View? = null

    private var mThumbnailBean: MediaBean? = null
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

        if (MediaType.isImage(mThumbnailBean?.type ?: -1)) {
            //图片类型
            mBinding.idIvImage.visibility = View.VISIBLE
            mBinding.idIvImage.load(mThumbnailBean?.uri?:mThumbnailBean?.path?:"")
        } else if (MediaType.isVideo(mThumbnailBean?.type ?: -1)) {
            //视频类型
            mBinding.idIvImage?.visibility = View.GONE

            mVideoViewHolder = VideoViewHolder()
            mVideoViewHolder?.initVideo(mBinding.idVideoView, mThumbnailBean?.uri, mThumbnailBean?.path,  mBinding.idIvVideoController)
        }

//        if (mThumbnailBean?.isAutoPlay == true) {
//            mVideoViewHolder?.play()
//        }
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

    fun setData(thumbnailBean: MediaBean?) {
        mThumbnailBean = thumbnailBean
    }
}

class PreviewFragmentViewModel: ViewModel() {

}