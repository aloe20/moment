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

package com.aloe.moment.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aloe.moment.flu.FlutterLayout
import com.aloe.moment.react.ReactLayout

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLayout() {
    Column() {
        HorizontalPager(pageCount = 2, modifier = Modifier.weight(1F)) {
            when (it) {
                0 -> FlutterLayout()
                1 -> ReactLayout(url = "assets://index.android.bundle")
            }
        }
        
        Row(modifier = Modifier.height(56.dp)) {

        }
    }
}