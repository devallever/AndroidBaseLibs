package app.allever.android.lib.widget.bottomnavigationbar

import android.view.View
import android.widget.TextView
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ImageTextItemProvider(private var mConfig: BottomNavigationBar.Config) :
    BaseItemProvider<NavigationBarItem>() {
    override val itemViewType: Int
        get() = NavigationBarItem.TYPE_IMG_TEXT
    override val layoutId: Int
        get() = R.layout.navigation_bar_item_img_text

    override fun convert(helper: BaseViewHolder, item: NavigationBarItem) {
        helper.setText(R.id.tvText, item.title)
        val textSize = mConfig.textSize.toFloat()
        if (textSize > 0) {
            helper.getView<TextView>(R.id.tvText).textSize = textSize
        }
        val iconSize = mConfig.iconSize
        if (iconSize > 0) {
            val ivIcon = helper.getView<View>(R.id.ivIcon)
            ivIcon.post {
                val ivIconLp = ivIcon.layoutParams
                ivIconLp.width = iconSize
                ivIconLp.height = iconSize
                ivIcon.layoutParams = ivIconLp
            }
        }

        val unreadCount = item.unreadCount
        val unreadCountText = if (unreadCount <= 99) {
            unreadCount.toString()
        } else {
            "99+"
        }
        if (item.select) {
            helper.setImageResource(R.id.ivIcon, (item.selectIcon))
            helper.setTextColor(
                R.id.tvText, mConfig.selectColor
            )
        } else {
            helper.setImageResource(R.id.ivIcon, (item.unSelectIcon))
            helper.setTextColor(
                R.id.tvText, mConfig.unSelectColor
            )
        }

        ViewHelper.setVisible(helper.getView(R.id.tvUnread), item.unreadCount != 0)
        helper.setText(R.id.tvUnread, unreadCountText)
    }
}