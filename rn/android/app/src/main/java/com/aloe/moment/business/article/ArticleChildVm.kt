package com.aloe.moment.business.article

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingConfig
import com.aloe.bean.ClassifyBean
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleChildVm @Inject constructor(state: SavedStateHandle, private val repository: Repository) : BasicVm() {
  val bean: ClassifyBean? = state["data"]
  fun getPager() = convertIntPager(PagingConfig(20, 3, true, 20)){
    repository.getArticleList(bean?.id ?: 0, it + 1, bean?.keyword)
  }
}