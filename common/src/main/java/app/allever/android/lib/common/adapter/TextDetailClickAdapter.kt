package app.allever.android.lib.common.adapter

import android.graphics.Canvas
import android.view.ViewGroup
import app.allever.android.lib.common.R
import app.allever.android.lib.common.adapter.bean.TextClickItem
import app.allever.android.lib.common.adapter.bean.TextDetailClickItem
import app.allever.android.lib.common.databinding.RvTextDetailItemBinding
import app.allever.android.lib.core.helper.ViewHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TextDetailClickAdapter :
    BaseQuickAdapter<TextDetailClickItem, BaseViewHolder>(R.layout.rv_text_detail_item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        setOnItemClickListener { adapter, view, position ->
            val item = (adapter.data[position] as? TextClickItem)
            item?.itemClick?.invoke(item)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun convert(holder: BaseViewHolder, item: TextDetailClickItem) {
        val binding = RvTextDetailItemBinding.bind(holder.itemView)
        binding.tvText.text = item.title
        binding.tvTextDetail.text = item.detail
        ViewHelper.setVisible(binding.tvTextDetail, item.detail.isNotEmpty())
    }
}