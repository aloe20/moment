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

package com.aloe.http.factory

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class BitmapConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? = if (type == Bitmap::class.java) BitmapResponseBodyConverter() else null

    companion object {
        fun create(): BitmapConverterFactory = BitmapConverterFactory()
        private class BitmapResponseBodyConverter : Converter<ResponseBody, Bitmap> {
            override fun convert(value: ResponseBody): Bitmap? {
                return value.use {
                    val bytes = value.bytes()
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
            }
        }
    }
}
