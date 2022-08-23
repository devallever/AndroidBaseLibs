package app.allever.android.lib.core.function.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import app.allever.android.lib.core.function.work.PollingTask2
import java.util.*

class SongMediaPlayer(onPlayerListener: OnPlayerListener? = null) {
    interface OnPlayerListener {
        fun onPrepared()
        fun onCompletion()
        fun onError(err: String)
        fun onProgress(time: Int)
    }

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    private var isPrepared = false

    private val playerListener = object :
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener {
        override fun onCompletion(mp: MediaPlayer?) {
            onPlayerListeners.map {
                it.onCompletion()
            }
        }

        override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            notifyError("error: what: $what, extra: $extra")
            return true
        }

        override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            return false
        }

        override fun onPrepared(mp: MediaPlayer?) {
            isPrepared = true
            onPlayerListeners.map {
                it.onPrepared()
            }
        }

        override fun onSeekComplete(mp: MediaPlayer?) {
        }
    }

    private var onPlayerListeners = Collections.synchronizedList(ArrayList<OnPlayerListener>())
    private val WHAT_PROGRESS = 1
    private var handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            WHAT_PROGRESS -> {
                onPlayerListeners?.map {
                    it.onProgress(mediaPlayer.currentPosition)
                    notifyMsgProgress()
                }
            }
        }
        false
    }

    init {
        mediaPlayer.setOnCompletionListener(playerListener)
        mediaPlayer.setOnErrorListener(playerListener)
        mediaPlayer.setOnInfoListener(playerListener)
        mediaPlayer.setOnPreparedListener(playerListener)
        mediaPlayer.setOnSeekCompleteListener(playerListener)

        onPlayerListener?.let {
            onPlayerListeners.add(it)
        }
    }

    fun addOnPlayerListener(listener: OnPlayerListener) {
        if (!onPlayerListeners.contains(listener)) {
            onPlayerListeners.add(listener)
        }
    }

    fun removePlayerListener(listener: OnPlayerListener) {
        onPlayerListeners.remove(listener)
    }

    fun load(path: String) {
        reset()
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            notifyError("error: load: ${e.message}")
            e.printStackTrace()
        }
    }

    fun load(context: Context, uri: Uri) {
        reset()
        try {
            mediaPlayer.setDataSource(context, uri)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            notifyError("error: load: ${e.message}")
            e.printStackTrace()
        }
    }

    fun reset() {
        try {
            isPrepared = false
            mediaPlayer.reset()
            notifyMsgProgress(true)
        } catch (e: Exception) {
            notifyError("error: reset: ${e.message}")
        }
    }

    fun isPlaying(): Boolean {
        return try {
            mediaPlayer.isPlaying
        } catch (e: Exception) {
            notifyError("error: isPlaying: ${e.message}")
            false
        }
    }

    fun play() {
        try {
            PollingTask2(null,200, condition = { isPrepared }, execute = {
                mediaPlayer.start()
                notifyMsgProgress()
            }).start()
        } catch (e: Exception) {
            notifyError("error: start: ${e.message}")
        }
    }

    fun stop() {
        try {
            mediaPlayer.stop()
            notifyMsgProgress(true)
        } catch (e: Exception) {
            notifyError("error: stop: ${e.message}")
        }
    }

    fun pause() {
        try {
            mediaPlayer.pause()
            notifyMsgProgress(true)
        } catch (e: Exception) {
            notifyError("error: pause: ${e.message}")
        }
    }

    fun resume() {
        try {
            mediaPlayer.start()
            notifyMsgProgress()
        } catch (e: Exception) {
            notifyError("error: resume: ${e.message}")
        }
    }

    fun release() {
        try {
            notifyMsgProgress(true)
            mediaPlayer.reset()
            mediaPlayer.release()
        } catch (e: Exception) {
            notifyError("error: release: ${e.message}")
        }
    }

    fun seekTo(offsetTime: Int) {
        try {
            mediaPlayer.seekTo(offsetTime)
            notifyProgress(offsetTime)
        } catch (e: Exception) {
            notifyError("error: seekTo: ${e.message}")
        }
    }

    /***
     * @param volume [0 - 255]
     */
    fun setVolume(volume: Int) {
        try {
            //比例 0-1
            val volumeRatio = volume.toFloat() / 255
            mediaPlayer.setVolume(volumeRatio, volumeRatio)

        } catch (e: Exception) {
            notifyError("error: volume: ${e.message}")
        }
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    private fun notifyMsgProgress(remove: Boolean = false) {
        handler.removeMessages(WHAT_PROGRESS)
        if (!remove) {
            handler.sendEmptyMessageDelayed(WHAT_PROGRESS, 100)
        }
    }

    private fun notifyProgress(time: Int) {
        onPlayerListeners.map {
            it.onProgress(time)
        }
    }

    private fun notifyError(info: String) {
        onPlayerListeners.map {
            it.onError(info)
        }
    }

}