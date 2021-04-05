package com.aloe.media.audio

import android.widget.RemoteViews

abstract class AudioRemote(val layout: Int) {
  abstract fun convert(remoteViews: RemoteViews, bean: AudioBean?)
}