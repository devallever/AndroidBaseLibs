package app.allever.android.lib.player.kernel.ijk

import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.function.player.kernel.internal.AbsPlayer
import app.allever.android.lib.core.function.player.kernel.internal.PlayerConstant
import app.allever.android.lib.core.helper.CoroutineHelper
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/***
 * IJKPlayer内核
 */
class IJKPlayer : AbsPlayer() {

    private var mMediaPlayer: IjkMediaPlayer? = null

    private var mBufferedPercent = 0

    private var mContext = App.context

    override fun initPlayer() {
        mMediaPlayer = IjkMediaPlayer()
        //native日志
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_VERBOSE)
        setOptions()
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
                mMediaPlayer?.dataSource = path
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
                accessFileDescriptor.fileDescriptor
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

    override fun prepareAsync() {
        try {
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
            mMediaPlayer?.seekTo(time)
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
        return mMediaPlayer?.currentPosition ?: 0L
    }

    override fun getDuration(): Long {
        return mMediaPlayer?.duration ?: 0L
    }

    override fun getBufferedPercentage(): Int {
        return mBufferedPercent
    }

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
        try {
            mMediaPlayer?.setSpeed(speed)
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
    }

    override fun getSpeed(): Float {
        try {
            return mMediaPlayer?.getSpeed(0F) ?: 1F
        } catch (e: Exception) {
            e.printStackTrace()
            playerStatusListener?.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.message)
        }
        return 1F
    }

    override fun getTcpSpeed(): Long {
        return mMediaPlayer?.tcpSpeed ?: 0L
    }

    private val mOnCompletionListener =
        IMediaPlayer.OnCompletionListener { playerStatusListener?.onCompletion() }

    private val mOnErrorListener =
        IMediaPlayer.OnErrorListener { iMediaPlayer, framework_err, impl_err ->
            playerStatusListener?.onError(
                PlayerConstant.ErrorType.TYPE_UNEXPECTED,
                "监听异常$framework_err, extra: $impl_err"
            );
            true
        }

    private val mOnBufferingUpdateListener =
        IMediaPlayer.OnBufferingUpdateListener { iMediaPlayer, buffered ->
            mBufferedPercent = buffered
        }

    private val mOnPreparedListener = IMediaPlayer.OnPreparedListener {
        playerStatusListener?.onPrepared()
    }

    private val mOnInfoListener =
        IMediaPlayer.OnInfoListener { iMediaPlayer, what, extra ->
            playerStatusListener?.onInfo(what, what)
            true
        }

    private val mOnSizeChangedListener =
        IMediaPlayer.OnVideoSizeChangedListener { iMediaPlayer, width, height, sar_num, sar_den ->
            playerStatusListener?.onVideoSizeChanged(
                width,
                height
            )
        }

    private fun initListener() {
        mMediaPlayer?.setOnCompletionListener(mOnCompletionListener)
        mMediaPlayer?.setOnErrorListener(mOnErrorListener)
        mMediaPlayer?.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
        mMediaPlayer?.setOnPreparedListener(mOnPreparedListener)
        mMediaPlayer?.setOnInfoListener(mOnInfoListener)
        mMediaPlayer?.setOnVideoSizeChangedListener(mOnSizeChangedListener)
    }

    private fun clearListener() {
        mMediaPlayer?.setOnCompletionListener(null)
        mMediaPlayer?.setOnErrorListener(null)
        mMediaPlayer?.setOnBufferingUpdateListener(null)
        mMediaPlayer?.setOnPreparedListener(null)
        mMediaPlayer?.setOnInfoListener(null)
        mMediaPlayer?.setOnVideoSizeChangedListener(null)

    }
}