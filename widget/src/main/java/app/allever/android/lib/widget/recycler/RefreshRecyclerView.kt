package app.allever.android.lib.widget.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.widget.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
    private var list = mutableListOf<Item>()

    private var mCurrentPage = 0
    private var mDataFetchListener: DataFetchListener<Item>? = null
    private var mPreLoadCount: Int = 5
    private var mIsPreLoading = false
    private var mEnablePreload = true


    private val job = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private var mEnableViewPager = false
    private var mLastSwitchPagerPosition = 0
    private var mPageChangeListener: PageChangeListener<Item>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.refresh_recycler_view, this)
        initView()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        refreshLayout = findViewById(R.id.smartRefreshLayout)
        refreshLayout?.setOnLoadMoreListener {
            handleLoadOrRefresh(true)
            postDelayed({
                refreshLayout?.finishLoadMore(true)
            }, 1000 * 10)
        }

        refreshLayout?.setOnRefreshListener {
            handleLoadOrRefresh(false)
            postDelayed({
                refreshLayout?.finishRefresh(true)
            }, 1000 * 10)
        }

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                log("newState = $newState")
                if (newState == SCROLL_STATE_IDLE) {
                    when (val layoutManager = recyclerView.layoutManager) {
                        is LinearLayoutManager -> {
                            val position = layoutManager.findLastVisibleItemPosition()
                            if (position == mLastSwitchPagerPosition) {
                                return
                            }
//                            log("page position = $position")
//                            toast("position = $position")
//                            Toast.makeText(context, "position = $position", Toast.LENGTH_SHORT).show()
                            mLastSwitchPagerPosition = position
                            if (mEnableViewPager) {
                                refreshRVAdapter?.data?.get(position)
                                    ?.let { mPageChangeListener?.onPageChanged(position, it) }
                            }
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 获取 LayoutManger
                val layoutManager = recyclerView.layoutManager
                // 如果 LayoutManager 是 LinearLayoutManager
                if (layoutManager is LinearLayoutManager) {
                    // 如果列表正在往上滚动，并且表项最后可见表项索引值 等于 预加载阈值
                    if (dy > 0 && layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1 - mPreLoadCount) {

                        if (!mEnablePreload) {
                            return
                        }

                        //
                        if (mIsPreLoading) {
                            return
                        }

//                        toast("预加载")
                        log("预加载第 $mCurrentPage 页")
                        handleLoadOrRefresh(true)
                        mIsPreLoading = true
                        postDelayed({
                            mIsPreLoading = false
                        }, 2000)
                    }
                }
            }
        })
    }

    private var mIsDataSourceFromFetchData = false
    private fun handleLoadOrRefresh(isLoadMore: Boolean) {
        if (isLoadMore) {
            mCurrentPage++
        } else {
            mCurrentPage = 0
        }

        //解决刷新/加载没响应
        if (context is LifecycleOwner) {
            (context as LifecycleOwner).lifecycleScope.launch {
                val data = mDataFetchListener?.fetchData(mCurrentPage, isLoadMore)
                if (data?.isNotEmpty() == true) {
                    mIsDataSourceFromFetchData = true
                    if (isLoadMore) {
                        loadMoreData(data)
                    } else {
                        refreshData(data)
                    }
                } else {
                    mDataFetchListener?.loadData(mCurrentPage, isLoadMore)
                }
            }
        }
    }

    /***
     * @param refreshRVAdapter
     * @param layoutManager
     */
    fun setAdapter(
        refreshRVAdapter: RefreshRVAdapter<Item, BaseViewHolder>,
        layoutManager: RecyclerView.LayoutManager? = null
    ): RefreshRecyclerView<Item> {
        setAdapter(
            refreshRVAdapter = refreshRVAdapter,
            layoutManager = layoutManager,
            dataFetchListener = null,
        )
        return this
    }

    /***
     * @param refreshRVAdapter
     * @param header 刷新头
     * @param footer 加载底部
     * @param layoutManager
     * @param emptyResId 空布局
     * @param preLoadCount 预加载阈值
     * @param dataFetchListener 刷新/加载监听器
     */
    fun setAdapter(
        refreshRVAdapter: RefreshRVAdapter<Item, BaseViewHolder>,
        dataFetchListener: DataFetchListener<Item>?,
        layoutManager: RecyclerView.LayoutManager? = null,
        header: RefreshHeader? = null,
        footer: RefreshFooter? = null,
        emptyResId: Int = R.layout.rv_empty_view,
        preLoadCount: Int = 5,
        enableViewPager: Boolean = false,
        pageChangeListener: PageChangeListener<Item>? = null,
        executeLoadData: Boolean = false
    ): RefreshRecyclerView<Item> {
        recyclerView?.layoutManager = layoutManager ?: LinearLayoutManager(context)
        recyclerView?.adapter = refreshRVAdapter
        mEnableViewPager = enableViewPager
        enableViewPager(enableViewPager)

        header?.let {
            refreshLayout?.setRefreshHeader(header)
        }
        footer?.let {
            refreshLayout?.setRefreshFooter(footer)
        }
        refreshRVAdapter.setList(list)
        refreshRVAdapter.setEmptyView(emptyResId)
        refreshRVAdapter.refreshRV = this
        this.refreshRVAdapter = refreshRVAdapter
        this.mDataFetchListener = dataFetchListener
        this.mCurrentPage = 0
        this.mPreLoadCount = preLoadCount
        mPageChangeListener = pageChangeListener
        if (executeLoadData) {
            coroutineScope.launch {
                handleLoadOrRefresh(false)
            }
        }
        return this
    }

    fun dataFetchListener(dataFetchListener: DataFetchListener<Item>?): RefreshRecyclerView<Item> {
        this.mDataFetchListener = dataFetchListener
        return this
    }

    fun layoutManager(layoutManager: RecyclerView.LayoutManager): RefreshRecyclerView<Item> {
        recyclerView?.layoutManager = layoutManager
        return this
    }

    fun header(header: RefreshHeader): RefreshRecyclerView<Item> {
        refreshLayout?.setRefreshHeader(header)
        return this
    }

    fun footer(footer: RefreshFooter): RefreshRecyclerView<Item> {
        refreshLayout?.setRefreshFooter(footer)
        return this
    }

    fun enableRefresh(enable: Boolean): RefreshRecyclerView<Item> {
        refreshLayout?.setEnableRefresh(enable)
        return this
    }

    fun enableLoadMore(enable: Boolean): RefreshRecyclerView<Item> {
        refreshLayout?.setEnableLoadMore(enable)
        return this
    }

    fun emptyView(layoutId: Int): RefreshRecyclerView<Item> {
        refreshRVAdapter?.setEmptyView(layoutId)
        return this
    }

    fun emptyView(view: View): RefreshRecyclerView<Item> {
        refreshRVAdapter?.setEmptyView(view)
        return this
    }

    fun preloadCount(preloadCount: Int): RefreshRecyclerView<Item> {
        mPreLoadCount = preloadCount
        return this
    }

    fun enablePreload(enable: Boolean): RefreshRecyclerView<Item> {
        mEnablePreload = enable
        return this
    }

    fun enableViewPager(enable: Boolean): RefreshRecyclerView<Item> {
        mEnableViewPager = enable
        if (enable) {
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(recyclerView)
        }
        return this
    }

    fun pageChangeListener(listener: PageChangeListener<Item>): RefreshRecyclerView<Item> {
        mPageChangeListener = listener
        return this
    }

    fun execute() {
        coroutineScope.launch {
            handleLoadOrRefresh(false)
        }
    }

    /**
     * 外部处理调用加载更多, 比如外层也有SmartRefreshLayout
     */
    fun handleLoadMore() {
        log("加载第 $mCurrentPage 页")
        handleLoadOrRefresh(true)
    }

    /**
     * 外部处理调用刷新, 比如外层也有SmartRefreshLayout
     */
    fun handleRefresh() {
        log("加载第 0 页")
        handleLoadOrRefresh(false)
    }

    interface DataFetchListener<Item> {
        /**
         * @param currentPage 加载第n页数据
         */
        fun loadData(currentPage: Int, isLoadMore: Boolean = true) {}

        /**
         * 协程方式获取数据，优先协程 -> loadMore
         */
        suspend fun fetchData(
            currentPage: Int,
            isLoadMore: Boolean
        ): MutableList<Item> {
            return mutableListOf()
        }
    }

    interface PageChangeListener<Item> {
        fun onPageChanged(position: Int, item: Item)
    }

    /**
     * 加载更多调用该方法
     * @param list 追加数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun loadMoreData(list: MutableList<Item>) {
        refreshRVAdapter?.data?.addAll(list)
        refreshRVAdapter?.notifyDataSetChanged()
        refreshLayout?.finishLoadMore(true)
        if (list.isEmpty() || mIsDataSourceFromFetchData) {
            refreshLayout?.finishLoadMoreWithNoMoreData()
        }
    }

    /**
     * 刷新/首次加载数据调用该方法
     * @param list 数据源
     */
    fun refreshData(list: MutableList<Item>) {
        refreshRVAdapter?.setList(list)
        refreshLayout?.finishRefresh(true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

    private fun log(msg: String) {
        log(TAG, msg)
    }

}