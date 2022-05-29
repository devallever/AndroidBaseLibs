package app.allever.android.lib.widget.bottomnavigationbar

import android.view.View
import androidx.core.content.res.ResourcesCompat
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ImageTextItemProvider : BaseItemProvider<NavigationBarItem>() {
    override val itemViewType: Int
        get() = NavigationBarItem.TYPE_IMG_TEXT
    override val layoutId: Int
        get() = R.layout.navigation_bar_item

    override fun convert(helper: BaseViewHolder, item: NavigationBarItem) {
        helper.setText(R.id.tvText, item.title)
        if (item.select) {
            helper.setImageResource(R.id.ivIcon, (item.selectIcon))
            helper.setTextColor(
                R.id.tvText,
                ResourcesCompat.getColor(
                    context.resources,
                    item.selectColor,
                    null
                )
            )
        } else {
            helper.setImageResource(R.id.ivIcon, (item.unSelectIcon))
            helper.setTextColor(
                R.id.tvText,
                ResourcesCompat.getColor(
                    context.resources,
                    item.unSelectColor,
                    null
                )
            )
        }

        ViewHelper.setVisible(helper.getView(R.id.tvUnread), item.unreadCount != 0)
    }

//    override fun onClick(
//        helper: BaseViewHolder,
//        view: View,
//        data: NavigationBarItem,
//        position: Int
//    ) {
//        toast(data.title)
//    }
}