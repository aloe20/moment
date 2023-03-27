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

import android.content.Context
import android.os.Environment
import com.aloe.bean.ArticleBean
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import javax.inject.Inject

internal class RemoteImplDataSource @Inject constructor(
    @ApplicationContext val ctx: Context,
    val api: HttpApi,
) : RemoteDataSource {
    override suspend fun loadBanner(): Result<String> = runCatching { api.loadBanner() }

    override suspend fun loadTop(): Result<List<ArticleBean>?> = runCatching { api.loadTop().data }

    override fun download(url: String, path: String?): Flow<Int> = flow {
        val name = url.substring(url.lastIndexOf("/") + 1)
        val file = if (path == null) {
            File(
                ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                name,
            )
        } else {
            File(path)
        }
        val body = api.download(url, "bytes=${file.length()}-")
        val length = body.contentLength()
        withContext(Dispatchers.IO) {
            runCatching {
                file.sink(true).buffer().use { sink ->
                    var size = file.length()
                    if (size < length) {
                        val buffer = body.byteStream().source().buffer()
                        val bytes = ByteArray(length.toInt())
                        while (size < length) {
                            buffer.read(bytes).takeIf { it > -1 }?.also {
                                size += it
                                sink.write(bytes, 0, it)
                                val progress = (1F * size * length / length).toInt()
                                emit(progress)
                            }
                        }
                        sink.flush()
                    }
                }
            }
        }
    }
}
