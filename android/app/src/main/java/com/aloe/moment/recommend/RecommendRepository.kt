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

package com.aloe.moment.recommend

import com.aloe.bean.ArticleBean
import com.aloe.http.RemoteDataSource
import com.aloe.local.LocalDataSource
import com.aloe.proto.Banner
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import javax.inject.Inject

@ViewModelScoped
class RecommendRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {
    fun loadBanner(): Flow<MutableList<Banner>?> = local.getBanner().map {
        val list = mutableListOf<Banner>()
        if (it.isNullOrEmpty()) {
            remote.loadBanner().getOrNull()?.also { data ->
                runCatching {
                    JSONObject(data).optJSONArray("data")?.also {
                        val length = it.length()
                        for (i in 0 until length) {
                            val newBuilder = Banner.newBuilder()
                            val json = it.getJSONObject(i)
                            if (json.has("title")) newBuilder.title = json.getString("title")
                            if (json.has("desc")) newBuilder.desc = json.getString("desc")
                            if (json.has("imagePath")) newBuilder.imagePath =
                                json.getString("imagePath")
                            if (json.has("url")) newBuilder.url = json.getString("url")
                            list.add(newBuilder.build())
                        }
                        if (length > 0) local.putBanner(list)
                    }
                }
            }
        }
        it ?: list
    }

    suspend fun loadTop():List<ArticleBean> {
        var top = local.getTop()
        if (top.isEmpty()) {
            remote.loadTop().getOrNull()?.also {
              local.putTop(it)
                top=it
            }
        }
        return top
    }
}