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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aloe.moment.rememberHttpPainter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendLayout() {
    Box(modifier = Modifier.height(160.dp)) {
        val images = mutableListOf(
            "https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png",
            "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png",
            "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
            "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png"
        )
        val loopingCount = Int.MAX_VALUE
        val startIndex = loopingCount / 2
        val pagerState = rememberPagerState(initialPage = startIndex)
        fun pageMapper(index: Int): Int = (index - startIndex).floorMod(images.size)
        HorizontalPager(pageCount = loopingCount, state = pagerState) {
            Image(
                painter = rememberHttpPainter(url = images[pageMapper(it)]),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(4.dp),
            pageCount = images.size,
            pageIndexMapping = ::pageMapper
        )
        var underDragging by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = Unit) {
            pagerState.interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> underDragging = true
                    is PressInteraction.Release -> underDragging = false
                    is PressInteraction.Cancel -> underDragging = false
                    is DragInteraction.Start -> underDragging = true
                    is DragInteraction.Stop -> underDragging = false
                    is DragInteraction.Cancel -> underDragging = false
                }
            }
        }

        if (underDragging.not()) {
            LaunchedEffect(key1 = underDragging) {
                try {
                    while (true) {
                        delay(1000L)
                        val current = pagerState.currentPage
                        val currentPos = pageMapper(current)
                        val nextPage = current + 1
                        if (underDragging.not()) {
                            val toPage = nextPage.takeIf { nextPage < loopingCount }
                                ?: (currentPos + startIndex + 1)
                            if (toPage > current) {
                                pagerState.animateScrollToPage(toPage)
                            } else {
                                pagerState.scrollToPage(toPage)
                            }
                        }
                    }
                } catch (e: CancellationException) {
                    Log.i("page", "Launched paging cancelled")
                }
            }
        }
    }
}

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}

fun randomSampleImageUrl(
    seed: Int = rangeForRandom.random(),
    width: Int = 300,
    height: Int = width,
): String {
    return "https://picsum.photos/seed/$seed/$width/$height"
}

private val rangeForRandom = (0..100000)

/**
 * Remember a URL generate by [randomSampleImageUrl].
 */
@Composable
fun rememberRandomSampleImageUrl(
    seed: Int = rangeForRandom.random(),
    width: Int = 300,
    height: Int = width,
): String = remember { randomSampleImageUrl(seed, width, height) }