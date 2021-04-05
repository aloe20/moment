package com.aloe.moment.business.article.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import com.aloe.bean.ClassifyBean
import com.aloe.bean.HotkeyBean
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchVm @Inject constructor(state: SavedStateHandle, private val repository: Repository) : BasicVm() {
  val bean: ClassifyBean? = state["data"]
  private val hotKeyLiveData = MediatorLiveData<List<HotkeyBean>?>()
  fun getHotKeyLiveData(): LiveData<List<HotkeyBean>?> = hotKeyLiveData
  suspend fun loadHotKeyData() = convertFlow(hotKeyLiveData, repository.getHotkey())
}