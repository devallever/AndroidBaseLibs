package app.allever.android.lib.core.base.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import app.allever.android.lib.core.helper.DisplayHelper
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AbstractSlideBottomDialog<DB : ViewDataBinding> : BottomSheetDialogFragment() {

    protected lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, app.allever.android.lib.core.R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        val frameLayout = dialog?.delegate?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        frameLayout?.let {
            val layoutParams = frameLayout.layoutParams
            layoutParams.height = getDialogHeight()
            val behavior = BottomSheetBehavior.from(frameLayout)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    open fun show(manager: FragmentManager) {
        if (context is Activity) {
            if ((context as Activity).isFinishing || (context as Activity).isDestroyed) {
                return
            }
        }
        if (isAdded || manager.findFragmentByTag(TAG) != null) {
            return
        }
        super.show(manager, TAG)
    }

    companion object {
        private val TAG = AbstractSlideBottomDialog::class.java.simpleName
    }

    /**
     * dialog布局文件中写高度无效，最终都变成match_parent
     */
    protected open fun getDialogHeight(): Int = DisplayHelper.dip2px(350)
    abstract fun getLayoutId(): Int
    abstract fun initView()
}