//package app.allever.android.lib.common.function.paging
//
//import android.annotation.SuppressLint
//import androidx.recyclerview.widget.DiffUtil
//
//@SuppressLint("DiffUtilEquals")
//open class PagingDiffCallback<T : Any>(val checkSame: (old: T, new: T) -> Boolean) :
//    DiffUtil.ItemCallback<T>() {
//    override fun areItemsTheSame(oldItem: T, newItem: T) = checkSame(oldItem, newItem)
//
//    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
//}