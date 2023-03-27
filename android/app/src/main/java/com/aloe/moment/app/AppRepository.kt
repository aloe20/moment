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

package com.aloe.moment.app

import com.aloe.http.RemoteDataSource
import com.aloe.local.LocalDataSource
import javax.inject.Inject

abstract class LocalRepository(local: LocalDataSource) : LocalDataSource by local
class AppRepository @Inject constructor(local: LocalDataSource, remote: RemoteDataSource) :
    LocalRepository(local), RemoteDataSource by remote
