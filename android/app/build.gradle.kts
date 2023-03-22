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
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("profile") {
            initWith(getByName("debug"))
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
    implementation(project(":data:local"))
    implementation(project(":data:http"))
    implementation(libs.androidx.activity)
    debugImplementation(libs.compose.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)
    implementation(libs.compose.hilt)
    implementation(libs.compose.lifecycle)
    implementation(libs.androidx.startup)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.google.hilt.android)
    implementation(libs.coil)
    kapt(libs.google.hilt.compiler)
    implementation("com.facebook.react:react-android")
    implementation("com.facebook.react:hermes-android")
    testImplementation(libs.junit)
    if ((gradle as ExtensionAware).extra["useAar"]==true) {
        debugImplementation("com.aloe.flu:flutter_debug:1.0")
        //profileImplementation("com.aloe.flu:flutter_profile:1.0")
        releaseImplementation("com.aloe.flu:flutter_release:1.0")
    } else {
        implementation(project(":flutter"))
    }
}
apply(from = "../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle")
(extra.get("applyNativeModulesAppBuildGradle") as groovy.lang.Closure<*>)(project)