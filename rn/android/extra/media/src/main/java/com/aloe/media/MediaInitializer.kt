package com.aloe.media

import android.content.Context
import androidx.startup.Initializer
import com.aloe.media.audio.AudioPlayer
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class MediaInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    AudioPlayer.init(context)
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}