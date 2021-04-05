package com.aloe.moment.basic

import android.app.Application
import android.os.StrictMode
import com.aloe.moment.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BasicApp : Application(){
  override fun onCreate() {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectResourceMismatches().detectAll().build())
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder().detectActivityLeaks()
        .detectFileUriExposure().detectLeakedClosableObjects().detectLeakedRegistrationObjects()
        .detectLeakedSqlLiteObjects().detectAll().build())
    }
    super.onCreate()
  }
}