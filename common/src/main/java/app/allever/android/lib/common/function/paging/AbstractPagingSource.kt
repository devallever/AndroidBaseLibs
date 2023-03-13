//package app.allever.android.lib.common.function.paging
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import app.allever.android.lib.core.ext.log
//
//abstract class AbstractPagingSource<T : Any> : PagingSource<Int, T>() {
//    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
//        return try {
//            val page = params.key ?: 1 // set page 1 as default
//            log("PagingSource 加载第${page}页")
//            val prevKey = if (page > 1) page - 1 else null
//            val data = getData(page)
//            val list = if (data.isEmpty()) {
//                listOf()
//            } else {
//                data
//            }
//            val nextKey = if (list.isNotEmpty()) page + 1 else null
//            if (list.isEmpty()) {
//                LoadResult.Page(list, prevKey, null)
//            } else {
//                LoadResult.Page(list, prevKey, nextKey)
//            }
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    abstract suspend fun getData(currentPage: Int): MutableList<T>
//}