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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.aloe.bean.ArticleBean
import com.aloe.moment.app.rememberAsyncPainter
import com.aloe.moment.page.main.LocalNav
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecommendLayout(vm: RecommendVm = hiltViewModel(), nav: NavHostController = LocalNav.current) {
    val uiState by vm.sendEvent<StateFlow<RecommendUiState>>(UiStateEvent)
        .collectAsStateWithLifecycle()
    val refreshing by vm.sendEvent<MutableState<Boolean>>(RefreshStateEvent)
    val pullRefreshState = rememberPullRefreshState(refreshing, { vm.sendEvent(RefreshEvent) })
    val pager = remember {
        Pager(
            PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                enablePlaceholders = true,
                maxSize = 200,
                initialLoadSize = 3,
            ),
        ) { vm.sendEvent<PagingSource<Int, ArticleBean>>(PagingSourceEvent) }
    }
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(Modifier.fillMaxSize()) {
            if (uiState.banner.isNotEmpty()) {
                item {
                    RecommendBanner(uiState = uiState)
                }
            }
            if (uiState.top.isNotEmpty()) {
                items(uiState.top) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    ) {
                        Text(text = it.title ?: "", color = Color.Black)
                        Text(text = it.author ?: "", color = Color.Black)
                        Text(text = it.niceDate ?: it.niceShareDate ?: "", color = Color.Black)
                    }
                    Divider(color = Color.LightGray)
                }
            }
            if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                item {
                    Text(
                        text = "Waiting for items to load from the backend",
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                }
            }

            itemsIndexed(lazyPagingItems) { index, item ->
                Text("Index=$index: $item", color = Color.Black, fontSize = 20.sp)
                if (index < lazyPagingItems.itemCount - 1) {
                    Divider(color = Color.LightGray)
                }
            }

            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun RecommendBanner(uiState: RecommendUiState, nav: NavHostController = LocalNav.current) {
    Banner(modifier = Modifier.height(180.dp), count = uiState.banner.size) {
        Image(
            painter = rememberAsyncPainter(model = uiState.banner[it].imagePath),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    nav.navigate("web?url=${uiState.banner[it].url}")
                    // nav.navigate("web?url=http://192.168.137.1:5173")
                },
            alignment = Alignment.Center,
        )
    }
}
