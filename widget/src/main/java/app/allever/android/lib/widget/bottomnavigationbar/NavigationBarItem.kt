package app.allever.android.lib.widget.bottomnavigationbar

class NavigationBarItem() {
    var selectIcon: Int = 0
    var unSelectIcon: Int = 0
    var title: String = ""
    var select: Boolean = false
    var unreadCount: Int = 0
    var itemType = TYPE_IMG_TEXT

    constructor(
        selectIcon: Int = 0,
        unSelectIcon: Int = 0,
        title: String = "",
        select: Boolean = false,
        unreadCount: Int = 0,
        itemType: Int = TYPE_IMG_TEXT
    ) : this() {
        this.selectIcon = selectIcon
        this.unSelectIcon = unSelectIcon
        this.title = title
        this.select = select
        this.unreadCount = unreadCount
        this.itemType = itemType
    }

    companion object {
        const val TYPE_IMG_TEXT = 1
        const val TYPE_IMG = 2
    }
}