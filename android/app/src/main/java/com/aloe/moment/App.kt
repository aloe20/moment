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

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class App:Application() {
    @Inject
    lateinit var okHttpClient: OkHttpClient
}

val Context.imageLoader: ImageLoader
    get() = ImageLoader.Builder(this).okHttpClient((applicationContext as App).okHttpClient)
        .diskCache(DiskCache.Builder().directory(externalCacheDir?:cacheDir).build())
        .memoryCache(MemoryCache.Builder(this).build())
        .build()

@Composable
fun rememberAsyncPainter(model: Any?): AsyncImagePainter {
    val imageLoader = LocalContext.current.imageLoader
    return rememberAsyncImagePainter(model, imageLoader)
}