package app.allever.android.lib.core.helper

import razerdp.basepopup.BasePopupWindow
import java.lang.ref.WeakReference

object PopupWindowHelper {
    private var mCurrentPopupWindow: WeakReference<BasePopupWindow>? = null
    fun show(popupWindow: BasePopupWindow) {
        val lastPop = mCurrentPopupWindow?.get()
        if (lastPop?.hashCode() == popupWindow.hashCode()) {
            if (lastPop.isShowing) {
                return
            }
            lastPop.showPopupWindow()
            return
        }

        if (lastPop?.isShowing == true) {
            lastPop.dismiss()
        }

        popupWindow.showPopupWindow()

        mCurrentPopupWindow = WeakReference(popupWindow)
    }

    fun dismiss(popupWindow: BasePopupWindow) {
        popupWindow.dismiss()
        val lastPop = mCurrentPopupWindow?.get()
        if (lastPop?.hashCode() == popupWindow.hashCode()) {
            mCurrentPopupWindow?.clear()
        }
    }
}