package com.aloe.moment.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.singleOrNull

class IntPagingSource<T : Any>(private val request: suspend (Int) -> Flow<Result<List<T>?>>) :
  PagingSource<Int, T>() {
  override fun getRefreshKey(state: PagingState<Int, T>): Int = 0

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
    val current = params.key ?: 0
    val data = request.invoke(current).singleOrNull()?.getOrNull()
    if (data == null) {
      LoadResult.Error(NullPointerException("no data"))
    } else {
      LoadResult.Page(data, if (current == 0) null else current - 1, current + 1)
    }
  } catch (e: Exception) {
    e.printStackTrace()
    LoadResult.Error(e)
  }
}