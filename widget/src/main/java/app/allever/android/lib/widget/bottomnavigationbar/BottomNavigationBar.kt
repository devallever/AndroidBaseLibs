package app.allever.android.lib.widget.bottomnavigationbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.BottomNavigationBarBinding

class BottomNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val mList = mutableListOf<NavigationBarItem>()
    private lateinit var mBinding: BottomNavigationBarBinding
    private var mAdapter: NavigationBarAdapter? = null

//    init {
//        initView()
//    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initData()
        initView()
    }


    private fun initData() {
        mList.add(
            NavigationBarItem(
                0,
                0,
                R.color.search_view_text_color,
                R.color.cardview_shadow_start_color,
                "1",
                true,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
        mList.add(
            NavigationBarItem(
                0,
                0,
                R.color.search_view_text_color,
                R.color.cardview_shadow_start_color,
                "2",
                false,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
        mList.add(
            NavigationBarItem(
                0,
                0,
                R.color.search_view_text_color,
                R.color.cardview_shadow_start_color,
                "3",
                false,
                0,
                NavigationBarItem.TYPE_IMG
            )
        )
        mList.add(
            NavigationBarItem(
                0,
                0,
                R.color.search_view_text_color,
                R.color.cardview_shadow_start_color,
                "4",
                false,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
        mList.add(
            NavigationBarItem(
                0,
                0,
                R.color.search_view_text_color,
                R.color.cardview_shadow_start_color,
                "5",
                false,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
    }

    private fun initView() {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.bottom_navigation_bar,
            this,
            true
        )
        mAdapter = NavigationBarAdapter()
        mBinding.recyclerView.layoutManager = GridLayoutManager(context, mList.size)
        mBinding.recyclerView.adapter = mAdapter
        mAdapter?.setList(mList)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            toast(mList[position].title)
        }
    }

}