package com.aloe.media.audio

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.ref.WeakReference

object AudioPlayer {
  private lateinit var contextRef: WeakReference<Context>
  internal val audioStateLiveData = MutableLiveData<AudioBean>()
  fun init(context: Context) {
    this.contextRef = WeakReference(context)
  }

  fun startAudio(bean: AudioBean) {
    startService(AudioService.TYPE_DATA, bean)
  }

  fun toggleAudio() {
    startService(AudioService.TYPE_TOGGLE)
  }

  fun addAudioStateObserver(owner: LifecycleOwner, observer: Observer<AudioBean>) {
    audioStateLiveData.observe(owner, observer)
  }

  fun removeAudioStateObserver(observer: Observer<AudioBean>) {
    audioStateLiveData.removeObserver(observer)
  }

  fun removeAudioStateObservers(owner: LifecycleOwner) {
    audioStateLiveData.removeObservers(owner)
  }

  private fun startService(type: Int, bean: AudioBean? = null) {
    contextRef.get()?.also {
      val intent = Intent(it, AudioService::class.java).putExtra(AudioService.KEY_TYPE, type)
        .putExtra(AudioService.KEY_DATA, bean)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        it.startForegroundService(intent)
      } else {
        it.startService(intent)
      }
    }
  }
}