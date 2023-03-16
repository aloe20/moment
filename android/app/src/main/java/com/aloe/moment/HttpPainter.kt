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

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.aloe.http.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

val Context.imageLoader: ImageLoader
    get() = (applicationContext as App).http

@Composable
fun rememberHttpPainter(url:String): HttpPainter {
    val context = LocalContext.current
    return remember { HttpPainter(context.imageLoader, url) }
}

class HttpPainter(private val imageLoader: ImageLoader, private val url: String) : Painter(), RememberObserver {
    private var rememberScope: CoroutineScope? = null
    private var bitmap: Bitmap? by mutableStateOf(null)

    override val intrinsicSize: Size =
        if (bitmap == null) Size.Unspecified else Size(bitmap!!.width.toFloat(), bitmap!!.height.toFloat())

    override fun DrawScope.onDraw() {
        bitmap?.also { drawImage(it.asImageBitmap()) }
    }

    override fun onAbandoned() {
        rememberScope?.cancel()
        rememberScope = null
    }

    override fun onForgotten() {
        rememberScope?.cancel()
        rememberScope = null
    }

    override fun onRemembered() {
        if (rememberScope != null) return
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        rememberScope = scope
        scope.launch {
            bitmap = imageLoader.loadImage(url).getOrNull()
        }
    }
}