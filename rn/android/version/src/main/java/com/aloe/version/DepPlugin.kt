@file:Suppress("unused")

package com.aloe.version

import org.gradle.api.Plugin
import org.gradle.api.Project

class DepPlugin : Plugin<Project> {
  override fun apply(target: Project) = Unit
}

object Versions {
  private const val minSdk = 23
  private const val compileSdk = 30
  private const val targetSdk = 30
  private const val buildTools = "30.0.3"
  const val compose = "1.0.0-beta04"
}

object Libs {
  //https://github.com/Kotlin/kotlinx.coroutines
  private const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"

  //https://github.com/square/retrofit
  private const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:2.9.0"

  //https://github.com/square/moshi
  private const val moshiGen = "com.squareup.moshi:moshi-kotlin-codegen:1.12.0"

  //https://github.com/facebook/react-native
  private const val reactNative = "com.facebook.react:react-native:0.64.0"
  private const val jsc = "org.webkit:android-jsc:r245459"

  private const val x5web = "com.tencent.tbs.tbssdk:sdk:43993"

  //https://github.com/dlew/joda-time-android
  private const val joda = "net.danlew:android.joda:2.10.9.1"

  //https://github.com/coil-kt/coil
  private const val coilGif = "io.coil-kt:coil-gif:1.1.1"

  //https://github.com/netty/netty
  private const val nettyCodec = "io.netty:netty-codec:4.1.63.Final"

  private const val junit = "junit:junit:4.13.2"

  object Dagger {
    //https://github.com/google/dagger
    private const val version = "2.34-beta"
    private const val android = "com.google.dagger:hilt-android:$version"
    private const val compiler = "com.google.dagger:hilt-android-compiler:$version"
  }

  object Protobuf {
    //https://github.com/protocolbuffers/protobuf
    private const val version = "3.15.7"
    private const val lite = "com.google.protobuf:protobuf-javalite:$version"
    private const val protoc = "com.google.protobuf:protoc:$version"
  }

  object Video {
    //https://github.com/CarGuo/GSYVideoPlayer
    private const val version = "8.1.2"
    private const val java = "com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-java:$version"
    private const val exo = "com.github.CarGuo.GSYVideoPlayer:GSYVideoPlayer-exo2:$version"
  }

  object Accompanist {
    //https://github.com/chrisbanes/accompanist
    private const val version = "0.7.1"
    private const val coil = "com.google.accompanist:accompanist-coil:$version"
    private const val insets = "com.google.accompanist:accompanist-insets:$version"
    private const val pager = "com.google.accompanist:accompanist-pager:$version"
    private const val indicators = "com.google.accompanist:accompanist-pager-indicators:$version"
  }

  object Androidx {
    //https://maven.google.com/web/index.html
    private const val roomVersion = "2.3.0-rc01"
    private const val lifecycleVersion = "2.3.1"
    private const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    private const val composeLiveData = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    private const val datastore = "androidx.datastore:datastore-preferences:1.0.0-alpha08"
    private const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    private const val liveDataRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
    private const val liveDataCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha04"
    private const val navigationFrag = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    private const val paging = "androidx.paging:paging-runtime-ktx:3.0.0-beta03"
    private const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    private const val room = "androidx.room:room-ktx:$roomVersion"
    private const val startup = "androidx.startup:startup-runtime:1.0.0"
    private const val material = "com.google.android.material:material:1.3.0"
  }
}
