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

package com.aloe.moment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseVm : ViewModel() {
    private val map = mutableMapOf<String,Job?>()
    fun loadData(key:String, callback: suspend () -> Unit) {
        map[key]?.cancel()
        map[key]=viewModelScope.launch {
            callback.invoke()
        }
    }

    override fun onCleared() {
        map.forEach {
            it.value?.cancel()
        }
        map.clear()
    }
}