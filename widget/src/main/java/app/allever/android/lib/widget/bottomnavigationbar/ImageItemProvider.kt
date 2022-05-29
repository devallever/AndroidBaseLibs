package app.allever.android.lib.widget.bottomnavigationbar

import android.view.View
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ImageItemProvider(val mConfig: BottomNavigationBar.Config) :
    BaseItemProvider<NavigationBarItem>() {
    override val itemViewType: Int
        get() = NavigationBarItem.TYPE_IMG
    override val layoutId: Int
        get() = R.layout.navigation_bar_item_img

    override fun convert(helper: BaseViewHolder, item: NavigationBarItem) {
        val iconSize = mConfig.singleIconSize
        if (iconSize > 0) {
            val ivIcon = helper.getView<View>(R.id.ivIcon)
            ivIcon.post {
                val ivIconLp = ivIcon.layoutParams
                ivIconLp.width = iconSize
                ivIconLp.height = iconSize
                ivIcon.layoutParams = ivIconLp
            }
        }
    }
}