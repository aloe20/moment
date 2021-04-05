package com.aloe.moment.business.web

import android.content.Context
import android.util.AttributeSet
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class WebLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  WebView(context, attrs, defStyleAttr) {
  private var onProgress: ((Int) -> Unit)? = null
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    webViewClient = WebViewClient()
    webChromeClient = object : WebChromeClient() {
      override fun onProgressChanged(p0: WebView?, p1: Int) {
        super.onProgressChanged(p0, p1)
        onProgress?.invoke(p1)
      }
    }
  }

  fun setOnProgress(block: (Int) -> Unit) = apply { onProgress = block }
}