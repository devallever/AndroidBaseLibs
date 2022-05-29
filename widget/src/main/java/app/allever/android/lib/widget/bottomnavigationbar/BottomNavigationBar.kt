package app.allever.android.lib.widget.bottomnavigationbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.BottomNavigationBarBinding

class BottomNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private lateinit var mBinding: BottomNavigationBarBinding
    private var mAdapter: NavigationBarAdapter? = null

    private lateinit var config: Config
    private var mItemClickListener: ItemClickListener<NavigationBarItem>? = null

    fun config(): BottomNavigationBar {
        config = Config()
        return this
    }

    fun data(list: MutableList<NavigationBarItem>): BottomNavigationBar {
        config.data.clear()
        config.data.addAll(list)
        return this
    }

    fun selectColor(color: Int): BottomNavigationBar {
        config.selectColor = color
        return this
    }

    fun unSelectColor(color: Int): BottomNavigationBar {
        config.unSelectColor = color
        return this
    }

    /**
     * 设置字体大小
     */
    fun textSize(sp: Int): BottomNavigationBar {
        config.textSize = sp
        return this
    }

    /**
     * 设置字体大小
     */
    fun singleTextSize(sp: Int): BottomNavigationBar {
        config.singleTextSize = sp
        return this
    }

    /**
     * 设置图标大小
     */
    fun iconSize(dp: Int): BottomNavigationBar {
        config.iconSize = DisplayHelper.dip2px(dp)
        return this
    }

    fun singleIconSize(dp: Int) : BottomNavigationBar {
        config.singleIconSize = DisplayHelper.dip2px(dp)
        return this
    }

    fun backgroundColor(color: Int): BottomNavigationBar {
        config.backgroundColor = color
        setBackgroundColor(color)
        return this
    }

    fun itemClickListener(itemClickListener: ItemClickListener<NavigationBarItem>): BottomNavigationBar {
        mItemClickListener = itemClickListener
        return this
    }

    fun init() {
        initView()
    }

    fun setUnreadCount(position: Int, count: Int) {
        config.data[position].unreadCount = count
        mAdapter?.setData(position, config.data[position])
    }

    fun switchItem(position: Int) {
        mAdapter?.handleSwitchItem(position)
    }

    private fun initView() {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.bottom_navigation_bar,
            this,
            true
        )
        mAdapter = NavigationBarAdapter(config)
        mBinding.recyclerView.layoutManager = GridLayoutManager(context, config.data.size)
        mBinding.recyclerView.adapter = mAdapter
        mAdapter?.setList(config.data)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            mItemClickListener?.onItemClick(position, config.data[position])
        }
    }

    class Config {
        var selectColor: Int = 0
        var unSelectColor: Int = 0
        var data: MutableList<NavigationBarItem> = mutableListOf()
        var textSize: Int = 0
        var singleTextSize: Int = 0
        var iconSize: Int = 0
        var singleIconSize = 0
        var backgroundColor = 0
    }

    interface ItemClickListener<Item> {
        fun onItemClick(position: Int, item: Item)
    }

}