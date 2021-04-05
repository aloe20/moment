package com.aloe.moment.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.aloe.moment.R
import com.google.android.material.appbar.MaterialToolbar

class TitleToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  MaterialToolbar(context, attrs, defStyleAttr) {
  var canBack = true

  init {
    if (attrs != null) {
      val a = context.obtainStyledAttributes(attrs, R.styleable.TitleToolbar, defStyleAttr, 0)
      canBack = a.getBoolean(R.styleable.TitleToolbar_canBack, true)
      a.recycle()
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (canBack) {
      setNavigationIcon(R.drawable.ic_back)
      setNavigationOnClickListener { it.findNavController().navigateUp() }
    }
  }

  override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
    (params as? MarginLayoutParams)?.takeUnless { isInEditMode }?.also {
      val statusId = resources.getIdentifier("status_bar_height", "dimen", "android")
      if (statusId > 0) {
        it.topMargin = resources.getDimensionPixelSize(statusId)
      }
    }
    super.setLayoutParams(params)
  }
}