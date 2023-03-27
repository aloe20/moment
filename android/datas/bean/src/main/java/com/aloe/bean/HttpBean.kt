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

/**
 * Http数据包装类型，该对象包含错误码[errorCode]，0代表有数据，错误描述[errorMsg]和业务数据[data]。
 */
@JsonClass(generateAdapter = true)
data class HttpBean<T>(val errorCode: Int? = 0, val errorMsg: String? = "", val data: T? = null)
