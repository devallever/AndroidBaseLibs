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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        mAdapter.refreshRV?.setAdapter(
            refreshRVAdapter = mAdapter,
            listener = object : RefreshRecyclerView.Listener {
                override fun loadData(currentPage: Int) {
                    loadUser(currentPage)
                }
            })
    }

    fun loadUser(page: Int = 0) {
        lifecycleScope.launch {
            delay(1000)
            val list = mutableListOf<UserItem>()
            for (i in 0..10) {
                val user = UserItem()
                user.nickname = "$i"
                user.id = i
                list.add(user)
            }
            if (page == 0) {
                mAdapter.refreshRV?.refreshData(list)
            } else {
                mAdapter.refreshRV?.loadMoreData(list)
            }
        }
    }
}
