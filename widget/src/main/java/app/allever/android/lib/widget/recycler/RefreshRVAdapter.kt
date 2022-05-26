package app.allever.android.lib.widget.recycler

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class RefreshRVAdapter<Item, VH : BaseViewHolder>() {
    var adapter: BaseQuickAdapter<Item, VH>? = null
    var refreshRV: RefreshRecyclerView<Item>? = null

    constructor(adapter: BaseQuickAdapter<Item, VH>) : this() {
        this.adapter = adapter
    }

    fun bindRv(refreshRecyclerView: RefreshRecyclerView<Item>) {
        this.refreshRV = refreshRecyclerView
    }
}