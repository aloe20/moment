package com.aloe.moment.business.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.aloe.bean.ClassifyBean
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleVm @Inject constructor(private val repository: Repository) : BasicVm() {
  private val tabLiveData = MediatorLiveData<List<ClassifyBean>?>()
  fun getTabLiveData(): LiveData<List<ClassifyBean>?> = tabLiveData
  suspend fun loadTabData() = convertFlow(tabLiveData, repository.getArticleClassify())
}