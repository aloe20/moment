package com.aloe.moment.business.hierarchy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import com.aloe.bean.TreeBean
import com.aloe.bean.TreeChildBean
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HierarchyDetailVm @Inject constructor(state: SavedStateHandle, private val repository: Repository) : BasicVm() {
  private val treeClassifyId: Int = state["tree_classify_id"] ?: 0
  private val treeChildId: Int = state["tree_child_id"] ?: 0
  private val treeClassifyLiveData = MediatorLiveData<TreeBean?>()
  private val treeChildLiveData = MediatorLiveData<TreeChildBean?>()

  fun getTreeChildLiveData(): LiveData<TreeChildBean?> = treeChildLiveData

  fun getTreeClassifyLiveData(): LiveData<TreeBean?> = treeClassifyLiveData

  suspend fun loadTreeData() {
    if (treeClassifyId != 0) {
      convertFlow(treeClassifyLiveData, repository.loadTreeBeanById(treeClassifyId))
    }
  }

  suspend fun loadTreeChildData() {
    if (treeChildId != 0) {
      convertFlow(treeChildLiveData, repository.loadTreeChildById(treeChildId))
    }
  }
}