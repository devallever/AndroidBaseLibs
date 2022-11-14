package app.allever.android.lib.widget.recycler

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 创建RefreshRVAdapter
 */
abstract class RefreshRVAdapter<Item, VH : BaseViewHolder>(
    layoutId: Int,
    data: MutableList<Item>? = null
) :
    BaseQuickAdapter<Item, VH>(layoutId, data) {

    var refreshRV: RefreshRecyclerView<Item>? = null
}