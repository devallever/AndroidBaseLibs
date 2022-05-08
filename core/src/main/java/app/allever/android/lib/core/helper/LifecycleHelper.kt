package app.allever.android.lib.core.helper

import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 * Created by Allever
 *
 * @Author: Jerry.
 * @Date: 2020/11/25 10
 * @Desc:
 */
object LifecycleHelper {
    private var mRootOwnerWr: WeakReference<LifecycleOwner>? = null
    private var mChildOwnerWr: WeakReference<LifecycleOwner>? = null
    fun pullRootOwner(mRootOwner: LifecycleOwner) {
        mRootOwnerWr?.clear()
        mRootOwnerWr = null
        mRootOwnerWr = WeakReference(mRootOwner)
    }

    fun pullChildOwner(mChildOwner: LifecycleOwner) {
        mChildOwnerWr?.clear()
        mChildOwnerWr = null
        mChildOwnerWr = WeakReference(mChildOwner)
    }

    val owner: LifecycleOwner?
        get() = if (null == mChildOwnerWr) {
            mRootOwnerWr?.get()
        } else mChildOwnerWr?.get()

    fun clear() {
        mChildOwnerWr?.clear()
        mChildOwnerWr = null
        mRootOwnerWr?.clear()
        mRootOwnerWr = null
    }
}