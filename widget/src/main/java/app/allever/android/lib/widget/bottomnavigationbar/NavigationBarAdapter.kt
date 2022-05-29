package app.allever.android.lib.widget.bottomnavigationbar

import android.view.View
import com.chad.library.adapter.base.BaseProviderMultiAdapter

class NavigationBarAdapter() :
    BaseProviderMultiAdapter<NavigationBarItem>() {

    init {
        addItemProvider(ImageItemProvider())
        addItemProvider(ImageTextItemProvider())
    }

    override fun getItemType(data: List<NavigationBarItem>, position: Int): Int {
        return data[position].itemType
    }

    override fun setOnItemClick(v: View, position: Int) {
        super.setOnItemClick(v, position)
        if (data[position].itemType != NavigationBarItem.TYPE_IMG) {
            handleSwitch(position)
        }
    }

    private fun handleSwitch(position: Int) {
        data.map {
            it.select = false
        }
        data[position].select = true
        setList(data)
    }
}