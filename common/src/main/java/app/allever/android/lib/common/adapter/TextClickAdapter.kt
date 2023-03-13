package app.allever.android.lib.common.adapter

import android.view.ViewGroup
import app.allever.android.lib.common.R
import app.allever.android.lib.common.adapter.bean.TextClickItem
import app.allever.android.lib.common.databinding.RvTextItemBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TextClickAdapter :
    BaseQuickAdapter<TextClickItem, BaseViewHolder>(R.layout.rv_text_item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        setOnItemClickListener { adapter, view, position ->
            val item = (adapter.data[position] as? TextClickItem)
            item?.itemClick?.invoke(item)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun convert(holder: BaseViewHolder, item: TextClickItem) {
        val binding = RvTextItemBinding.bind(holder.itemView)
        binding.tvText.text = item.title
    }
}