package app.allever.android.lib.common

import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import app.allever.android.lib.mvvm.base.BaseViewModel
import kotlinx.coroutines.launch

abstract class RefreshListFragment<DB : ViewBinding, VM : BaseViewModel, T> :
    ListFragment<DB, VM, T>() {
    override fun getList() = mutableListOf<T>()
    override fun init() {
        super.init()
        initRefreshLayout()
        lifecycleScope.launch {
            mAdapter?.setList(loadListData())
        }
    }

    private fun initRefreshLayout() {
        mBinding.smartRefreshLayout.setEnableLoadMore(enableLoadMore())
        mBinding.smartRefreshLayout.setEnableRefresh(enableRefresh())

        mBinding.smartRefreshLayout.setOnRefreshListener { refreshLayout ->
            lifecycleScope.launch {
                val list = loadListData(false)
                mAdapter?.setList(list)
                mBinding.smartRefreshLayout.finishRefresh(true)

            }
        }
        mBinding.smartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            lifecycleScope.launch {
                val list = loadListData(true)
                mAdapter?.addData(list)
                mBinding.smartRefreshLayout.finishLoadMore(true)
            }
        }
    }

    open fun enableLoadMore() = true
    open fun enableRefresh() = true

    abstract suspend fun loadListData(loadMore: Boolean = false): MutableList<T>
}