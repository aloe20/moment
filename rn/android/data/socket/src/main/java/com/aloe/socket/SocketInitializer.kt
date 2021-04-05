package com.aloe.socket

import android.content.Context
import androidx.startup.Initializer

class SocketInitializer: Initializer<Unit> {
  override fun create(context: Context) {
    SocketService.enqueueWork(context)
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}