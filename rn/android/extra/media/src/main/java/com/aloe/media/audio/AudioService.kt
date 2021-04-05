package com.aloe.media.audio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.aloe.media.MediaPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

class AudioService : Service() {
  private var bean: AudioBean? = null
  private lateinit var mediaPlayer: MediaPlayer

  //private lateinit var imageAdapter: MediaImageAdapter
  override fun onBind(p0: Intent?): IBinder? = null

  override fun onCreate() {
    super.onCreate()
    mediaPlayer = MediaPlayer(this)
    mediaPlayer.playTag = AudioService::class.java.simpleName
    mediaPlayer.playPosition = -1
    //imageAdapter = EntryPoints.get(applicationContext, MediaInterface::class.java).getMediaImageAdapter()
    mediaPlayer.setGSYStateUiListener {
      if (bean != null) {
        bean?.state = it
        AudioPlayer.audioStateLiveData.value = bean
        handleNotification(bean)
      }
    }
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val result = super.onStartCommand(intent, flags, startId)
    when (intent.getIntExtra(KEY_TYPE, 0)) {
      TYPE_CLOSE -> {
        if (bean != null) {
          bean?.state = GSYVideoView.CURRENT_STATE_NORMAL
          AudioPlayer.audioStateLiveData.value = bean
        }
        stopSelf()
      }
      TYPE_TOGGLE -> {
        bean?.also {
          when (it.state) {
            GSYVideoView.CURRENT_STATE_PLAYING -> mediaPlayer.onVideoPause()
            GSYVideoView.CURRENT_STATE_PAUSE -> mediaPlayer.onVideoResume()
            else -> mediaPlayer.startPlayLogic()
          }
        }
      }
      TYPE_DATA -> intent.getParcelableExtra<AudioBean>(KEY_DATA)?.also { bean ->
        this.bean = bean
        mediaPlayer.setUp(bean.url, true, "")
        mediaPlayer.startPlayLogic()
        handleNotification(bean)
        /*GlobalScope.takeUnless { bean.img.isEmpty() }?.launch {
            (imageAdapter.loadImage(applicationContext, bean.img) as? BitmapDrawable)?.also {
                bean.bitmap = it.bitmap
                bean.state = GSYVideoView.CURRENT_STATE_PLAYING
                handleNotification(bean)
            }
        }*/
      }
    }
    return result
  }

  override fun onDestroy() {
    super.onDestroy()
    bean = null
    mediaPlayer.onDestroy()
    mediaPlayer.release()
  }

  private fun handleNotification(bean: AudioBean?) {
    val clz = AudioService::class.java
    val manager = getSystemService(NotificationManager::class.java)
    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      manager.createNotificationChannel(
        NotificationChannel(clz.name, clz.simpleName, NotificationManager.IMPORTANCE_HIGH)
      )
      Notification.Builder(this, clz.name)
    } else {
      Notification.Builder(this)
    }
    //val audioRemote: AudioRemote = EntryPoints.get(applicationContext, MediaInterface::class.java).getAudioRemote()
    //val views = RemoteViews(packageName, audioRemote.layout)
    //audioRemote.convert(views, bean)
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        builder.setCustomContentView(views)
    } else {
        builder.setContent(views)
    }*/
    builder.setSmallIcon(applicationInfo.icon).build().also {
      it.flags = Notification.FLAG_ONGOING_EVENT
      startForeground(-1001, it)
    }
  }

  companion object {
    const val KEY_TYPE = "type"
    const val KEY_DATA = "data"
    const val TYPE_DATA = 1
    const val TYPE_CLOSE = 2
    const val TYPE_TOGGLE = 3
  }
}