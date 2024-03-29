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

package com.aloe.moment.page.recommend

import androidx.compose.runtime.mutableStateOf
import com.aloe.basic.BasicVm
import com.aloe.bean.ArticleBean
import com.aloe.proto.Banner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecommendVm @Inject constructor(private val useCase: RecommendUseCase) : BasicVm() {
    private val _uiState = MutableStateFlow(RecommendUiState())
    private val isRefreshing = mutableStateOf(false)

    init {
        sendEvent<Unit>(RefreshEvent)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> sendEvent(event: Event): T {
        val result: T
        when (event) {
            is RefreshEvent -> {
                isRefreshing.value = true
                loadData("banner") {
                    useCase.loadBanner().collectLatest { banner ->
                        isRefreshing.value = false
                        _uiState.update {
                            it.copy(banner = banner ?: listOf(), top = useCase.loadTop())
                        }
                    }
                }
                result = Unit as T
            }
            is UiStateEvent -> result = _uiState.asStateFlow() as T
            is RefreshStateEvent -> result = isRefreshing as T
            is PagingSourceEvent -> result = useCase.pagingSource as T
        }
        return result
    }
}

data class RecommendUiState(
    val banner: List<Banner> = listOf(),
    val top: List<ArticleBean> = listOf(),
)

sealed class Event
object RefreshEvent : Event()

object UiStateEvent : Event()
object RefreshStateEvent : Event()
object PagingSourceEvent : Event()
