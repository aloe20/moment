package com.aloe.moment.business.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.aloe.http.Repository
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class SplashVm @Inject constructor(private val repository: Repository) : BasicVm() {

  fun getImage(): LiveData<String> = repository.getSplashImg().map {
    var url = it.imgUrl
    val date = DateTime.now().toString("yyyyMMdd")
    if (url.isEmpty() || date != it.date) {
      url = "https://picsum.photos/id/${(0..1000).random()}/1080/1920"
      repository.updateSplashImg(date, url)
    }
    url
  }.asLiveData()

  override fun prepare() {
    repository.addReceiveUdpObserver(owner) {
      var ip = ""
      var port = 0
      it.forEachIndexed { index, byte ->
        if (index < 4) {
          @Suppress("EXPERIMENTAL_API_USAGE")
          ip = ip.plus(".").plus(byte.toUByte())
        } else if (index < 6) {
          port = port.shl(8).plus(byte)
        }
      }
      Log.i("aloe", "${ip.substring(1)}:$port -> ${it.copyOfRange(6, it.size).decodeToString()}")
    }
  }

  override fun onCleared() {
    super.onCleared()
    repository.removeReceiveUdpObserver(owner)
  }

  fun sendUdpData() {
    //repository.addReceiveUdpObserver()
    repository.sendUdpData("255.255.255.255", 10008, "aloe".toByteArray())
//    repository.sendUdpData(byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x27.toByte(), 0x18.toByte()).plus("aloe".toByteArray()))
  }
}