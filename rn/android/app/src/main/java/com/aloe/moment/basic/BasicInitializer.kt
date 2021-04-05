package com.aloe.moment.basic

import android.content.Context
import android.os.Build
import androidx.startup.Initializer
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import com.aloe.media.MediaInitializer
import com.aloe.socket.SocketInitializer
import com.facebook.react.modules.network.OkHttpClientProvider
import com.facebook.react.modules.network.ReactCookieJarContainer
import com.facebook.soloader.SoLoader
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import dagger.hilt.EntryPoints
import net.danlew.android.joda.JodaTimeInitializer

@Suppress("unused")
class BasicInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    val okHttpClient = EntryPoints.get(context, BasicInterface::class.java).getOkHttpClient()
    SoLoader.init(context, false)
    OkHttpClientProvider.setOkHttpClientFactory {
      okHttpClient.newBuilder().cookieJar(ReactCookieJarContainer()).build()
    }
    Coil.setImageLoader(
      ImageLoader.Builder(context).availableMemoryPercentage(0.25)
        .crossfade(true).diskCachePolicy(CachePolicy.ENABLED).okHttpClient(okHttpClient)
        .componentRegistry {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) add(ImageDecoderDecoder()) else add(GifDecoder())
        }.build()
    )
    QbSdk.initTbsSettings(
      mutableMapOf<String, Any>(
        TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
        TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true
      )
    )
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> =
    mutableListOf(JodaTimeInitializer::class.java, SocketInitializer::class.java, MediaInitializer::class.java)
}