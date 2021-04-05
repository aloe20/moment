package com.aloe.banner

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.min

class Banner @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  FrameLayout(context, attrs, defStyleAttr, 0), Runnable {
  private val viewPager2 = ViewPager2(context)
  private var indicator: LinearLayout? = null
  private var indicatorSelectDrawable: Drawable
  private var indicatorNormalDrawable: Drawable? = null
  private var indicatorSize: Int
  private var indicatorMargin: Int
  var loop: Boolean
  var itemLayout: Int
  private var duration: Int
  private var playing = false
  private var autoStart: Boolean
  private var touchStatus = false

  init {
    addView(viewPager2)
    val size = resources.getDimensionPixelSize(R.dimen.defaultMargin)
    val a = context.obtainStyledAttributes(attrs, R.styleable.Banner, defStyleAttr, 0)
    itemLayout = a.getResourceId(R.styleable.Banner_bannerLayout, 0)
    loop = a.getBoolean(R.styleable.Banner_loop, false)
    autoStart = a.getBoolean(R.styleable.Banner_autoStart, false)
    duration = a.getInt(R.styleable.Banner_android_duration, DURATION)
    indicatorSize =
      a.getDimensionPixelSize(R.styleable.Banner_indicatorSize, LinearLayout.LayoutParams.WRAP_CONTENT)
    indicatorMargin = a.getDimensionPixelSize(R.styleable.Banner_indicatorMargin, size)
    indicatorSelectDrawable = if (a.hasValue(R.styleable.Banner_indicatorSelectDrawable)) {
      a.getDrawable(R.styleable.Banner_indicatorSelectDrawable) ?: ColorDrawable(Color.RED)
    } else {
      createDrawable(a.getColor(R.styleable.Banner_indicatorSelectColor, Color.GRAY))
    }
    indicatorNormalDrawable = if (a.hasValue(R.styleable.Banner_indicatorNormalDrawable)) {
      a.getDrawable(R.styleable.Banner_indicatorNormalDrawable) ?: ColorDrawable(Color.TRANSPARENT)
    } else {
      createDrawable(a.getColor(R.styleable.Banner_indicatorNormalColor, Color.WHITE))
    }
    viewPager2.orientation = a.getInt(R.styleable.Banner_android_orientation, ViewPager2.ORIENTATION_HORIZONTAL)
    a.recycle()
    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        indicator?.also {
          val count = min(it.childCount, (viewPager2.adapter as BannerAdapter<*>).getDataSize())
          for (i in 0 until count) {
            it.getChildAt(i).background = if (position % count == i) indicatorSelectDrawable
            else indicatorNormalDrawable
          }
        }
      }
    })
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    indicator = findViewById(R.id.bannerIndicator)
    if (isInEditMode) {
      createIndicator(COUNT)
      if (itemLayout != 0) {
        viewPager2.adapter = BannerAdapter<String>(itemLayout, false, null).apply {
          setData(mutableListOf(""))
        }
      }
    }
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        if (playing) {
          stop()
          touchStatus = true
        }
      }
      MotionEvent.ACTION_UP -> {
        if (touchStatus) {
          touchStatus = false
          start()
        }
      }
    }
    return super.dispatchTouchEvent(ev)
  }

  private fun createDrawable(color: Int): Drawable {
    val drawable = ShapeDrawable(OvalShape())
    drawable.paint.color = color
    return drawable
  }

  fun setBannerLayout(@LayoutRes layout: Int) = apply {
    itemLayout = layout
  }

  fun setAutoStart(autoStart: Boolean) = apply {
    this.autoStart = autoStart
  }

  fun setDuration(duration: Int) = apply {
    this.duration = duration
  }

  fun setIndicatorSelectDrawable(drawable: Drawable) = apply {
    this.indicatorSelectDrawable = drawable
  }

  fun setIndicatorNormalDrawable(drawable: Drawable) = apply {
    this.indicatorNormalDrawable = drawable
  }

  override fun run() {
    val count = viewPager2.adapter?.itemCount ?: 0
    if (playing && viewPager2.currentItem < count - 1) {
      viewPager2.setCurrentItem(++viewPager2.currentItem, true)
      postDelayed(this, duration.toLong())
    }
  }

  override fun onVisibilityAggregated(isVisible: Boolean) {
    super.onVisibilityAggregated(isVisible)
    if (autoStart && isVisible) start()
    else stop()
  }

  fun start() {
    if (!playing) {
      playing = true
      postDelayed(this, duration.toLong())
    }
  }

  fun stop() {
    playing = false
    removeCallbacks(this)
  }

  private fun createIndicator(count: Int) {
    indicator?.also {
      it.removeAllViews()
      for (i in 0 until count) {
        val view = ImageView(context)
        val params = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
        if (i > 0) {
          view.background = indicatorNormalDrawable
          params.marginStart = indicatorMargin
        } else {
          view.background = indicatorSelectDrawable
        }
        it.addView(view, params)
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun <T> getItem(index: Int): T = (viewPager2.adapter as BannerAdapter<T>).getData(index)

  fun setAdapter(adapter: BannerAdapter<*>, click: ((View, Int) -> Unit)? = null) {
    adapter.setOnItemClickListener(click)
    viewPager2.adapter = adapter
  }

  @Suppress("UNCHECKED_CAST")
  fun <T> setData(data: Collection<T>?) {
    if (data == null) return
    createIndicator(data.size)
    viewPager2.adapter?.also {
      (it as? BannerAdapter<T>)?.setData(data)
      if (data.isEmpty()) {
        stop()
      } else {
        if (loop) {
          val count = it.itemCount.shr(1)
          viewPager2.setCurrentItem(count - count % data.size, false)
        }
      }
    }
  }

  companion object {
    private const val DURATION = 3000
    private const val COUNT = 5
  }
}
