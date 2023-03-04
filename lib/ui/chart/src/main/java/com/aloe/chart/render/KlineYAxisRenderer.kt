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

package com.aloe.chart.render

import android.graphics.Canvas
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class KlineYAxisRenderer(
    viewPortHandler: ViewPortHandler, yAxis: YAxis, trans: Transformer
) : YAxisRenderer(viewPortHandler, yAxis, trans) {
    private val labelVerticalOffset = Utils.convertDpToPixel(5F)

    override fun drawYLabels(c: Canvas, fixedPosition: Float, positions: FloatArray, offset: Float) {
        //super.drawYLabels(c, fixedPosition, positions, offset)
        val from = if (mYAxis.isDrawBottomYLabelEntryEnabled) 0 else 1
        val to = if (mYAxis.isDrawTopYLabelEntryEnabled) mYAxis.mEntryCount else mYAxis.mEntryCount - 1
        val medium = (from + to) / 2
        for (i in from until to) {
            val offset2 = when {
                i < medium -> offset - labelVerticalOffset + 1
                i > medium -> offset + labelVerticalOffset + 1
                else -> offset
            }
            val text = mYAxis.getFormattedLabel(i)
            c.drawText(text, fixedPosition + 1, positions[i * 2 + 1] + offset2, mAxisLabelPaint)
        }
    }
}