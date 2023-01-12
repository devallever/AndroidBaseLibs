package app.allever.android.lib.widget.demo.adapter

import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.RvItemUserBinding
import app.allever.android.lib.widget.demo.adapter.bean.UserItem
import app.allever.android.lib.widget.recycler.RefreshRVAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class UserItemAdapter() :
    RefreshRVAdapter<UserItem, BaseViewHolder>(
        R.layout.rv_item_user
    ) {

    override fun convert(holder: BaseViewHolder, item: UserItem) {
        val binding = RvItemUserBinding.bind(holder.itemView)
        binding.tvNickname.text = item.nickname
    }
}