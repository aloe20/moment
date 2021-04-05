package com.aloe.socket

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import javax.inject.Inject
import javax.inject.Named

internal class SocketDataImpl @Inject constructor(
  @Named("sendUdp") private val liveData: MutableLiveData<ByteArray>,
  @Named("receiveUdp") private val receiveLiveData: MutableLiveData<ByteArray>
) : SocketData {
  override fun sendUdpData(ip: String, port: Int, data: ByteArray) {
    liveData.value = ip.split(".").map { it.toInt().toByte() }.plus(port.shr(8).toByte())
      .plus(port.and(0xFF).toByte()).toByteArray().plus(data)
  }

  override fun addReceiveUdpObserver(owner: LifecycleOwner, observer: Observer<ByteArray>) {
    receiveLiveData.observe(owner, observer)
  }

  override fun removeReceiveUdpObserver(owner: LifecycleOwner) {
    receiveLiveData.removeObservers(owner)
  }

  override fun removeReceiveUdpObserver(observer: Observer<ByteArray>) {
    receiveLiveData.removeObserver(observer)
  }
}