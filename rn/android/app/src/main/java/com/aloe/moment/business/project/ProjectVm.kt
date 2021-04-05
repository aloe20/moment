package com.aloe.moment.business.project

import androidx.paging.PagingConfig
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectVm @Inject constructor(private val repository: Repository) : BasicVm() {
  fun getPager() = convertIntPager(PagingConfig(15, 3, true, 15)) {
    repository.getProject(it)
  }
}