package app.allever.android.lib.core.function.media

import android.media.AudioAttributes
import android.media.MediaPlayer

/**
 * 播放/缓存
 */
class MusicPlayer {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var mIsPrepared = false
    private var mAutoPlay = false
    private var mDataSource = ""

    var onPrepareListener: (() -> Unit)? = null

    init {
        mediaPlayer.setOnPreparedListener {
            mIsPrepared = true
            if (mAutoPlay) {
                mediaPlayer.start()
            }
            onPrepareListener?.invoke()
        }
    }

    fun load(url: String, autoPlay: Boolean = false) {
        mAutoPlay = autoPlay
        mDataSource = url
        reset()
        try {
            val mAudioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            mediaPlayer.setAudioAttributes(mAudioAttributes)
            mediaPlayer.setDataSource(mDataSource)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun play() {
        if (mediaPlayer.isPlaying) {
            return
        }

        if (mIsPrepared) {
            mediaPlayer.start()
            return
        }

        load(mDataSource, true)
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun release() {
        mediaPlayer.release()
    }

    private fun reset() {
        try {
            mIsPrepared = false
            mediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}