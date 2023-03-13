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
 * 定义首页Banner数据类型，该对象包含标题[title]，描述[desc]，封面图[imagePath]和跳转链接[url]。
 */
@JsonClass(generateAdapter = true)
data class BannerBean(
    val title: String? = null,
    val desc: String? = null,
    val imagePath: String? = null,
    val url: String? = null
)
