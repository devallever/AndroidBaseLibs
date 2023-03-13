package app.allever.android.lib.common.adapter

import app.allever.android.lib.common.R
import app.allever.android.lib.common.databinding.RvTextItemBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TextAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_text_item) {
    override fun convert(holder: BaseViewHolder, item: String) {
        val binding = RvTextItemBinding.bind(holder.itemView)
        binding.tvText.text = item
    }
}