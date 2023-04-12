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

package com.aloe.basic

import android.util.Log

var logDebug = true
private const val stackTraceLen = 4
fun String?.log(type: Int = Log.DEBUG, tag: String = "aloe", tr:Throwable?=null) {
    takeIf { logDebug }?.let {
        with(Thread.currentThread().stackTrace[stackTraceLen]) { "$methodName($fileName:$lineNumber) $it" }
    }?.also {
        when (type) {
            Log.DEBUG -> Log.d(tag, it, tr)
            Log.INFO -> Log.i(tag, it, tr)
            Log.WARN -> Log.w(tag, it, tr)
            Log.ERROR -> Log.e(tag, it, tr)
        }
    }
}