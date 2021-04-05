package com.aloe.moment.basic

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aloe.moment.paging.IntPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class BasicVm : ViewModel() {
  protected lateinit var owner: LifecycleOwner
  fun initOwner(owner: LifecycleOwner) {
    this.owner = owner
    prepare()
  }

  protected open fun prepare() {

  }

  protected fun <T : Any> convertIntPager(
    config: PagingConfig,
    data: suspend (Int) -> Flow<Result<List<T>?>>
  ): LiveData<PagingData<T>> = Pager(config) { IntPagingSource(data) }.flow.cachedIn(viewModelScope).asLiveData()

  protected fun <T> convertFlow(liveData: MediatorLiveData<T?>, flow: Flow<Result<T?>>) {
    liveData.addSource(flow.map { it.getOrNull() }.asLiveData()) { liveData.value = it }
  }
}