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

/**
 * N日移动平均线指标，N日收市价总和/N.
 * MA(n)=SUM(c)/n
 *
 * n 周期，一般设置为N1=5，N2=10，N3=20，N4=60，N5=120，N6=250
 */
open class MaIndex : BaseIndexLine() {

    override fun initCycleAndColor() {
        args.add(Triple(5, Color.RED, "MA5"))
        args.add(Triple(10, Color.BLACK, "MA10"))
        args.add(Triple(20, Color.BLUE, "MA20"))
    }

    override fun calcIndex(cycle: Int, entries: List<KlineEntry>): List<Float> {
        var sum = 0F
        return entries.foldIndexed(mutableListOf()) { index, acc, entry ->
            sum += entry.close
            if (index < cycle - 1) {
                acc.add(Float.NaN)
            } else {
                if (index - cycle > -1) {
                    sum -= entries[index - cycle].close
                }
                acc.add(sum / cycle)
            }
            acc
        }
    }
}
