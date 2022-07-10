package app.allever.android.lib.widget.mediapicker.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.RvItemAudioBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.SongMediaPlayer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class AudioAdapter :
    BaseQuickAdapter<MediaItem, BaseDataBindingHolder<RvItemAudioBinding>>(R.layout.rv_item_audio) {

    private var mClickListener: ClickListener? = null
    private lateinit var mBinding: RvItemAudioBinding

    private val unknownArtist = "未知"

    private var songPlayer = SongMediaPlayer()
    private var currentPlayItemStateInfo: MediaItem? = null
    private val onPlayerListener = object : SongMediaPlayer.OnPlayerListener {

        fun reset() {
            songPlayer.reset()
        }

        fun play() {
            val isi = currentPlayItemStateInfo
            if (isi != null) {
                songPlayer.play()
                isi.playing = true
                notifyDataSetChanged()
//                onItemListener?.play(isi)
            }
        }

        fun pause() {
            val isi = currentPlayItemStateInfo
            val position = data.indexOf(isi)
            if (isi != null  && position >= 0) {
                songPlayer.pause()
                isi.playing = false
                setData(position, isi)
//                notifyItemChanged(position, position)
            }
        }

        fun load(isi: MediaItem) {
            songPlayer.reset()
            val prevISI = currentPlayItemStateInfo
            val position = data.indexOf(prevISI)
            if (prevISI != null && prevISI != isi && position >= 0) {
                prevISI.playing = false
                prevISI.loaded = false
                setData(position, prevISI)
//                notifyItemChanged(prevISI.position, prevISI.position)
                songPlayer.reset()
            }
            currentPlayItemStateInfo = isi
            if (isi.loaded) {
                onPrepared()
            } else {
                songPlayer.load(context, isi.data.uri!!)
            }
        }

        override fun onPrepared() {
            currentPlayItemStateInfo?.loaded = true
            play()
        }

        override fun onCompletion() {
            val isi = currentPlayItemStateInfo
            val position = data.indexOf(isi)
            if (isi != null && position >= 0) {
                isi.playing = false
                setData(position, isi)
//                notifyItemChanged(isi.position, isi.position)
            }
        }

        override fun onError(err: String) {
        }

        override fun onProgress(time: Int) {


        }
    }


    init {
        songPlayer.addOnPlayerListener(onPlayerListener)
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseDataBindingHolder<RvItemAudioBinding>, item: MediaItem) {
        val position = data.indexOf(item)
        val binding = holder.dataBinding ?: return
        mBinding = binding
        binding.tvIndex.text = "${(position + 1)}"
        binding.tvTitle.text = item.data.musicTitle
        binding.tvInfo.text = "${item.data.musicArtist} - ${item.data.musicAlbum}"

        binding.ivSelect.setOnClickListener {
            item.isChecked = !item.isChecked
            val result = mClickListener?.onItemClick(item, position)
//            if (result == true) {
//                checkSelected(holder, item)
//            } else {
//                item.isChecked = false
//            }
            setData(position, item)
        }

        holder.itemView.setOnClickListener {
            if (!item.loaded) {
                onPlayerListener.load(item)
            } else {
                if (item.playing) {
                    onPlayerListener.pause()
                } else {
                    onPlayerListener.play()
                }
            }
        }

        holder.itemView.setOnLongClickListener {
            mClickListener?.onItemLongClick(item, position)
            return@setOnLongClickListener true
        }

        checkSelected(holder, item)

        if (item.playing) {
            binding.ivCover.setImageResource(R.drawable.icon_edit_music_online_pause)
            val magnify = 10000
            var toDegrees = 360f
            var duration = 1000L
            toDegrees *= magnify
            duration *= magnify
            val animation = RotateAnimation(
                0f, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            animation.duration = duration
            animation.repeatCount = Animation.INFINITE
            animation.interpolator = LinearInterpolator()
            binding.ivCover.startAnimation(animation)
        } else {
            binding.ivCover.setImageResource(R.drawable.icon_edit_music_online_play)
            binding.ivCover.clearAnimation()
        }
    }

    fun setOptionListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }


    private fun checkSelected(holder: BaseViewHolder, item: MediaItem) {
        if (item.isChecked) {
            holder.setImageResource(mBinding.ivSelect.id, R.drawable.icon_album_select)
            mBinding.ivSelect.colorFilter = null
        } else {
            holder.setImageResource(mBinding.ivSelect.id, R.drawable.icon_music_add)
            mBinding.ivSelect.setColorFilter(Color.parseColor("#666666"))
        }
    }

    fun release() {
        try {
            songPlayer.reset()
            songPlayer.release()
            currentPlayItemStateInfo = null
        } catch (e: Exception) {
            logE("error: release: ${e.message}")

        }
    }
}