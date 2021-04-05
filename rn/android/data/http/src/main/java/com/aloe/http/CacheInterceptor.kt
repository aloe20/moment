package com.aloe.http

import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
  private val time = 24 * 3600
  override fun intercept(chain: Interceptor.Chain): Response {
    val req = chain.request()
    var res = chain.proceed(req)
    if (req.url.host.contains("picsum.photos") || req.url.toString().endsWith("png|jpg|jpeg|gif")) {
      res = res.newBuilder().removeHeader("pragma").header("Cache-Control", "max-age=$time").build()
    }
    return res
  }
}