package app.allever.android.lib.common

import app.allever.android.lib.common.adapter.TextAdapter
import app.allever.android.lib.common.databinding.FragmentListBinding
import app.allever.android.lib.core.helper.ActivityHelper

class CommonFragment : ListFragment<FragmentListBinding, ListViewModel, String>() {
    override fun getAdapter() = TextAdapter()

    override fun getList() = mutableListOf(
        "全屏Activity",
        "标题Activity",
        "全屏Activity-黑夜模式",
        "全屏Fragment",
        "标题Fragment",
        "全屏Fragment-黑夜模式",
    )

    override fun onItemClick(position: Int, item: String) {
        when (position) {
            0 -> {
                ActivityHelper.startActivity<FullScreenActivity> { }
            }
            1 -> {
                ActivityHelper.startActivity<TitleActivity> { }
            }
            2 -> {
                ActivityHelper.startActivity<FullScreenDarkActivity> { }
            }

            3 -> {
                FragmentActivity.start<EmptyPageFragment>(item, false)
            }
            4 -> {
                FragmentActivity.start<EmptyPageFragment>(item)
            }
            5 -> {
                FragmentActivity.start<EmptyPageDarkFragment>(item, showTopBar = false, darkMode = true)
            }
        }
    }
}