package app.allever.android.lib.common

import android.text.TextUtils
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import app.allever.android.lib.common.databinding.FragmentListBinding
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.helper.KeyEventHelper
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.mvvm.base.BaseViewModel
import com.chad.library.adapter.base.BaseQuickAdapter

abstract class ListFragment<DB : ViewBinding, VM : BaseViewModel, T> :
    BaseFragment<FragmentListBinding, ListViewModel>() {

    private var mAdapter: BaseQuickAdapter<T, *>? = null

    override fun inflate() = FragmentListBinding.inflate(layoutInflater)

    override fun init() {

        initTopBar()

        mBinding.smartRefreshLayout.setEnableOverScrollDrag(true)

        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)

        mAdapter = getAdapter()
        mBinding.recyclerView.adapter = mAdapter

        mAdapter?.setList(getList())

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            onItemClick(position, mAdapter?.getItem(position) ?: return@setOnItemClickListener)
        }
    }

    private fun initTopBar() {
        val title = getTitle()
        val showTopBar = !TextUtils.isEmpty(title)
        if (showTopBar) {
            ViewHelper.setViewHeight(
                mBinding.statusBar,
                DisplayHelper.getStatusBarHeight(requireContext())
            )
            ViewHelper.setVisible(mBinding.topBar, showTopBar)
            mBinding.tvTitle.text = title
            mBinding.ivBack.setOnClickListener {
                KeyEventHelper.clickBack()
            }
        }
    }

    abstract fun getAdapter(): BaseQuickAdapter<T, *>
    abstract fun getList(): MutableList<T>
    open protected fun onItemClick(position: Int, item: T) {

    }

    open fun getTitle(): String {
        return ""
    }

    protected fun updateList(list: MutableList<T>) {
        mAdapter?.setList(list)
    }
}

class ListViewModel : BaseViewModel() {

}