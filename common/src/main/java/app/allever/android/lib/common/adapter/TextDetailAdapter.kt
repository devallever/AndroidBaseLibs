package app.allever.android.lib.common.adapter

import app.allever.android.lib.common.R
import app.allever.android.lib.common.adapter.bean.TextDetailItem
import app.allever.android.lib.common.databinding.RvTextDetailItemBinding
import app.allever.android.lib.core.helper.ViewHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TextDetailAdapter :
    BaseQuickAdapter<TextDetailItem, BaseViewHolder>(R.layout.rv_text_detail_item) {
    override fun convert(holder: BaseViewHolder, item: TextDetailItem) {
        val binding = RvTextDetailItemBinding.bind(holder.itemView)
        binding.tvText.text = item.title
        binding.tvTextDetail.text = item.detail
        ViewHelper.setVisible(binding.tvTextDetail, item.detail.isNotEmpty())
    }
}