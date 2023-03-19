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
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    private val moshi: Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun getExecutor(): ExecutorService {
        val count = Runtime.getRuntime().availableProcessors()
        return ThreadPoolExecutor(
            max(2, min(4, count - 1)),
            count.shl(1) + 1,
            30,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(10)
        )
    }

    @Provides
    @Singleton
    fun getOkHttpClient(
        @ApplicationContext ctx: Context,
        executor: ExecutorService
    ): OkHttpClient = OkHttpClient.Builder().dispatcher(Dispatcher(executor)).cache(
        Cache(ctx.cacheDir, 1024_1000_100)
    ).build()

    @Provides
    @Singleton
    fun getHttpApi(client: OkHttpClient): HttpApi = Retrofit.Builder()
        //.baseUrl("http://httpbin.org/")
        .baseUrl("http://192.168.137.1:3000/")
        .addConverterFactory(BitmapConverterFactory.create())
        .addConverterFactory(EnumConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .client(client).build().create()

    /*@Provides
    @Singleton
    fun getDataSource(@ApplicationContext ctx: Context, client: OkHttpClient): RemoteDataSource {
        return RemoteImplDataSource(
            ctx, Retrofit.Builder()
            //.baseUrl("http://httpbin.org/")
            .baseUrl("http://192.168.137.1:3000/")
            .addConverterFactory(BitmapConverterFactory.create()).addConverterFactory(EnumConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .client(client).build().create(),
            LocalImpl(ctx)
        )
    }*/
}

@Module(includes = [HttpModule::class])
@InstallIn(SingletonComponent::class)
internal abstract class AbsHttpModule {

    @Binds
    @Singleton
    abstract fun getHttp(impl: RemoteImplDataSource): RemoteDataSource
}