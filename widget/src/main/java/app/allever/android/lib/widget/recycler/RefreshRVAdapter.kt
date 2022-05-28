package app.allever.android.lib.widget.recycler

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class RefreshRVAdapter<Item, VH : BaseViewHolder>() {
    lateinit var adapter: BaseQuickAdapter<Item, VH>
    lateinit var refreshRV: RefreshRecyclerView<Item>

    constructor(adapter: BaseQuickAdapter<Item, VH>) : this() {
        this.adapter = adapter
    }

    fun bindRv(refreshRecyclerView: RefreshRecyclerView<Item>) {
        this.refreshRV = refreshRecyclerView
    }
}