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

package com.aloe.moment.page.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aloe.moment.page.web.WebLayout

val LocalNav = compositionLocalOf<NavHostController> { error("no provider value") }

@Composable
fun MainHost() {
    CompositionLocalProvider(LocalNav provides rememberNavController()) {
        NavHost(
            navController = LocalNav.current,
            startDestination = "mainPage",
        ) {
            composable("mainPage") { MainLayout() }
            composable(
                "web?url={url}",
                arguments = listOf(navArgument("url") { defaultValue = "" }),
            ) {
                WebLayout(url = it.arguments?.getString("url") ?: "")
            }
        }
    }
}
