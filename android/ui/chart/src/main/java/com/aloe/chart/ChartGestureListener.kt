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

import android.view.MotionEvent
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import java.lang.ref.WeakReference

class ChartGestureListener(chart: KlineCombinedChart) : OnChartGestureListener {
    private var startIndex = -1
    private var endIndex = -1
    private var isLongPress = false
    private val chart: WeakReference<KlineCombinedChart> = WeakReference(chart)

    override fun onChartGestureEnd(me: MotionEvent, cg: ChartTouchListener.ChartGesture) {
        isLongPress = false
        updateRange(true)
    }

    override fun onChartFling(me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) {
        isLongPress = false
        updateRange(false)
    }

    override fun onChartSingleTapped(me: MotionEvent) {
        chart.get()?.highlightValue(null, true)
    }

    override fun onChartGestureStart(me: MotionEvent, cg: ChartTouchListener.ChartGesture) {
        isLongPress = true
        updateRange(false)
    }

    override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {
        updateRange(true)
    }

    override fun onChartLongPressed(me: MotionEvent) {
        chart.get()?.takeIf { isLongPress }?.apply {
            highlightValue(getHighlightByTouchPoint(me.x, me.y), true)
        }
    }

    override fun onChartDoubleTapped(me: MotionEvent) {

    }

    override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
        updateRange(false)
    }

    private fun updateRange(forceUpdate: Boolean) {
        chart.get()?.apply {
            val range = getRange()
            if (forceUpdate || (startIndex != range[0] && endIndex != range[1])) {
                startIndex = range[0]
                endIndex = range[1]
                updateRange(startIndex, endIndex)
            }
        }
    }
}