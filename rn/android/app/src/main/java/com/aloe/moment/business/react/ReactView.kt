package com.aloe.moment.business.react

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.aloe.moment.BuildConfig
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.ReactRootView
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.network.OkHttpClientProvider
import com.facebook.react.shell.MainReactPackage
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException

class ReactView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
  ReactRootView(context, attrs, defStyle), LifecycleEventObserver {
  private var backBtnHandler: DefaultHardwareBackBtnHandler? = null
  private var activity: FragmentActivity? = null

  init {
    initActivity(context)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    findViewTreeLifecycleOwner()?.lifecycle?.addObserver(this)
  }

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
      Lifecycle.Event.ON_RESUME -> reactInstanceManager?.onHostResume(activity, backBtnHandler)
      Lifecycle.Event.ON_PAUSE -> reactInstanceManager?.onHostPause(activity)
      Lifecycle.Event.ON_DESTROY -> {
        reactInstanceManager?.onHostDestroy(activity)
        unmountReactApplication()
      }
      else -> Unit
    }
  }

  private fun initActivity(context: Context) {
    activity = (context as ContextWrapper).baseContext as AppCompatActivity
  }

  /**
   * assets://index.android.bundle
   * file://sdcard/myapp_cache/index.android.bundle
   * http://host/index.android.bundle
   */
  @MainThread
  fun loadPage(scope: CoroutineScope? = null, jsBundle: Uri?) {
    if (jsBundle != null && (jsBundle.scheme == "http" || jsBundle.scheme == "https")) {
      val dir = File(context.filesDir, "bundle")
      if (!dir.exists()) {
        dir.mkdirs()
      }
      val name = jsBundle.lastPathSegment ?: "index.bundle"
      val file = File(dir, name)
      if (file.exists() && file.length() > 0) {
        realLoadPage(Uri.fromFile(file))
      } else {
        scope?.launch(Dispatchers.IO) {
          OkHttpClientProvider.getOkHttpClient().newCall(Request.Builder().url(jsBundle.toString()).build())
            .enqueue(object : Callback {
              override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
              }

              override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && response.code == 200) {
                  if (!file.exists()) {
                    file.createNewFile()
                  }
                  response.body?.source()?.also { source ->
                    file.sink().buffer().also { sink ->
                      sink.writeAll(source)
                      sink.flush()
                    }
                  }
                  launch(Dispatchers.Main) { realLoadPage(Uri.fromFile(file)) }
                }
              }
            })
        }
      }
    } else {
      realLoadPage(jsBundle)
    }
  }

  private fun realLoadPage(jsBundle: Uri?) {
    startReactApplication(
      ReactHost(context.applicationContext as Application, jsBundle).reactInstanceManager, "rn", null
    )
  }

  fun setBackBtnHandler(handler: DefaultHardwareBackBtnHandler) = apply { backBtnHandler = handler }

  companion object {
    private class ReactHost(application: Application, private val jsBundle: Uri?) : ReactNativeHost(application) {
      override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

      override fun getPackages(): MutableList<ReactPackage> = mutableListOf(MainReactPackage())

      override fun getJSMainModuleName(): String = "index"

      override fun getJSBundleFile(): String? = if (jsBundle == null) null else when (jsBundle.scheme) {
        "assets" -> jsBundle.toString()
        "file" -> jsBundle.path
        else -> null
      }
    }
  }
}