package app.allever.android.lib.core.function.paging

import android.annotation.SuppressLint
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow

object PagingHelper {

    fun <T : Any> getPager(pageSource: PagingSource<*, T>): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pageSource }
        ).flow
    }

    @SuppressLint("DiffUtilEquals")
    fun <T : Any> createPagingDiffCallback(block: (old: T, new: T) -> Boolean): DiffUtil.ItemCallback<T> {
        return object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = block(oldItem, newItem)

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }
        }
    }
}