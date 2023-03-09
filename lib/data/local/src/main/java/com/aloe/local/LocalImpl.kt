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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.aloe.proto.Banner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

internal class LocalImpl constructor(private val ctx: Context) : ILocal {
    private val settingsDataStore: DataStore<Preferences> =
        preferencesDataStore(name = "settings").getValue(ctx, Preferences::javaClass)

    override suspend fun getAssetsStr(name: String): String {
        return withContext(Dispatchers.IO) {
            async(Dispatchers.IO) {
                StringBuilder().apply {
                    BufferedReader(InputStreamReader(ctx.assets.open(name))).use {
                        var line = it.readLine()
                        while (line != null) {
                            append(line)
                            line = it.readLine()
                        }
                    }
                }
            }.await().toString()
        }
    }

    override suspend fun putPrivacyVisible(isVisible: Boolean) {
        settingsDataStore.edit {
            it[booleanPreferencesKey("privacy")] = isVisible
        }
    }

    override suspend fun getPrivacyVisible(): Flow<Boolean> = settingsDataStore.data.map {
        it[booleanPreferencesKey("privacy")] ?: true
    }

    override suspend fun putBanner(banner: List<Banner>) {
        ctx.bannerDataStore.updateData { it?.toBuilder()?.addAllBanners(banner)?.build() }
    }

    override suspend fun getBanner(): Flow<MutableList<Banner>?> = ctx.bannerDataStore.data.map { it?.bannersList }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
