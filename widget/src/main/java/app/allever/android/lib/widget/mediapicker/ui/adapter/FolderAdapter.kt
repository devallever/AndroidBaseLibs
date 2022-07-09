package app.allever.android.lib.widget.mediapicker.ui.adapter

import app.allever.android.lib.core.function.imageloader.load
import app.allever.android.lib.core.function.media.FolderBean
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.RvItemSlectFolderBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class FolderAdapter :
    BaseQuickAdapter<FolderBean, BaseDataBindingHolder<RvItemSlectFolderBinding>>(R.layout.rv_item_slect_folder) {
    override fun convert(
        holder: BaseDataBindingHolder<RvItemSlectFolderBinding>,
        item: FolderBean
    ) {
        val binding = holder.dataBinding ?: return
        binding.tvAudioCount.text = item.audioCount.toString()
        binding.tvPhotoCount.text = item.photoCount.toString()
        binding.tvVideoCount.text = item.videoCount.toString()
        binding.tvAlbumName.text = item.name
        if (item.coverMediaBean?.uri != null) {
            binding.ivImage.load(item.coverMediaBean?.uri!!)
        } else {
            binding.ivImage.load(R.drawable.ic_music_cover_bg)
        }
    }
}