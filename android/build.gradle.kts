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
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.facebook.react:react-native-gradle-plugin")
    }
}
plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    kotlin("jvm").version(libs.versions.kotlin)
    kotlin("plugin.serialization").version(libs.versions.kotlin)
    alias(libs.plugins.spotless).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.ksp).apply(false)
    id("io.gitlab.arturbosch.detekt").version(libs.versions.detekt)
}
subprojects {
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    apply(plugin = "io.gitlab.arturbosch.detekt")
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
    detekt {
        buildUponDefaultConfig = true
        allRules = false
        config = files("$rootDir/config/detekt.yml")
        baseline = file("$rootDir/config/baseline.xml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "11"
    }
    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = "11"
    }
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:1.22.0")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-ruleauthors:1.22.0")
    }
    configurations.all {
        resolutionStrategy.eachDependency {
            when (requested.group) {
                "androidx.activity" -> useVersion(libs.versions.androidxActivity.get())
                "androidx.appcompat" -> useVersion(libs.versions.androidxAppcompat.get())
                "androidx.arch.core" -> useVersion(libs.versions.androidxArch.get())
                "androidx.collection" -> useVersion(libs.versions.androidxCollection.get())
                "androidx.lifecycle" -> useVersion(libs.versions.androidxLifecycle.get())
                "androidx.core" -> useVersion(libs.versions.androidxCore.get())
                "androidx.fragment" -> useVersion(libs.versions.androidxFragment.get())
                "androidx.tracing" -> useVersion(libs.versions.androidxTracing.get())
                "com.squareup.okhttp3" -> useVersion(libs.versions.okhttp.get())
                "com.squareup.okio" -> useVersion(libs.versions.okio.get())
                "org.jetbrains.kotlin" -> useVersion(libs.versions.kotlin.get())
            }
        }
    }
}

tasks.register<Copy>("copyHookCommit"){
    val file = file("../.git/hooks/pre-commit")
    if (!file.exists()) {
        from(file("hooks/pre-commit"))
        into(file.parentFile)
    }
}