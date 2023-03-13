//package app.allever.android.lib.common.function.paging
//
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import androidx.paging.PagingSource
//import kotlinx.coroutines.flow.Flow
//
//object PagingHelper {
//
//    fun <T : Any> getPager(pageSource: PagingSource<*, T>): Flow<PagingData<T>> {
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            pagingSourceFactory = { pageSource }
//        ).flow
//    }
//}