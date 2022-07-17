package app.allever.android.lib.widget.mediapicker.ui.adapter

import android.view.ViewGroup
import app.allever.android.lib.core.function.imageloader.load
import app.allever.android.lib.core.function.media.MediaType
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.util.TimeUtils
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.RvItemImageVideoBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * 图片/视频列表的适配器
 */
class ImageVideoAdapter :
    BaseQuickAdapter<MediaItem, BaseDataBindingHolder<RvItemImageVideoBinding>>(R.layout.rv_item_image_video) {

    companion object {
        private val TAG = ImageVideoAdapter::class.java.simpleName
    }

    private var mItemWidth = 0

    init {
        val screenWidth = DisplayHelper.getScreenWidth()
        val margin = DisplayHelper.dip2px(4)
        mItemWidth = ((screenWidth - margin * 5) / 4f).roundToInt()
    }

    private var mClickListener: ClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseDataBindingHolder<RvItemImageVideoBinding> {
        val holder = super.onCreateViewHolder(parent, viewType)
        val itemView = holder.itemView
        val lp = itemView.layoutParams
//        lp.width = mItemWidth.toInt()
        //根据宽高决定高度
        lp.height = mItemWidth
        itemView.layoutParams = lp
        itemView.tag = holder
        return holder
    }


    fun setOptionListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    private fun checkSelected(holder: BaseViewHolder, item: MediaItem) {
        if (item.isChecked) {
            holder.setVisible(R.id.ivMask, true)
            holder.setVisible(R.id.ivSelect, true)
        } else {
            holder.setVisible(R.id.ivMask, false)
            holder.setVisible(R.id.ivSelect, false)
        }
    }

    override fun convert(holder: BaseDataBindingHolder<RvItemImageVideoBinding>, item: MediaItem) {
        val binding = holder.dataBinding ?: return
        val position = data.indexOf(item)
        if (MediaType.isVideo(item.data.type)) {
            holder.setVisible(R.id.tvDuration, true)
            holder.setText(
                R.id.tvDuration,
                TimeUtils.formatTime(item.data.duration, TimeUtils.FORMAT_mm_ss)
            )
            CoroutineHelper.MAIN.launch {
//                val bitmap = MediaStore.Images.Media.getBitmap(App.context.contentResolver, item.data.uri);
//                val bitmap = BitmapFactory.decodeStream(App.context.contentResolver.openInputStream(item.data.uri!!));
//                binding.ivImg.load(bitmap)
                binding.ivImg.load(item.data.uri ?: item.data.path)
            }
        } else {
            holder.setVisible(R.id.tvDuration, false)
            binding.ivImg.load(item.data.uri ?: item.data.path)
        }

        binding.root.setOnClickListener {
            item.isChecked = !item.isChecked
            mClickListener?.onItemClick(item, position)
            setData(position, item)
        }

        holder.itemView.setOnLongClickListener {
            mClickListener?.onItemLongClick(item, position)
            return@setOnLongClickListener true
        }

        checkSelected(holder, item)
    }

}