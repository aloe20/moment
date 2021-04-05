package com.aloe.http

import okhttp3.*
import okhttp3.internal.http.promisesBody
import okhttp3.internal.platform.Platform
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import kotlin.math.min


class HttpLoggingInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) : Interceptor {

  @Volatile
  private var headersToRedact = emptySet<String>()

  /**
   * 日志标记.
   */
  @set:JvmName("level")
  @Volatile
  var level = Level.NONE

  /**
   * 设置打印标记.
   */
  fun setLevel(level: Level) = apply { this.level = level }

  /**
   * 日志标记.
   */
  enum class Level {
    /**
     * 不打印日志.
     */
    NONE,

    /**
     * 只打印头信息.
     */
    HEADERS,

    /**
     * 打印所有信息.
     */
    BODY
  }

  /**
   * 日志打印接口.
   */
  interface Logger {
    /**
     * 打印日志.
     * @param message 日志信息
     */
    fun log(message: String)

    /**
     * 内部类.
     */
    companion object {
      private const val MAX_LEN = 3333

      /**
       * 默认日志接口类.
       */
      val DEFAULT: Logger = object : Logger {
        override fun log(message: String) {
          var len = MAX_LEN
          while (len < message.length) {
            Platform.get().log(message.substring(len - MAX_LEN, len))
            len += MAX_LEN
          }
          if (len >= message.length) {
            Platform.get().log(message.substring(len - MAX_LEN, min(len, message.length)))
          }
        }
      }
    }
  }

  /**
   * 请求拦截处理.
   * @param chain 请求参数
   * @return 返回请求的数据
   */
  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val level = this.level
    val request = chain.request()
    if (level == Level.NONE || request.header("noLog") != null) {
      return chain.proceed(request)
    }
    val logBody = level == Level.BODY
    val logHeaders = logBody || level == Level.HEADERS
    val requestBody = request.body
    logStartMessage(chain, request, requestBody, logHeaders)
    if (logHeaders) {
      logHeaderAndBody(request, requestBody, logBody)
    }
    val startNs = System.nanoTime()
    val response: Response
    try {
      response = chain.proceed(request)
    } catch (e: Exception) {
      logger.log("<-- HTTP FAILED: $e")
      throw e
    }
    val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
    val responseBody = response.body!!
    val contentLength = responseBody.contentLength()
    val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
    logger.log("<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})")
    return logHeader(logHeaders, logBody, response, responseBody, contentLength)
  }

  private fun logStartMessage(
    chain: Interceptor.Chain,
    request: Request,
    requestBody: RequestBody?,
    logHeaders: Boolean
  ) {
    chain.connection()
      .let { ("--> ${request.method} ${request.url}${if (it != null) " " + it.protocol() else ""}") }
      .let { msg ->
        msg.takeUnless { logHeaders }?.takeUnless { requestBody == null }
          ?.let { "$it (${requestBody?.contentLength()}-byte body)" } ?: msg
      }.also { logger.log(it) }
  }

  private fun logHeader(
    logHeaders: Boolean,
    logBody: Boolean,
    response: Response,
    responseBody: ResponseBody,
    contentLength: Long
  ): Response {
    if (logHeaders) {
      val headers = response.headers
      for (i in 0 until headers.size) {
        logHeader(headers, i)
      }
      if (!logBody || !response.promisesBody()) {
        logger.log("<-- END HTTP")
      } else if (bodyHasUnknownEncoding(response.headers)) {
        logger.log("<-- END HTTP (encoded body omitted)")
      } else {
        return logEnd(response, responseBody, headers, contentLength) ?: response
      }
    }
    return response
  }

  private fun logHeaderAndBody(request: Request, requestBody: RequestBody?, logBody: Boolean) {
    val headers = request.headers
    requestBody?.also { body ->
      body.contentType()?.takeIf { headers["Content-Type"] == null }?.let { logger.log("Content-Type: $it") }
      body.takeIf { headers["Content-Length"] == null }?.takeUnless { it.contentLength() == -1L }
        ?.also { logger.log("Content-Length: ${it.contentLength()}") }
    }
    for (i in 0 until headers.size) {
      logHeader(headers, i)
    }
    if (!logBody || requestBody == null) {
      logger.log("--> END ${request.method}")
    } else if (bodyHasUnknownEncoding(request.headers)) {
      logger.log("--> END ${request.method} (encoded body omitted)")
    } else if (requestBody.isDuplex()) {
      logger.log("--> END ${request.method} (duplex request body omitted)")
    } else if (requestBody.isOneShot()) {
      logger.log("--> END ${request.method} (one-shot body omitted)")
    } else {
      val buffer = Buffer()
      requestBody.writeTo(buffer)
      val contentType = requestBody.contentType()
      val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
      logger.log("")
      if (buffer.isProbablyUtf8()) {
        logger.log(buffer.readString(charset))
        logger.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
      } else {
        logger.log("--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
      }
    }
  }

  private fun logEnd(
    response: Response,
    responseBody: ResponseBody,
    headers: Headers,
    contentLength: Long
  ): Response? {
    val source = responseBody.source()
    source.request(Long.MAX_VALUE) // Buffer the entire body.
    var buffer = source.buffer
    var gzippedLength: Long? = null
    if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
      gzippedLength = buffer.size
      GzipSource(buffer.clone()).use { gzippedResponseBody ->
        buffer = Buffer()
        buffer.writeAll(gzippedResponseBody)
      }
    }
    val contentType = responseBody.contentType()
    val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
    if (!buffer.isProbablyUtf8()) {
      logger.log("")
      logger.log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
      return response
    }
    if (contentLength != 0L) {
      logger.log("")
      logger.log(buffer.clone().readString(charset))
    }
    if (gzippedLength != null) {
      logger.log("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
    } else {
      logger.log("<-- END HTTP (${buffer.size}-byte body)")
    }
    return null
  }

  private fun logHeader(headers: Headers, i: Int) {
    val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
    logger.log(headers.name(i) + ": " + value)
  }

  private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
    val contentEncoding = headers["Content-Encoding"] ?: return false
    return !contentEncoding.equals("identity", ignoreCase = true)
        && !contentEncoding.equals("gzip", ignoreCase = true)
  }

  private fun Buffer.isProbablyUtf8(): Boolean {
    try {
      val prefix = Buffer()
      val byteCount = size.coerceAtMost(MAX_VALUE)
      copyTo(prefix, 0, byteCount)
      for (i in 0 until LEN) {
        if (prefix.exhausted()) {
          break
        }
        val codePoint = prefix.readUtf8CodePoint()
        if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
          return false
        }
      }
      return true
    } catch (_: EOFException) {
      return false // Truncated UTF-8 sequence.
    }
  }

  /**
   * 内部参数类.
   */
  companion object {
    /**
     * 长度.
     */
    private const val LEN = 16

    /**
     * 最大值.
     */
    private const val MAX_VALUE = 64L
  }
}
