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

import android.graphics.Color
import com.aloe.chart.KlineEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

/**
 * N日轨道线指标,由上轨线(UPPER)，下轨线(LOWER)和中轨线(ENE)组成.
 * UPPER=(1+M1/100)*MA(n)
 * LOWER=(1-M2/100)*MA(n)
 * ENE=(UPPER+LOWER)/2
 *
 * n 周期，一般设置为N1=5，N2=10，N3=20，N4=60，N5=120，N6=250
 * m1 上轨线偏移量，一般设为11
 * m2 下轨线偏移量，一般设为9
 * ENE指标三条线，第一个为上轨线，第二个为中轨线，第三个为下轨线
 */
class EneIndex : MaIndex() {

    override fun initCycleAndColor() {
        args.add(Triple(10, Color.RED, "UPPER"))
        args.add(Triple(11, Color.BLACK, "LOWER"))
        args.add(Triple(9, Color.BLUE, "ENE"))
    }

    override fun getLineDataSet(entries: List<KlineEntry>): List<LineDataSet> {
        return getEntry(args[0].first, entries).foldIndexed(
            mutableListOf<MutableList<Entry>>(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
            ),
        ) { index, acc, entry ->
            if (entry.y.isNaN()) {
                acc.forEach { it.add(Entry(index.toFloat(), Float.NaN)) }
            } else {
                val upper = (1 + args[1].first / 100F) * entry.y
                val lower = (1 - args[2].first / 100F) * entry.y
                val ene = (upper + lower) / 2F
                acc[0].add(Entry(index.toFloat(), upper))
                acc[1].add(Entry(index.toFloat(), lower))
                acc[2].add(Entry(index.toFloat(), ene))
            }
            acc
        }.foldIndexed(mutableListOf()) { index, acc, mutableList ->
            acc.also {
                it.add(
                    LineDataSet(mutableList, args[index].third).apply {
                        setDrawCircles(false)
                        isHighlightEnabled = false
                        color = args[index].second
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                    },
                )
            }
        }
    }
}
