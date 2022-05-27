package app.allever.android.lib.widget.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader

/**
 * 下拉刷新/上拉加载/预加载的RecyclerView
 */
class RefreshRecyclerView<Item> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val TAG = RefreshRecyclerView::class.java.simpleName

    var recyclerView: RecyclerView? = null
    var refreshLayout: SmartRefreshLayout? = null
    var refreshRVAdapter: RefreshRVAdapter<Item, BaseViewHolder>? = null
    var list = mutableListOf<Item>()

    private var mCurrentPage = 0
    private var mListener: Listener? = null
    private var mPreLoadCount: Int = 5
    private var mIsPreLoading = false

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

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 获取 LayoutManger
                val layoutManager = recyclerView.layoutManager
                // 如果 LayoutManager 是 LinearLayoutManager
                if (layoutManager is LinearLayoutManager) {
                    // 如果列表正在往上滚动，并且表项最后可见表项索引值 等于 预加载阈值
                    if (dy > 0 && layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1 - mPreLoadCount) {
                        //
                        if (mIsPreLoading) {
                            return
                        }

                        mCurrentPage++
//                        toast("预加载")
                        log("预加载第 $mCurrentPage 页")
                        mListener?.loadData(mCurrentPage)
                        mIsPreLoading = true
                        postDelayed({
                            mIsPreLoading = false
                        }, 2000)
                    }
                }
            }
        })
    }

    /***
     * @param refreshRVAdapter
     * @param header 刷新头
     * @param footer 加载底部
     * @param layoutManager
     * @param emptyResId 空布局
     * @param preLoadCount 预加载阈值
     * @param listener 刷新/加载监听器
     */
    fun setAdapter(
        refreshRVAdapter: RefreshRVAdapter<Item, BaseViewHolder>,
        header: RefreshHeader? = null,
        footer: RefreshFooter? = null,
        layoutManager: RecyclerView.LayoutManager? = null,
        emptyResId: Int = R.layout.rv_empty_view,
        preLoadCount: Int = 5,
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
        this.mPreLoadCount = preLoadCount
    }


    interface Listener {
        /**
         * @param currentPage 加载第n页数据
         */
        fun loadData(currentPage: Int) {}
    }

    /**
     * 加载更多调用该方法
     * @param list 追加数据
     */
    fun loadMoreData(list: MutableList<Item>) {
        refreshRVAdapter?.adapter?.data?.addAll(list)
        refreshRVAdapter?.adapter?.notifyDataSetChanged()
        refreshLayout?.finishLoadMore(true)
    }

    /**
     * 刷新/首次加载数据调用该方法
     * @param list 数据源
     */
    fun refreshData(list: MutableList<Item>) {
        refreshRVAdapter?.adapter?.setList(list)
        refreshLayout?.finishRefresh(true)
    }

    private fun log(msg: String) {
        log(TAG, msg)
    }

}