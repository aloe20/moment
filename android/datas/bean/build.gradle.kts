/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("moment.android.library")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id ("kotlinx-serialization")
}

android {
    namespace = "com.aloe.bean"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
//    api(libs.squareup.moshi)
//    kapt(libs.squareup.moshi.codegen)
    api("androidx.room:room-ktx:2.5.0")
    ksp("androidx.room:room-compiler:2.5.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.5.0")
}