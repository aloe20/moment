package com.aloe.http

import android.app.Application
import com.aloe.local.RepositoryLocalImpl
import com.aloe.socket.SocketData
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
internal class HttpModule {
  @Provides
  @Singleton
  fun getOkhttpClient(app: Application): OkHttpClient = TrustManager().let {
    val sf = SSLContext.getInstance("TLS").apply { init(null, arrayOf(it), SecureRandom()) }.socketFactory
    val hv = HostnameVerifier { _, _ -> true }
    HttpsURLConnection.setDefaultSSLSocketFactory(sf)
    HttpsURLConnection.setDefaultHostnameVerifier(hv)
    OkHttpClient.Builder().sslSocketFactory(sf, it)
      .cache(Cache(File(app.externalCacheDir, "OkHttp"), 500.shl(20).toLong()))
      .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
      .addNetworkInterceptor(CacheInterceptor()).hostnameVerifier(hv).build()
  }

  @Provides
  @Singleton
  fun getMoshi(): Moshi = Moshi.Builder().build()

  @Provides
  @Singleton
  fun getHttpApi(okHttpClient: OkHttpClient, moshi: Moshi): HttpApi = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .client(okHttpClient).baseUrl("https://httpbin.org/").build().create(HttpApi::class.java)

  @Provides
  @Singleton
  fun getRepository(http: RepositoryHttpImpl, local: RepositoryLocalImpl, socket: SocketData): Repository =
    Repository(local, http, socket)

  private inner class TrustManager : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
  }
}