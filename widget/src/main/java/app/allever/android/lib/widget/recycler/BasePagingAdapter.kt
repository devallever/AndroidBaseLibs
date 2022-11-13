package app.allever.android.lib.widget.recycler

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import app.allever.android.lib.core.app.App


/***
 * https://blog.csdn.net/lmj623565791/article/details/51118836
 * https://blog.csdn.net/qq_35605213/article/details/80176558
 * @param <T>
</T> */
abstract class BasePagingAdapter<T : Any>(
    val mLayoutResId: Int,
    diffCallback: DiffUtil.ItemCallback<T>
) :
    PagingDataAdapter<T, BaseViewHolder>(diffCallback) {
    var mItemListener: ItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.getHolder(App.context, parent, mLayoutResId)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemListener?.onItemClick(position, holder)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener mItemListener?.onItemLongClick(position, holder) ?: false
        }
        getItem(position)?.let { bindHolder(holder, position, it) }
    }


//    fun updateData(position: Int) {
//        notifyItemChanged(position, position)
//    }
//
//    fun addData(@IntRange(from = 0) position: Int, data: T) {
//        mData.add(position, data)
//        notifyItemInserted(position)
//        compatibilityDataSizeChanged(1)
//    }
//
//    fun addData(data: T) {
//        mData.add(data)
//        notifyItemInserted(mData.size)
//    }
//
//
//    fun remove(position: Int) {
//        mData.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, mData.size - position)
//    }

    fun setItemListener(itemListener: ItemListener) {
        mItemListener = itemListener
    }

//    private fun compatibilityDataSizeChanged(size: Int) {
//        val dataSize = mData.size
//        if (dataSize == size) {
//            notifyDataSetChanged()
//        }
//    }

    /**
     * 需要重写的方法
     *
     * @param holder
     * @param position
     */
    abstract fun bindHolder(holder: BaseViewHolder, position: Int, item: T)
}