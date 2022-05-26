package app.allever.android.lib.widget.recycler

import android.content.Context
import android.view.ViewGroup
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author allever
 */
abstract class MultiItemRecyclerViewAdapter<T>(
    context: Context, datas: MutableList<T>,
    protected var mMultiItemTypeSupport: MultiItemTypeSupport<T>
) : BaseRecyclerViewAdapter<T>(context, -1, datas) {

    fun s() {
//        Bas
    }
    override fun getItemViewType(position: Int): Int {
        return mMultiItemTypeSupport.getItemViewType(position, mData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutId = mMultiItemTypeSupport.getLayoutId(viewType)
        return BaseViewHolder.getHolder(mContext, parent, layoutId)
    }

}