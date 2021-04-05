package com.aloe.moment.business.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingConfig
import com.aloe.bean.WanBean
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RecommendChildVm @Inject constructor(state: SavedStateHandle, private val repository: Repository) : BasicVm() {
  private val cid: Int? = state["cid"]
  suspend fun loadTopData(): LiveData<List<WanBean>?> =
    (if (cid == null) repository.getWanTop() else flowOf(Result.success(emptyList())))
      .map { it.getOrNull() }.asLiveData()

  fun getPager() = convertIntPager(PagingConfig(20, 3, true, 20)) {
    repository.getWanRecommend(it, cid)
  }
}