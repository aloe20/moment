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

package com.aloe.local.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.aloe.proto.BannerList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

internal object BannerSerializer : Serializer<BannerList?> {
    override val defaultValue: BannerList = BannerList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): BannerList? =
        runCatching { BannerList.parseFrom(input) }.getOrNull()

    override suspend fun writeTo(t: BannerList?, output: OutputStream) {
        withContext(Dispatchers.IO) {
            launch(Dispatchers.IO) {
                t?.writeTo(output)
            }
        }
    }
}

internal val Context.bannerDataStore: DataStore<BannerList?> by dataStore(
    "banner.pb",
    BannerSerializer,
)
