package app.allever.android.lib.widget

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class BaseViewBindingHolder<BD : ViewBinding>(view: View) : BaseViewHolder(view) {

    val dataBinding = inflateBinding()

    open fun inflateBinding(): BD? {
        return null
    }

}