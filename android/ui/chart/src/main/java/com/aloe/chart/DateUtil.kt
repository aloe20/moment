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

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.CHINESE)
    private val date = Date()

    fun formatDate(time: Long, pattern: String = "yyyy/MM/dd"): String {
        simpleDateFormat.applyPattern(pattern)
        date.time = time
        return simpleDateFormat.format(date)
    }
}
