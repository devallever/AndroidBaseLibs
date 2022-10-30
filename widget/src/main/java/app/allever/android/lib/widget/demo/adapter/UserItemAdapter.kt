package app.allever.android.lib.widget.demo.adapter

import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.RvItemUserBinding
import app.allever.android.lib.widget.demo.adapter.bean.UserItem
import app.allever.android.lib.widget.recycler.RefreshRVAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class UserItemAdapter() :
    RefreshRVAdapter<UserItem, BaseDataBindingHolder<RvItemUserBinding>>(
        R.layout.rv_item_user
    ) {
    override fun convert(holder: BaseDataBindingHolder<RvItemUserBinding>, item: UserItem) {
        val binding = holder.dataBinding?: return
        binding.tvNickname.text = item.nickname
    }
}