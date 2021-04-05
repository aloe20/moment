package com.aloe.moment.business.hierarchy

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import com.aloe.bean.TreeBean
import com.aloe.bean.TreeChildBean
import com.aloe.http.Repository
import com.aloe.moment.R
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HierarchyChildVm @Inject constructor(
  private val app: Application,
  state: SavedStateHandle,
  private val repository: Repository
) : BasicVm() {
  private val type: String = state["type"] ?: ""
  private val listLiveData = MediatorLiveData<List<TreeBean>?>()

  fun getListLiveData(): LiveData<List<TreeBean>?> = listLiveData

  fun isHierarchy() = type == app.getString(R.string.hierarchy)

  suspend fun loadListData() {
    val flow = if (isHierarchy()) {
      repository.getTree()
    } else {
      repository.getNavi().map { result ->
        if (result.isSuccess) {
          val list = mutableListOf<TreeBean>()
          result.getOrNull()?.forEach { bean ->
            list.add(TreeBean(bean.articles.map { TreeChildBean(it.id, 0, it.title, it.link) }).apply {
              id = bean.cid
              name = bean.name
            })
          }
          Result.success(list)
        } else {
          Result.failure(result.exceptionOrNull()!!)
        }
      }
    }
    convertFlow(listLiveData, flow)
  }
}