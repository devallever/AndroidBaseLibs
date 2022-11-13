package app.allever.android.lib.widget.demo

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.bottomnavigationbar.BottomNavigationBar
import app.allever.android.lib.widget.bottomnavigationbar.NavigationBarItem
import app.allever.android.lib.widget.databinding.ActivityRefreshRecyclerViewBinding
import app.allever.android.lib.widget.demo.adapter.UserItemAdapter
import app.allever.android.lib.widget.demo.adapter.bean.UserItem
import app.allever.android.lib.widget.recycler.RefreshRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RefreshRVActivity : AbstractActivity() {
    lateinit var mBinding: ActivityRefreshRecyclerViewBinding
    private val mAdapter =  UserItemAdapter()

    private lateinit var bottomNavigationBar: BottomNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_refresh_recycler_view, null, false)
        setContentView(mBinding.root)
        initView()
        initBottomNavigationData()
    }

    private fun initView() {
        bottomNavigationBar = findViewById(R.id.bottomNavigationBar)
//        mAdapter.refreshRV.setAdapter(mAdapter)
//            .dataFetchListener(object : RefreshRecyclerView.DataFetchListener<UserItem> {
//                override fun loadData(currentPage: Int, isLoadMore: Boolean) {
//                    loadUser(currentPage, isLoadMore)
//                }
//
//                override suspend fun fetchData(
//                    currentPage: Int,
//                    isLoadMore: Boolean
//                ): MutableList<UserItem> {
//                    return fetchUser(currentPage, isLoadMore)
//                }
//            })
//            .enableViewPager(true)
//            .pageChangeListener(object : RefreshRecyclerView.PageChangeListener<UserItem> {
//                override fun onPageChanged(position: Int, item: UserItem) {
//                    toast("position = $position, item = $item")
//                }
//            })


        //新方式
        mBinding.refreshRV.setAdapter(mAdapter)
            .dataFetchListener(object : RefreshRecyclerView.DataFetchListener<UserItem> {
                override fun loadData(currentPage: Int, isLoadMore: Boolean) {
                    loadUser(currentPage, isLoadMore)
                }

                override suspend fun fetchData(
                    currentPage: Int,
                    isLoadMore: Boolean
                ): MutableList<UserItem> {
                    return fetchUser(currentPage, isLoadMore)
                }
            })
            .enableViewPager(false)
            .enableRefresh(false)
            .preloadCount(5)
//            .pageChangeListener(object : RefreshRecyclerView.PageChangeListener<UserItem> {
//                override fun onPageChanged(position: Int, item: UserItem) {
//                    toast("position = $position, item = $item")
//                }
//            })
            .execute()


        mBinding.verticalDragView.setChildCanScrollView(mBinding.refreshRV?.recyclerView)
    }

    private fun initBottomNavigationData() {
        val mList = mutableListOf<NavigationBarItem>()
        mList.add(
            NavigationBarItem(
                R.drawable.ic_chatroom_checked,
                R.drawable.ic_chatroom_unchecked,
                "聊天室",
                true,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
        mList.add(
            NavigationBarItem(
                R.drawable.ic_chatroom_checked,
                R.drawable.ic_chatroom_unchecked,
                "私聊",
                false,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
        mList.add(
            NavigationBarItem(
                R.drawable.ic_chatroom_checked,
                R.drawable.ic_chatroom_unchecked,
                "3",
                false,
                0,
                NavigationBarItem.TYPE_IMG
            )
        )
        mList.add(
            NavigationBarItem(
                R.drawable.ic_chatroom_checked,
                R.drawable.ic_chatroom_unchecked,
                "聊圈",
                false,
                100,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )
        mList.add(
            NavigationBarItem(
                R.drawable.ic_chatroom_checked,
                R.drawable.ic_chatroom_unchecked,
                "消息",
                false,
                0,
                NavigationBarItem.TYPE_IMG_TEXT
            )
        )

        bottomNavigationBar.config()
            .data(mList)
            .selectColor(Color.parseColor("#ffffff"))
            .unSelectColor(Color.parseColor("#cccccc"))
            .textSize(12)//0就是默认，不改变
            .singleTextSize(0)
            .iconSize(0)
            .singleIconSize(0)
            .backgroundColor(Color.parseColor("#ff6c1e"))
            .itemClickListener(object : BottomNavigationBar.ItemClickListener<NavigationBarItem> {
                override fun onItemClick(position: Int, item: NavigationBarItem) {
//                    toast(item.title)
                }
            })
            .init()

        App.mainHandler.postDelayed({
            bottomNavigationBar.setUnreadCount(0, 3)
            bottomNavigationBar.switchItem(1)
        }, 1000)
    }

    fun loadUser(page: Int, isLoadMore: Boolean) {
        lifecycleScope.launch {
            delay(1000)
            val list = mutableListOf<UserItem>()
            for (i in 0..9) {
                val user = UserItem()
                user.nickname = "${i + page * 10}"
                user.id = i
                list.add(user)
            }
            if (isLoadMore) {
                mAdapter.refreshRV?.loadMoreData(list)
            } else {
                mAdapter.refreshRV?.refreshData(list)
            }
        }
    }

    suspend fun fetchUser(page: Int, isLoadMore: Boolean): MutableList<UserItem> =
        withContext(Dispatchers.IO) {
            delay(1000)
            val list = mutableListOf<UserItem>()
            for (i in 0..9) {
                val user = UserItem()
                user.nickname = "${i + page * 10}"
                user.id = i
                list.add(user)
            }
            list
        }
}
