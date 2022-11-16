package app.allever.android.lib.core.function.media

import android.media.AudioAttributes
import android.media.MediaPlayer
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.network.HttpHelper
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.util.FileUtils
import app.allever.android.lib.core.util.MD5
import kotlinx.coroutines.launch
import java.io.File


/**
 * 播放/缓存
 */
class MusicPlayer {

    //文件格式 url -> md5
    //路径
    private var mDir = "${App.context.externalCacheDir}${File.separator}audio"

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var mIsPrepared = false
    private var mAutoPlay = false
    private var mLoop = false
    private var mDataSource = ""

    var onPrepareListener: (() -> Unit)? = null

    init {
        createCacheDir()

        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        mediaPlayer.setOnPreparedListener {
            mIsPrepared = true
            if (mAutoPlay) {
                mediaPlayer.start()
            }
            onPrepareListener?.invoke()
        }
    }

    private fun createCacheDir() {
        CoroutineHelper.IO.launch {
            if (!FileUtils.checkExist(mDir)) {
                try {
                    File(mDir).mkdirs()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun load(url: String, autoPlay: Boolean = false, loop: Boolean = false) {
        if (!url.startsWith("http")) {
            toast("暂不支持")
            return
        }
        mAutoPlay = autoPlay
        mDataSource = url
        mLoop = false
        reset()
        try {
            val mAudioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            mediaPlayer.setAudioAttributes(mAudioAttributes)

            val filePath = getCache()
            if (filePath.isNotEmpty()) {
                log("使用缓存: $filePath")
            }

            val source = filePath.ifEmpty {
                mDataSource
            }

            mediaPlayer.setDataSource(source)
            mediaPlayer.prepareAsync()

            if (filePath.isEmpty()) {
                val fileName = "${MD5.getMD5Str(mDataSource)}"
                val path = "$mDir${File.separator}$fileName"
                cacheData(mDataSource, path)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun play(loop: Boolean = false) {
        if (mediaPlayer.isPlaying) {
            return
        }

        if (mIsPrepared) {
            mediaPlayer.start()
            return
        }

        load(mDataSource, true, loop)
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
            mLoop = false
            mAutoPlay = false
            mediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCache(): String {

        val result = ""

        val fileCachePath = "$mDir${File.separator}${MD5.getMD5Str(mDataSource)}"
        if (FileUtils.checkExist(fileCachePath)) {
            return fileCachePath
        }
        return result
    }

    private fun cacheData(url: String, path: String) {
        HttpHelper.downloadFile(url, path)
    }

    private fun log(msg: String) {
        log("MusicPlayer", msg)
    }
}