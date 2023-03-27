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

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EnumConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody>? {
        return if ((type as? Class<*>)?.isEnum == true) EnumRequestConverter() else null
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        return if (type == String::class.java) StringResponseConverter() else null
    }

    companion object {
        fun create(): EnumConverterFactory = EnumConverterFactory()
        private class EnumRequestConverter : Converter<Enum<*>, RequestBody> {
            override fun convert(value: Enum<*>): RequestBody {
                return value.toString()
                    .toRequestBody("application/json; charset=UTF-8".toMediaType())
            }
        }

        private class StringResponseConverter : Converter<ResponseBody, String> {
            override fun convert(value: ResponseBody): String = value.use {
                String(it.bytes())
            }
        }
    }
}
