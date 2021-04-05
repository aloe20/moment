package com.aloe.socket

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
internal class SocketService : JobIntentService() {
  @Inject
  @Named("sendUdp")
  lateinit var sendLiveData: MutableLiveData<ByteArray>
  @Inject
  @Named("receiveUdp")
  lateinit var receiveLiveData: MutableLiveData<ByteArray>
  private lateinit var clientUdp: ClientUdp
  override fun onCreate() {
    super.onCreate()
    clientUdp = ClientUdp(sendLiveData, receiveLiveData)
  }

  override fun onHandleWork(intent: Intent) {
    clientUdp.run(8887)
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.e("aloe", "---> onDestroy")
  }

  companion object {
    fun enqueueWork(context: Context) {
      enqueueWork(context, SocketService::class.java, 100111, Intent())
    }
  }
}