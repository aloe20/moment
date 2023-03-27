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

package com.aloe.local

import android.content.Context
import androidx.room.Room
import com.aloe.local.room.AppDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalModule {
    @Binds
    @Singleton
    abstract fun getLocal(impl: LocalImplDataSource): LocalDataSource
}

@Module
@InstallIn(SingletonComponent::class)
internal class RoomModule {
    @Provides
    @Singleton
    fun getRoomDb(@ApplicationContext ctx: Context): AppDatabase = Room.databaseBuilder(
        ctx,
        AppDatabase::class.java,
        "moment",
    ).build()
}
