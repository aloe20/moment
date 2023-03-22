/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.aloe.moment.recommend

import android.util.Log
import com.aloe.bean.ArticleBean
import com.aloe.moment.BaseVm
import com.aloe.proto.Banner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecommendVm @Inject constructor(private val useCase: RecommendUseCase) : BaseVm() {
    private val _uiState = MutableStateFlow(RecommendUiState())
    val uiState: StateFlow<RecommendUiState> = _uiState.asStateFlow()

    init {
        sendEvent(RefreshEvent)
    }

    fun sendEvent(event: Event) {
        when (event) {
            is RefreshEvent -> {
                loadBanner()
                loadTop()
            }
        }
    }

    private fun loadBanner() {
        loadData("banner") {
            useCase.loadBanner().collectLatest { banner ->
                _uiState.update {
                    it.copy(banner = banner ?: listOf())
                }
            }
        }
    }

    private fun loadTop() {
        loadData("top") {
            _uiState.update { it.copy(top = useCase.loadTop()) }
        }
    }
}

data class RecommendUiState(
    val banner: List<Banner> = listOf(),
    val top: List<ArticleBean> = listOf()
)

sealed class Event
object RefreshEvent : Event()