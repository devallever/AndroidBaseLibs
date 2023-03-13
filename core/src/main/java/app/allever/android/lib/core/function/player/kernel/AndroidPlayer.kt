package app.allever.android.lib.core.function.player.kernel

import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.Surface
import android.view.SurfaceHolder
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.function.player.kernel.internal.AbsPlayer
import app.allever.android.lib.core.function.player.kernel.internal.PlayerConstant
import app.allever.android.lib.core.helper.CoroutineHelper
import kotlinx.coroutines.launch

/**
 * MediaPlayer内核
 */
class AndroidPlayer : AbsPlayer() {
    private var mMediaPlayer: MediaPlayer? = null

    private var mIsPreparing = false
    private var mBufferedPercent = 0
    private var mContext = App.context

    override fun initPlayer() {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        initListener()
    }

    override fun setDataSource(path: String, headers: Map<String, String>?) {
        if (path.startsWith("http", true)) {
            //网络视频
            // 设置dataSource
            if (path.isEmpty()) {
                playerStatusListener?.onInfo(PlayerConstant.MEDIA_INFO_URL_NULL, 0)
                return
            }
            try {
                val uri = Uri.parse(path)
                mMediaPlayer?.reset()
                mMediaPlayer?.setDataSource(mContext, uri, headers)
            } catch (e: Exception) {
                e.printStackTrace()
                playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_PARSE, e.message)
            }
        } else {
            //本地视频
            try {
                mMediaPlayer?.reset()
                mMediaPlayer?.setDataSource(path)
            } catch (e: Exception) {
                e.printStackTrace()
                playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_PARSE, e.message)
            }
        }
    }

    override fun setDataSource(accessFileDescriptor: AssetFileDescriptor) {
        try {
            mMediaPlayer?.reset()
            mMediaPlayer?.setDataSource(
                accessFileDescriptor.fileDescriptor,
                accessFileDescriptor.startOffset,
                accessFileDescriptor.length
            )
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun setDataSource(uri: Uri, headers: Map<String, String>?) {
        try {
            mMediaPlayer?.reset()
            mMediaPlayer?.setDataSource(mContext, uri)
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_PARSE, e.message)
        }
    }

    override fun prepareAsync() {
        try {
            mIsPreparing = true
            mMediaPlayer?.prepareAsync()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun start() {
        try {
            mMediaPlayer?.start()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun pause() {
        try {
            mMediaPlayer?.pause()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun stop() {
        try {
            mMediaPlayer?.stop()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun reset() {
        mMediaPlayer?.reset()
        mMediaPlayer?.setSurface(null)
        mMediaPlayer?.setDisplay(null)
        mMediaPlayer?.setVolume(1f, 1f)
    }

    override fun isPlaying(): Boolean {
        return mMediaPlayer?.isPlaying ?: false
    }

    override fun seekTo(time: Long) {
        try {
            mMediaPlayer?.seekTo(time.toInt())
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun release() {
        clearListener()
        CoroutineHelper.IO.launch {
            try {
                //异步释放，防止卡顿
                mMediaPlayer?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getCurrentPosition(): Long {
        return mMediaPlayer?.currentPosition?.toLong() ?: 0L
    }

    override fun getDuration(): Long {
        return mMediaPlayer?.duration?.toLong() ?: 0L
    }

    override fun getBufferedPercentage(): Int {
        return mBufferedPercent
    }

    override fun setSurface(surface: Surface?) {
        surface?.let {
            try {
                mMediaPlayer?.setSurface(surface)
            } catch (e: Exception) {
                e.printStackTrace()
                playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
            }
        }
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     * @param holder                            holder
     */
    override fun setDisplay(holder: SurfaceHolder?) {
        try {
            mMediaPlayer?.setDisplay(holder)
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun setVolume(v1: Float, v2: Float) {
        try {
            mMediaPlayer?.setVolume(v1, v2)
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun setLooping(isLooping: Boolean) {
        try {
            mMediaPlayer?.isLooping = isLooping
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun setOptions() {

    }

    override fun setSpeed(speed: Float) {
        // only support above Android M
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                mMediaPlayer?.playbackParams =
                    mMediaPlayer?.playbackParams?.setSpeed(speed) ?: return
            } catch (e: Exception) {
                e.printStackTrace()
                playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
            }
        }
    }


    override fun getSpeed(): Float {
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return mMediaPlayer?.playbackParams?.speed ?: 1F
            } catch (e: Exception) {
                e.printStackTrace()
                playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
            }
        }
        return 1F
    }

    override fun getTcpSpeed(): Long {
        //不支持获取网速
        return 0
    }


    //完成监听器
    private val onCompletionListener =
        MediaPlayer.OnCompletionListener { playerStatusListener?.onCompletion() }

    //错误监听器
    private val mOnErrorListener = MediaPlayer.OnErrorListener { mp, what, extra ->
        playerStatusListener?.onError(
            PlayerConstant.ErrorType.TYPE_UNEXPECTED,
            "监听异常$what, extra: $extra"
        )
        true
    }

    //信息监听器
    private val mOnInfoListener =
        MediaPlayer.OnInfoListener { mp, what, extra -> //解决MEDIA_INFO_VIDEO_RENDERING_START多次回调问题
            if (what == PlayerConstant.MEDIA_INFO_VIDEO_RENDERING_START) {
                if (mIsPreparing) {
                    playerStatusListener?.onInfo(what, extra)
                    mIsPreparing = false
                }
            } else {
                playerStatusListener?.onInfo(what, extra)
            }
            true
        }

    //缓冲监听器
    private val mOnBufferingUpdateListener =
        MediaPlayer.OnBufferingUpdateListener { mp, percent -> mBufferedPercent = percent }

    //准备监听器
    private val mOnPreparedListener = MediaPlayer.OnPreparedListener {
        playerStatusListener?.onPrepared()
        start()
    }

    //视频大小监听器
    private val mOnVideoSizeChangedListener =
        MediaPlayer.OnVideoSizeChangedListener { mp, width, height ->
            val videoWidth = mp.videoWidth
            val videoHeight = mp.videoHeight
            if (videoWidth != 0 && videoHeight != 0) {
                playerStatusListener?.onVideoSizeChanged(videoWidth, videoHeight)
            }
        }

    private fun initListener() {
        mMediaPlayer?.setOnCompletionListener(onCompletionListener)
        mMediaPlayer?.setOnErrorListener(mOnErrorListener)
        mMediaPlayer?.setOnInfoListener(mOnInfoListener)
        mMediaPlayer?.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
        mMediaPlayer?.setOnPreparedListener(mOnPreparedListener)
        mMediaPlayer?.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener)
    }

    private fun clearListener() {
        mMediaPlayer?.setOnCompletionListener(null)
        mMediaPlayer?.setOnErrorListener(null)
        mMediaPlayer?.setOnInfoListener(null)
        mMediaPlayer?.setOnBufferingUpdateListener(null)
        mMediaPlayer?.setOnPreparedListener(null)
        mMediaPlayer?.setOnVideoSizeChangedListener(null)
    }
}