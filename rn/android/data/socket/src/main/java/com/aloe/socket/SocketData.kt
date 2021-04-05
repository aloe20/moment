package com.aloe.socket

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface SocketData {
  fun sendUdpData(ip: String, port: Int, data: ByteArray)
  fun addReceiveUdpObserver(owner: LifecycleOwner, observer: Observer<ByteArray>)
  fun removeReceiveUdpObserver(owner: LifecycleOwner)
  fun removeReceiveUdpObserver(observer: Observer<ByteArray>)
}