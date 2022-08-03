package app.allever.android.lib.demo.stickytop

import android.os.Build
import androidx.annotation.RequiresApi
import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.demo.BR
import app.allever.android.lib.demo.R
import app.allever.android.lib.demo.databinding.ActivityRemoveAddViewStickyTopBinding
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig

class RemoveAddViewStickyTopActivity :
    BaseActivity<ActivityRemoveAddViewStickyTopBinding, RemoveAddViewStickyTopViewModel>() {
    override fun getContentMvvmConfig() =
        MvvmConfig(R.layout.activity_remove_add_view_sticky_top, BR.removeAddViewStickyTopVM)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun init() {
        initTopBar("RemoveAddView吸顶")
        var firstViewHeight = 0
        binding.firstView.post {
            firstViewHeight = binding.firstView.height
        }
        val stickyView = binding.tvStickyTopView
        binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (firstViewHeight == 0) {
                return@setOnScrollChangeListener
            }

            if (scrollY >= firstViewHeight) {
                //防止重复添加闪退
                if (stickyView.parent != binding.outsideLayout) {
                    binding.insideLayout.removeView(stickyView)
                    binding.outsideLayout.addView(stickyView)
                }
            } else {
                if (stickyView.parent != binding.insideLayout) {
                    binding.outsideLayout.removeView(stickyView)
                    binding.insideLayout.addView(stickyView)
                }

            }
        }
    }
}

class RemoveAddViewStickyTopViewModel(): BaseViewModel() {
    override fun init() {

    }
}

