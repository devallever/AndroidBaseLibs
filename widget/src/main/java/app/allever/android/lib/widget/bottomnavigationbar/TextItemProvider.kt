package app.allever.android.lib.widget.bottomnavigationbar

import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TextItemProvider(private var mConfig: BottomNavigationBar.Config) : BaseItemProvider<NavigationBarItem>() {
    override val itemViewType: Int
        get() = NavigationBarItem.TYPE_TEXT
    override val layoutId: Int
        get() = R.layout.navigation_bar_item_text

    override fun convert(helper: BaseViewHolder, item: NavigationBarItem) {
        helper.setText(R.id.tvText, item.title)
        val unreadCount = item.unreadCount
        val unreadCountText = if (unreadCount <= 99) {
            unreadCount.toString()
        } else {
            "99+"
        }
        if (item.select) {
//            helper.setImageResource(R.id.ivIcon, (item.selectIcon))
            helper.setTextColor(
                R.id.tvText, mConfig.selectColor
            )
        } else {
//            helper.setImageResource(R.id.ivIcon, (item.unSelectIcon))
            helper.setTextColor(
                R.id.tvText, mConfig.unSelectColor
            )
        }

        ViewHelper.setVisible(helper.getView(R.id.tvUnread), item.unreadCount != 0)
        helper.setText(R.id.tvUnread, unreadCountText)
    }


}