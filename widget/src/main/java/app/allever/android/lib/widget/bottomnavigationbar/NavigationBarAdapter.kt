package app.allever.android.lib.widget.bottomnavigationbar

import android.view.View
import com.chad.library.adapter.base.BaseProviderMultiAdapter

class NavigationBarAdapter(private val mConfig: BottomNavigationBar.Config) :
    BaseProviderMultiAdapter<NavigationBarItem>() {

    init {
        addItemProvider(ImageItemProvider(mConfig))
        addItemProvider(ImageTextItemProvider(mConfig))
    }

    override fun getItemType(data: List<NavigationBarItem>, position: Int): Int {
        return data[position].itemType
    }

    override fun setOnItemClick(v: View, position: Int) {
        super.setOnItemClick(v, position)
        if (data[position].itemType != NavigationBarItem.TYPE_IMG) {
            handleSwitchItem(position)
        }
    }

    fun handleSwitchItem(position: Int) {
        data.map {
            it.select = false
        }
        data[position].select = true
        setList(data)
    }
}