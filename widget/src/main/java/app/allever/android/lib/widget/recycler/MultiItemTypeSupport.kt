package app.allever.android.lib.widget.recycler

interface MultiItemTypeSupport<T> {
    fun getLayoutId(itemType: Int): Int
    fun getItemViewType(position: Int, t: T): Int
}