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

package com.aloe.chart

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import kotlin.math.abs

class KlineChartTouchListener(val chart: KlineCombinedChart, touchMatrix: Matrix, dragTriggerDistance: Float) :
    BarLineChartTouchListener(chart, touchMatrix, dragTriggerDistance) {
    private val touchSlop: Int = ViewConfiguration.get(chart.context).scaledTouchSlop
    private var x = 0F
    private var y = 0F

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val result = super.onTouch(v, event)
        when (event.action.and(MotionEvent.ACTION_MASK)) {
            MotionEvent.ACTION_DOWN -> {
                x = event.x
                y = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if ((mChart as KlineCombinedChart).highlightShow && (abs(event.x - x) > touchSlop || abs(event.y - y) > touchSlop)) {
                    //高亮选中线显示时，滑动则移动高度选中线。滑动距离需要大于最小有效距离，否则算点击事件处理
                    performHighlightDrag(event)
                }
            }
        }
        return result
    }

    /**
     * 移动高亮选中线.
     * @param e 移动事件，获取移动坐标
     */
    private fun performHighlightDrag(e: MotionEvent) {
        mChart.getHighlightByTouchPoint(e.x, e.y)?.takeUnless { it.equalTo(mLastHighlighted) }?.also {
            mLastHighlighted = it
            mChart.highlightValue(it, true)
        }
    }
}