package app.allever.android.lib.widget.recycler

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 创建RefreshRVAdapter
 */
open class RefreshRVAdapter<Item, VH : BaseViewHolder>(
    val adapter: BaseQuickAdapter<Item, VH>,
    val refreshRV: RefreshRecyclerView<Item>
) {
}