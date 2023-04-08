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
import com.aloe.http.factory.BitmapConverterFactory
import com.aloe.http.factory.EnumConverterFactory
import com.aloe.http.factory.JsonConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Module
@InstallIn(SingletonComponent::class)
internal class HttpModule {
    private val cacheSize = 102_400_000L
    private val coreMinSize = 2
    private val coreMaxSize = 4
    private val outTime = 30L
    private val queueSize = 10

    @Provides
    @Singleton
    fun getExecutor(): ExecutorService {
        val count = Runtime.getRuntime().availableProcessors()
        return ThreadPoolExecutor(
            max(coreMinSize, min(coreMaxSize, count - 1)),
            count.shl(1) + 1,
            outTime,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(queueSize),
        )
    }

    @Provides
    @Singleton
    fun getOkHttpClient(
        @ApplicationContext ctx: Context,
        executor: ExecutorService,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .dispatcher(Dispatcher(executor))
        .cache(Cache(ctx.cacheDir, cacheSize))
        .build()

    @Provides
    @Singleton
    fun getHttpApi(client: OkHttpClient): HttpApi = Retrofit.Builder()
        // .baseUrl("http://httpbin.org/")
        .baseUrl("http://192.168.1.6:8000/")
        .addConverterFactory(BitmapConverterFactory.create())
        .addConverterFactory(EnumConverterFactory.create())
        .addConverterFactory(JsonConverterFactory.create())
        .client(client)
        .build()
        .create()
}

@Module(includes = [HttpModule::class])
@InstallIn(SingletonComponent::class)
internal interface AbsHttpModule {

    @Binds
    @Singleton
    fun getHttp(impl: RemoteImplDataSource): RemoteDataSource
}
