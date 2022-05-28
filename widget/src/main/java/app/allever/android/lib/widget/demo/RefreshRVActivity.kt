package app.allever.android.lib.widget.demo

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.demo.adapter.UserItemAdapter
import app.allever.android.lib.widget.demo.adapter.bean.UserItem
import app.allever.android.lib.widget.recycler.RefreshRVAdapter
import app.allever.android.lib.widget.recycler.RefreshRecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RefreshRVActivity : AbstractActivity() {
    private val mAdapter: RefreshRVAdapter<UserItem, BaseViewHolder> by lazy {
        RefreshRVAdapter(UserItemAdapter() as BaseQuickAdapter<UserItem, BaseViewHolder>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_recycler_view)
        initView()
    }

    private fun initView() {
        mAdapter.bindRv(findViewById(R.id.refreshRV))
        mAdapter.refreshRV.setAdapter(mAdapter, object : RefreshRecyclerView.Listener<UserItem> {
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
                mAdapter.refreshRV.loadMoreData(list)
            } else {
                mAdapter.refreshRV.refreshData(list)
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
