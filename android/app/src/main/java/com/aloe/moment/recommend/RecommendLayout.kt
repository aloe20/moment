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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aloe.moment.app.rememberAsyncPainter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecommendLayout(vm: RecommendVm = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val refreshing by vm.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing, { vm.sendEvent(RefreshEvent) })
    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(Modifier.fillMaxSize())  {
            if (uiState.banner.isNotEmpty()) {
                item {
                    Banner(modifier = Modifier.height(180.dp), count = uiState.banner.size) {
                        Image(
                            painter = rememberAsyncPainter(model = uiState.banner[it].imagePath),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            alignment = Alignment.Center
                        )
                    }
                }
            }
            if (uiState.top.isNotEmpty()) {
                items(uiState.top){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                        Text(text = it.title ?: "", color = Color.Black)
                        Text(text = it.author ?: "", color = Color.Black)
                        Text(text = it.niceDate ?: it.niceShareDate ?: "", color = Color.Black)
                    }
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}
