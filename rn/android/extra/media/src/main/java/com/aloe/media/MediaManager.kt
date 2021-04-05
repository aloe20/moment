package com.aloe.media

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.shuyu.gsyvideoplayer.GSYVideoBaseManager
import com.shuyu.gsyvideoplayer.player.IPlayerManager
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class MediaManager : GSYVideoBaseManager() {
  init {
    init()
  }

  override fun getPlayManager(): IPlayerManager = Exo2PlayerManager()

  fun onResume(key: String) {
    getMediaManager(key).listener()?.onVideoResume()
  }

  fun onResume(key: String, seek: Boolean) {
    getMediaManager(key).listener()?.onVideoResume(seek)
  }

  fun onPause(key: String) {
    getMediaManager(key).listener()?.onVideoPause()
  }

  companion object {
    private val map = mutableMapOf<String, MediaManager?>()

    @Synchronized
    fun getMediaManager(key: String): MediaManager {
      val manager = map[key]
      if (manager == null) {
        val tmp = MediaManager()
        map[key] = tmp
        return tmp
      }
      return manager
    }

    fun backFromWindowFull(context: Context, key: String): Boolean {
      var backFrom = false
      val vp = CommonUtil.scanForActivity(context).findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
      vp.findViewById<View?>(R.id.custom_full_id)?.also {
        backFrom = true
        CommonUtil.hideNavKey(context)
        getMediaManager(key).lastListener()?.onBackFullscreen()
      }
      return backFrom
    }

    fun onResumeAll() {
      map.forEach { it.value?.onResume(it.key) }
    }

    fun onResumeAll(seek: Boolean) {
      map.forEach { it.value?.onResume(it.key, seek) }
    }

    fun onPauseAll() {
      map.forEach { it.value?.onPause(it.key) }
    }

    fun releaseAllVideo(key: String) {
      getMediaManager(key).also {
        it.listener()?.onCompletion()
        it.releaseMediaPlayer()
      }
    }

    fun clearAllVideo() {
      map.forEach { releaseAllVideo(it.key) }
      map.clear()
    }
  }
}