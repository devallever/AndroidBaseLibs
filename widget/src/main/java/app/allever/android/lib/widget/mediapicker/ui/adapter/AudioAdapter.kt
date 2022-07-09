package app.allever.android.lib.widget.mediapicker.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.RvItemAudioBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class AudioAdapter :
    BaseQuickAdapter<MediaItem, BaseDataBindingHolder<RvItemAudioBinding>>(R.layout.rv_item_audio) {

    private var mClickListener: ClickListener? = null
    private lateinit var mBinding: RvItemAudioBinding

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

        holder.itemView.setOnLongClickListener {
            mClickListener?.onItemLongClick(item, position)
            return@setOnLongClickListener true
        }

        checkSelected(holder, item)
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


}