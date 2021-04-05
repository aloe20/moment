package com.aloe.moment.business.recommend

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendVm @Inject constructor(private val repository: Repository) : BasicVm() {
  fun getImageLiveData(): LiveData<String?> = repository.getString("user_image").asLiveData()

  fun updateImage(uri: Uri?) {
    uri?.toString()?.also {
      viewModelScope.launch {
        repository.saveString("user_image", it)
      }
    }
  }

  suspend fun loadBannerData() = repository.getWanBanner().map { it.getOrNull() }.asLiveData()
}