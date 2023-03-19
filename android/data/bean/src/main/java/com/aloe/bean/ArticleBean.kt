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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "article")
@JsonClass(generateAdapter = true)
data class ArticleBean(
    @PrimaryKey val id: Int=0,
    @ColumnInfo(name = "course_id") val courseId: Int = 0,
    @ColumnInfo(name = "chapter_id") val chapterId: Int = 0,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "author") val author: String? = null,
    @ColumnInfo(name = "super_chapter_name") val superChapterName: String? = null,
    @ColumnInfo(name = "nice_date") val niceDate: String? = null,
    @ColumnInfo(name = "nice_share_date") val niceShareDate: String? = null,
    @ColumnInfo(name = "chapter_name") val chapterName: String? = null,
    @ColumnInfo(name = "link") val link: String? = null
)
