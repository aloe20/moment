package com.aloe.moment.business.project.classify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingConfig
import com.aloe.bean.ClassifyBean
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassifyVm @Inject constructor(state: SavedStateHandle, private val repository: Repository) : BasicVm() {
  val cid: Int = state["cid"] ?: 0
  private val tabLiveData = MediatorLiveData<List<ClassifyBean>?>()
  fun getTabLiveData(): LiveData<List<ClassifyBean>?> = tabLiveData
  suspend fun loadTabData() = convertFlow(tabLiveData, repository.getProjectClassify())

  fun getPager() = convertIntPager(PagingConfig(15, 3, true, 15)) {
    repository.getProjectClassifyList(cid, it + 1)
  }
}