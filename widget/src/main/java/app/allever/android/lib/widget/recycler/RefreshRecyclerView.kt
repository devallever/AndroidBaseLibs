package app.allever.android.lib.widget.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader

class RefreshRecyclerView<Item> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var recyclerView: RecyclerView? = null
    var refreshLayout: SmartRefreshLayout? = null
    var refreshRVAdapter: RefreshRVAdapter<Item, BaseViewHolder>? = null
    var list = mutableListOf<Item>()

    private var mCurrentPage = 0
    private var mListener: Listener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.refresh_recycler_view, this)
        initView()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        refreshLayout = findViewById(R.id.smartRefreshLayout)
        refreshLayout?.setOnLoadMoreListener {
            mCurrentPage++
            mListener?.loadData(mCurrentPage)
        }

        refreshLayout?.setOnRefreshListener {
            mCurrentPage = 0
            mListener?.loadData(mCurrentPage)
        }
    }

    fun setAdapter(
        refreshRVAdapter: RefreshRVAdapter<Item, BaseViewHolder>,
        header: RefreshHeader? = null,
        footer: RefreshFooter? = null,
        layoutManager: RecyclerView.LayoutManager? = null,
        emptyResId: Int = R.layout.rv_empty_view,
        listener: Listener? = null,
    ) {
        recyclerView?.layoutManager = layoutManager ?: LinearLayoutManager(context)
        recyclerView?.adapter = refreshRVAdapter.adapter
        header?.let {
            refreshLayout?.setRefreshHeader(header)
        }
        footer?.let {
            refreshLayout?.setRefreshFooter(footer)
        }
        refreshRVAdapter.adapter?.setList(list)
        refreshRVAdapter.adapter?.setEmptyView(emptyResId)
        this.refreshRVAdapter = refreshRVAdapter
        this.mListener = listener
        this.mCurrentPage = 0
        this.mListener?.loadData(mCurrentPage)
    }


    interface Listener {
        fun loadData(currentPage: Int) {}
    }

    fun loadMoreData(list: MutableList<Item>) {
        refreshRVAdapter?.adapter?.data?.addAll(list)
        refreshRVAdapter?.adapter?.notifyDataSetChanged()
        refreshLayout?.finishLoadMore(true)
    }

    fun refreshData(list: MutableList<Item>) {
        refreshRVAdapter?.adapter?.setList(list)
        refreshLayout?.finishRefresh(true)
    }

}