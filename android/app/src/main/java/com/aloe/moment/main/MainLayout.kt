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

package com.aloe.moment.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aloe.moment.flu.FlutterLayout
import com.aloe.moment.react.ReactLayout
import kotlinx.coroutines.launch

fun NavGraphBuilder.mainPage() {
    composable("mainPage") {
        MainLayout()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLayout() {
    val pageState = rememberPagerState()
    val items = mutableListOf("推荐", "项目", "公众号", "我的")
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.background(Color.White)) {
        HorizontalPager(
            modifier = Modifier.weight(1F),
            pageCount = items.size,
            state = pageState
        ) {
            when (it) {
                0 -> Text(
                    text = "第一页", modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red)
                )
                1 -> ReactLayout(url = "assets://index.android.bundle")
                2 -> FlutterLayout()
                3 -> Text(
                    text = "第四页", modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Blue)
                )
            }
        }
        Row {
            items.forEachIndexed { index, s ->
                Column(modifier = Modifier.weight(1F)) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_add),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                scope.launch {
                                    pageState.scrollToPage(index, 0F)
                                }
                            },
                        tint = if (index == pageState.currentPage) Color.Red else Color.Black
                    )
                    Text(
                        text = s,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                scope.launch {
                                    pageState.scrollToPage(index, 0F)
                                }
                            },
                        color = if (index == pageState.currentPage) Color.Red else Color.Black
                    )
                }
            }
        }
    }
}