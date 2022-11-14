package app.allever.android.lib.widget.recycler.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import app.allever.android.lib.core.app.App


/***
 * https://blog.csdn.net/lmj623565791/article/details/51118836
 * https://blog.csdn.net/qq_35605213/article/details/80176558
 * @param <T>
</T> */
abstract class BaseRecyclerViewBindingAdapter<T : Any, DB : ViewDataBinding>(
    val mLayoutResId: Int, var mData: MutableList<T> = mutableListOf()
) :
    RecyclerView.Adapter<BaseBindingViewHolder<DB>>() {
    var itemClickListener: ((Int, BaseBindingViewHolder<DB>) -> Unit)? = null
    var itemLongClickListener: ((Int, BaseBindingViewHolder<DB>) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<DB> {
        val binding = DataBindingUtil.inflate<DB>(
            LayoutInflater.from(App.context),
            mLayoutResId,
            parent,
            false
        )
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<DB>, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position, holder)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener itemLongClickListener?.invoke(position, holder) ?: false
        }
        convert(holder, position, mData[position])
    }


    fun updateData(position: Int) {
        notifyItemChanged(position, position)
    }

    fun addData(@IntRange(from = 0) position: Int, data: T) {
        mData.add(position, data)
        notifyItemInserted(position)
        compatibilityDataSizeChanged(1)
    }

    fun addData(data: T) {
        mData.add(data)
        notifyItemInserted(mData.size)
    }


    fun remove(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mData.size - position)
    }

    private fun compatibilityDataSizeChanged(size: Int) {
        val dataSize = mData.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * 需要重写的方法
     *
     * @param holder
     * @param position
     */
    abstract fun convert(holder: BaseBindingViewHolder<DB>, position: Int, item: T)
}