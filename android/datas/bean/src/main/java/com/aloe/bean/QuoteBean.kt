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

package com.aloe.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuoteBean(
    val market: String = "",
    val code: String = "",
    val name: String = "",
    val upDown: Double = Double.NaN,
    val upDownSpeed: Double = Double.NaN,
    val volume: Int = Int.MIN_VALUE,
    val sa: Double = Double.NaN,
    val ratio: Double = Double.NaN,
    val totVal: Double = Double.NaN,
    val amount: Double = Double.NaN,
    val cirVal: Double = Double.NaN,
    val peRatio: Double = Double.NaN,
    val lastPrice: Double = Double.NaN,
    val preClosePrice: Double = Double.NaN,
    val turnoverRate: Double = Double.NaN,
    val flowMainOut: Double = Double.NaN,
    val flowMainIn: Double = Double.NaN,
    val flowMainNetIn: Double = Double.NaN
)
