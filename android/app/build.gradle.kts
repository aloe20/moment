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
    id("moment.android.application")
    id("com.facebook.react")
}

android {
    namespace = "com.aloe.moment"
    signingConfigs {
        create("sign") {
            storeFile = file("../key.jks")
            storePassword = "123456"
            keyAlias = "android"
            keyPassword = "123456"
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }
    defaultConfig {
        applicationId = "com.aloe.moment"
        versionCode = 1
        versionName = "1.0"
        signingConfig = signingConfigs.getByName("sign")
        resourceConfigurations.addAll(listOf("en", "zh"))
        vectorDrawables { useSupportLibrary = true }
        ndk { abiFilters.add("arm64-v8a") }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.+"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":flutter"))
    implementation(libs.androidx.activity)
    implementation(libs.compose.preview)
    implementation(libs.compose.material3)
    implementation("com.facebook.react:react-android")
    implementation("com.facebook.react:hermes-android")
    testImplementation(libs.junit)
}