package com.aloe.moment.business.video

import androidx.lifecycle.asLiveData
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class VideoVm @Inject constructor(private val repository: Repository) : BasicVm() {
  suspend fun loadVideo() = repository.getVideoList().map { it.getOrNull() }.asLiveData()
}