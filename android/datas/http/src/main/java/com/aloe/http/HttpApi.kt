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

package com.aloe.http

import android.graphics.Bitmap
import com.aloe.bean.ArticleBean
import com.aloe.bean.HttpBean
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Streaming
import retrofit2.http.Url

internal interface HttpApi {
    @GET("banner/json")
    suspend fun loadBanner(): String

    @GET("/static/article/top.json")
    suspend fun loadTop(): HttpBean<List<ArticleBean>>

    @GET
    @Headers("Cache-Control:public,max-age=360000")
    @Streaming
    suspend fun loadImage(@Url url: String): Bitmap

    @GET
    @Streaming
    suspend fun download(@Url url: String, @Header("RANGE") range: String): ResponseBody
}