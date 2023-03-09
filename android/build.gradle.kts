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

plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.spotless).apply(false)
}
subprojects {
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint(libs.versions.ktlint.get()).userData(mapOf("android" to "true"))
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("**/build/**/*.kts")
            licenseHeaderFile(rootProject.file("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
        }
    }
    configurations.all {
        resolutionStrategy.eachDependency {
            when (requested.group) {
                "androidx.activity" -> useVersion(libs.versions.androidxActivity.get())
                "androidx.appcompat" -> useVersion(libs.versions.androidxAppcompat.get())
                "androidx.lifecycle" -> useVersion(libs.versions.androidxLifecycle.get())
                "androidx.compose.animation" -> useVersion(libs.versions.androidxCompose.get())
                "androidx.compose.foundation" -> useVersion(libs.versions.androidxCompose.get())
                "androidx.compose.material" -> useVersion(libs.versions.androidxCompose.get())
                "androidx.core" -> useVersion(libs.versions.androidxCore.get())
                "androidx.fragment" -> useVersion(libs.versions.androidxFragment.get())
                "org.jetbrains.kotlin" -> useVersion(libs.versions.kotlin.get())
            }
        }
    }
}