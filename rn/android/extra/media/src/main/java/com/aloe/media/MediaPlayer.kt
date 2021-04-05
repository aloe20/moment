package com.aloe.media

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.shuyu.gsyvideoplayer.listener.GSYStateUiListener
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge

class MediaPlayer : StandardGSYVideoPlayer, LifecycleObserver {
  private var thumbImageUrl = ""
  private var orientationUtils: OrientationUtils? = null

  constructor(context: Context) : this(context, null)
  constructor(context: Context, fullFlag: Boolean) : super(context, fullFlag)
  constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

  init {
    playTag = MediaPlayer::class.java.simpleName
  }

  override fun init(context: Context) {
    super.init(context)
    isShowFullAnimation = false
    if (context is Activity) {
      orientationUtils = OrientationUtils(context, this)
    }
    orientationUtils?.isEnable = false
    mFullscreenButton?.setOnClickListener(this)
    mBackButton?.visibility = View.GONE
    if (mThumbImageViewLayout != null) {
      thumbImageView = ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
    }
  }

  override fun setGSYStateUiListener(gsyStateUiListener: GSYStateUiListener?) {
    super.setGSYStateUiListener(gsyStateUiListener)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    findViewTreeLifecycleOwner()?.lifecycle?.addObserver(this)
  }

  override fun showSmallVideo(size: Point?, actionBar: Boolean, statusBar: Boolean): GSYBaseVideoPlayer {
    val player = super.showSmallVideo(size, actionBar, statusBar)
    player.backButton?.visibility = View.GONE
    return player
  }

  override fun getGSYVideoManager(): GSYVideoViewBridge {
    val manager = MediaManager.getMediaManager(getKey())
    manager.initContext(context.applicationContext)
    return manager
  }

  override fun backFromFull(context: Context): Boolean {
    return MediaManager.backFromWindowFull(context, getKey())
  }

  override fun getFullId(): Int {
    return R.id.custom_full_id
  }

  override fun getSmallId(): Int {
    return R.id.custom_small_id
  }

  override fun releaseVideos() {
    MediaManager.releaseAllVideo(getKey())
  }

  /*override fun onLossTransientAudio() {
      //super.onLossTransientAudio()
  }*/

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onResume() {
    onVideoResume()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun onPause() {
    onVideoPause()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    orientationUtils?.releaseListener()
  }

  public override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(context as Activity, newConfig, orientationUtils)
  }

  override fun onClick(v: View) {
    if (v == mFullscreenButton) {
      orientationUtils?.also {
        it.isLand = 0
        it.resolveByClick()
      }
    } else {
      super.onClick(v)
    }
  }

  fun releaseListener() {
    orientationUtils?.releaseListener()
  }

  private fun getKey(): String = MediaPlayer::class.java.simpleName + mPlayPosition + mPlayTag
}