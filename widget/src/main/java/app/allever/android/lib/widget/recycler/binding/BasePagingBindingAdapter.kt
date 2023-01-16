package app.allever.android.lib.widget.recycler.binding

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding


/***
 * https://blog.csdn.net/lmj623565791/article/details/51118836
 * https://blog.csdn.net/qq_35605213/article/details/80176558
 * @param <T>
</T> */
abstract class BasePagingBindingAdapter<T : Any, DB : ViewBinding>(
    val mLayoutResId: Int,
    diffCallback: DiffUtil.ItemCallback<T>
) :
    PagingDataAdapter<T, BaseBindingViewHolder<DB>>(diffCallback) {
    var itemClickListener: ((Int, BaseBindingViewHolder<DB>, item: T) -> Unit)? = null
    var itemLongClickListener: ((Int, BaseBindingViewHolder<DB>) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<DB> {
        val binding = inflate()
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<DB>, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position, holder, getItem(position)!!)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener itemLongClickListener?.invoke(position, holder) ?: false
        }
        getItem(position)?.let { convert(holder, position, it) }
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

//    fun setItemListener(itemListener: ItemListener) {
//        mItemListener = itemListener
//    }

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
    abstract fun convert(holder: BaseBindingViewHolder<DB>, position: Int, item: T)
    abstract fun inflate(): DB
}