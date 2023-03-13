package app.allever.android.lib.common.adapter.bean

open class TextClickItem(
    var title: String = "",
    var id: Int = 0,
    var itemClick: ((item: TextClickItem) -> Unit)? = null
) {
}