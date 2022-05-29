package app.allever.android.lib.widget.bottomnavigationbar

import android.view.View
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ImageItemProvider: BaseItemProvider<NavigationBarItem>() {
    override val itemViewType: Int
        get() = NavigationBarItem.TYPE_IMG
    override val layoutId: Int
        get() = R.layout.navigation_bar_item_img

    override fun convert(helper: BaseViewHolder, item: NavigationBarItem) {
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