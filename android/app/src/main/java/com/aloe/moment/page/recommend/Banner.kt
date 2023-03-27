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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(
    modifier: Modifier = Modifier,
    isVertical: Boolean = false,
    count: Int,
    content: @Composable BoxScope.(Int) -> Unit,
) {
    Box(modifier = modifier) {
        val loopCount = Int.MAX_VALUE
        val startIndex = loopCount / 2
        val pagerState = rememberPagerState(initialPage = startIndex)
        fun pageMapper(index: Int): Int = (index - startIndex).let {
            if (count == 0) it else it - it.floorDiv(count) * count
        }
        if (isVertical) {
            VerticalPager(pageCount = loopCount, state = pagerState) { content(pageMapper(it)) }
            VerticalPagerIndicator(
                pagerState = pagerState,
                pageCount = count,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(4.dp),
                pageIndexMapping = ::pageMapper,
            )
        } else {
            HorizontalPager(pageCount = loopCount, state = pagerState) { content(pageMapper(it)) }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(4.dp),
                pageCount = count,
                pageIndexMapping = ::pageMapper,
            )
        }

        var underDragging by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = Unit) {
            pagerState.interactionSource.interactions.collectLatest { interaction ->
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
                        delay(2000L)
                        val current = pagerState.currentPage
                        val currentPos = pageMapper(current)
                        val nextPage = current + 1
                        if (underDragging.not()) {
                            val toPage = nextPage.takeIf { nextPage < loopCount }
                                ?: (currentPos + startIndex + 1)
                            if (toPage > current) {
                                pagerState.animateScrollToPage(toPage)
                            } else {
                                pagerState.scrollToPage(toPage)
                            }
                        }
                    }
                } catch (e: CancellationException) {
                    e.printStackTrace()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color.LightGray,
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = indicatorWidth,
    indicatorShape: Shape = CircleShape,
) {
    val indicatorWidthPx = LocalDensity.current.run { indicatorWidth.roundToPx() }
    val spacingPx = LocalDensity.current.run { spacing.roundToPx() }
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val indicatorModifier = Modifier
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = inactiveColor, shape = indicatorShape)
            repeat(pageCount) {
                Box(indicatorModifier)
            }
        }
        Box(
            Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffsetFraction
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceIn(
                            0f,
                            (pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat(),
                        )
                    IntOffset(
                        x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(),
                        y = 0,
                    )
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .then(
                    if (pageCount > 0) {
                        Modifier.background(
                            color = activeColor,
                            shape = indicatorShape,
                        )
                    } else {
                        Modifier
                    },
                ),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = Color.Red,
    inactiveColor: Color = Color.Gray,
    indicatorHeight: Dp = 8.dp,
    indicatorWidth: Dp = indicatorHeight,
    spacing: Dp = indicatorHeight,
    indicatorShape: Shape = CircleShape,
) {
    val indicatorHeightPx = LocalDensity.current.run { indicatorHeight.roundToPx() }
    val spacingPx = LocalDensity.current.run { spacing.roundToPx() }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val indicatorModifier = Modifier
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = inactiveColor, shape = indicatorShape)
            repeat(pageCount) {
                Box(indicatorModifier)
            }
        }
        Box(
            Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffsetFraction
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceIn(
                            0f,
                            (pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat(),
                        )
                    IntOffset(
                        x = 0,
                        y = ((spacingPx + indicatorHeightPx) * scrollPosition).toInt(),
                    )
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .then(
                    if (pageCount > 0) {
                        Modifier.background(
                            color = activeColor,
                            shape = indicatorShape,
                        )
                    } else {
                        Modifier
                    },
                ),
        )
    }
}
