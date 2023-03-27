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

class CciIndex : MaIndex() {

    override fun initCycleAndColor() {
        args.add(Triple(26, Color.RED, "CCI"))
    }

    override fun calcIndex(cycle: Int, entries: List<KlineEntry>): List<Float> {
        val ma = super.calcIndex(cycle, entries)
        var mdSum = 0F
        return ma.foldIndexed(mutableListOf()) { index, acc, value ->
            if (index < (cycle - 1)) {
                acc.add(Float.NaN)
            } else {
                mdSum += value - entries[index].close
                val maTmp = ma[index - cycle]
                if (!maTmp.isNaN()) {
                    mdSum -= maTmp - entries[index - cycle].close
                }
                acc.add((entries[index].medium() - value) / (mdSum / cycle) / 0.015F)
            }
            acc
        }
    }
}
