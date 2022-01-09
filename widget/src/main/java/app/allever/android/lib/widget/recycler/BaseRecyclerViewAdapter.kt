package app.allever.android.lib.widget.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import java.util.*


/***
 * https://blog.csdn.net/lmj623565791/article/details/51118836
 * https://blog.csdn.net/qq_35605213/article/details/80176558
 * @param <T>
</T> */
abstract class BaseRecyclerViewAdapter<T> :
    androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder> {
    protected var mContext: Context
    protected var mLayoutResId: Int = 0
    var mItemListener: ItemListener? = null
    var mData: MutableList<T>

    constructor(context: Context, @LayoutRes layoutRes: Int) {
        mContext = context
        mLayoutResId = layoutRes
        mData = ArrayList()
    }

    constructor(context: Context, @LayoutRes layoutResId: Int, data: MutableList<T>?) {
        mContext = context
        mData = data ?: ArrayList()
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.getHolder(mContext, parent, mLayoutResId)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemListener?.onItemClick(position, holder)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener mItemListener?.onItemLongClick(position, holder) ?: false
        }
        bindHolder(holder, position, mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    fun setData(items: MutableList<T>?) {
        this.mData = items ?: ArrayList()
        notifyDataSetChanged()
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

    fun setItemListener(itemListener: ItemListener) {
        mItemListener = itemListener
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
    abstract fun bindHolder(holder: BaseViewHolder, position: Int, item: T)
}