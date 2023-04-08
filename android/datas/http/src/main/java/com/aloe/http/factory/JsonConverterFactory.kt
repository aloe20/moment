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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class JsonConverterFactory : Converter.Factory() {

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody> {
        Json.serializersModule.serializer(type)
        return RequestBodyConverter(Json.serializersModule.serializer(type))
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *> {
        return ResponseConverterFactory(Json.serializersModule.serializer(type))
    }

    companion object {
        fun create(): JsonConverterFactory = JsonConverterFactory()
        class RequestBodyConverter<T>(private val saver: SerializationStrategy<T>) :
            Converter<T, RequestBody> {
            private val contentType = "application/json".toMediaType()
            override fun convert(value: T): RequestBody = Json.encodeToString(saver, value)
                .toRequestBody(contentType)
        }

        class ResponseConverterFactory<T>(private val loader: DeserializationStrategy<T>) :
            Converter<ResponseBody, T> {
            override fun convert(value: ResponseBody): T? = Json.decodeFromString(
                loader,
                value.string(),
            )
        }
    }
}
