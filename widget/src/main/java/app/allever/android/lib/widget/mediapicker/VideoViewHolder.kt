package app.allever.android.lib.widget.mediapicker

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.widget.R

class VideoViewHolder
    : View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener {

    companion object {
        private val TAG = VideoViewHolder::class.java.simpleName
        private const val ALPHA_VALUE_OPAQUE = 1F
    }

    private var mIvPlay: ImageView? = null
    private var mVideoView: VideoView? = null
    private var mPath: String? = null
    private var mUri: Uri? = null

    private var mAlphaAnimator: ObjectAnimator? = null

    private var mAnimListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
//            DLog.d(TAG, "onAnimationRepeat")
        }

        override fun onAnimationEnd(animation: Animator?) {
//            DLog.d(TAG, "onAnimationEnd")
        }

        override fun onAnimationCancel(animation: Animator?) {
//            DLog.d(TAG, "onAnimationCancel")
            mIvPlay?.alpha =
                ALPHA_VALUE_OPAQUE
            mIvPlay?.visibility = View.VISIBLE
        }

        override fun onAnimationStart(animation: Animator?) {
            mIvPlay?.alpha =
                ALPHA_VALUE_OPAQUE
//            DLog.d(TAG, "onAnimationStart")
        }
    }


    init {
//        initVideoView()
    }

    private fun initVideoView() {
        mIvPlay?.setOnClickListener(this)
        mVideoView?.setOnTouchListener(this)
        mVideoView?.setOnCompletionListener(this)
        //处理开始播放时的短暂黑屏
        mVideoView?.setOnPreparedListener(this)
        mVideoView?.setVideoURI(mUri)
        mIvPlay?.visibility = View.VISIBLE
        mIvPlay?.setImageResource(R.drawable.icon_album_video_preview_play)
        mVideoView?.visibility = View.VISIBLE

        mAlphaAnimator = ObjectAnimator.ofFloat(mIvPlay, "alpha", 1f, 0f)
        mAlphaAnimator?.startDelay = 1000
        mAlphaAnimator?.duration = 1000
        mAlphaAnimator?.addListener(mAnimListener)
    }

    fun initVideo(videoView: VideoView?, path: String?, ivPlay: ImageView?) {
        mIvPlay = ivPlay
        mVideoView = videoView
        mPath = path
        initVideoView()
    }

    fun initVideo(videoView: VideoView?, uri: Uri?, path: String?, ivPlay: ImageView?) {
        mIvPlay = ivPlay
        mVideoView = videoView
        mUri = uri
        mPath = path
        initVideoView()
    }

    fun play() {
//        mVideoView?.start()
//        mVideoView?.visibility = View.VISIBLE
//        mIvPlay?.visibility = View.GONE
        checkAndPlay()
    }

    fun pause() {
        mVideoView?.pause()
        mIvPlay?.visibility = View.VISIBLE
        mIvPlay?.setImageResource(R.drawable.icon_album_video_preview_play)
        mIvPlay?.alpha = ALPHA_VALUE_OPAQUE
        mAlphaAnimator?.cancel()
    }

    fun stop() {
        mVideoView?.stopPlayback()
        mAlphaAnimator?.cancel()
    }

    fun destroy() {
        mIvPlay = null
        mVideoView = null
        mAlphaAnimator = null
    }

    private fun checkAndPlay() {
        val asyncTask = object : AsyncTask<Void, Void, Boolean>() {
            override fun doInBackground(vararg params: Void?): Boolean? {
                return checkVideoAvailable(mPath)
            }

            override fun onPostExecute(result: Boolean?) {
                if (result == true) {
                    mVideoView?.start()
                    mVideoView?.visibility = View.VISIBLE
//                    mIvPlay?.visibility = View.GONE
                    mIvPlay?.setImageResource(R.drawable.icon_album_video_preview_pause)
                    mAlphaAnimator?.start()
                } else {
                    toast("播放失败")
                }
            }
        }

        asyncTask.execute()
    }

    override fun onPrepared(it: MediaPlayer?) {

        //适应屏幕显示
        it?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)

        //显示第一帧
        mVideoView?.seekTo(100)
        it?.setOnInfoListener { mp, what, extra ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                mVideoView?.setBackgroundColor(Color.TRANSPARENT)
            }
            return@setOnInfoListener true
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mVideoView?.seekTo(100)
        mIvPlay?.visibility = View.VISIBLE
        mIvPlay?.setImageResource(R.drawable.icon_album_video_preview_play)
        mAlphaAnimator?.cancel()
        mIvPlay?.alpha = ALPHA_VALUE_OPAQUE
    }

    override fun onClick(v: View?) {
        when (v) {
            mIvPlay -> {
                if (mVideoView?.isPlaying == true) {
                    pause()
                } else {
                    play()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            mVideoView -> {
                pause()
                return true
            }
        }
        return false
    }

    private fun checkVideoAvailable(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }

        val cr = App.context.contentResolver
        var cursor: Cursor? = null
        try {
            cursor = cr.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Video.VideoColumns._ID,
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.VideoColumns.DURATION
                ),
                MediaStore.Video.VideoColumns.DATA + " = ? ", arrayOf(mPath),
                MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Video.VideoColumns._ID + " ASC"
            )

            if (cursor == null) {
                return false
            }

            if (cursor.moveToFirst()) {
                val durationIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                //有些文件后缀为视频格式，却不是视频文件，长度为0， 需要排除
                val time = cursor.getLong(durationIndex)
                if (time <= 0) {
                    return false
                }
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cursor?.close()
            return false
        }

        return true
    }

}