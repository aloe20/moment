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

package com.aloe.chart.index

import com.aloe.chart.KlineEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

abstract class BaseIndexLine : IndexLine {
    protected val args: MutableList<Triple<Int, Int, String>> = mutableListOf()

    init {
        initCycleAndColor()
    }

    override fun getLineDataSet(entries: List<KlineEntry>): List<LineDataSet> {
        return args.fold(mutableListOf()) { acc, triple ->
            acc.apply {
                add(LineDataSet(getEntry(triple.first, entries), triple.third).apply {
                    color = triple.second
                    setDrawCircles(false)
                    isHighlightEnabled = false
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                })
            }
        }
    }

    override fun getEntry(cycle: Int, entries: List<KlineEntry>): List<Entry> {
        return calcIndex(cycle, entries).mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }
    }
}