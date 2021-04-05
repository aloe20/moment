package com.aloe.image

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

class EasyImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  ShapeableImageView(context, attrs, defStyleAttr) {
  init {
    var topStart = 0F
    var topEnd = 0F
    var bottomStart = 0F
    var bottomEnd = 0F
    val a = context.obtainStyledAttributes(attrs, R.styleable.EasyImageView, defStyleAttr, 0)
    val cornerType = a.getInt(R.styleable.EasyImageView_cornerType, 0)
    if (a.hasValue(R.styleable.EasyImageView_allCorner)) {
      topStart = a.getDimension(R.styleable.EasyImageView_allCorner, 0F)
      topEnd = topStart
      bottomStart = topStart
      bottomEnd = topStart
    }
    if (a.hasValue(R.styleable.EasyImageView_startCorner)) {
      topStart = a.getDimension(R.styleable.EasyImageView_startCorner, 0F)
      bottomStart = topStart
    }
    if (a.hasValue(R.styleable.EasyImageView_topCorner)) {
      topStart = a.getDimension(R.styleable.EasyImageView_topCorner, 0F)
      topEnd = topStart
    }
    if (a.hasValue(R.styleable.EasyImageView_endCorner)) {
      topEnd = a.getDimension(R.styleable.EasyImageView_endCorner, 0F)
      bottomEnd = topEnd
    }
    if (a.hasValue(R.styleable.EasyImageView_bottomCorner)) {
      bottomStart = a.getDimension(R.styleable.EasyImageView_bottomCorner, 0F)
      bottomEnd = bottomStart
    }
    if (a.hasValue(R.styleable.EasyImageView_topStartCorner)) {
      topStart = a.getDimension(R.styleable.EasyImageView_topStartCorner, 0F)
    }
    if (a.hasValue(R.styleable.EasyImageView_topEndCorner)) {
      topEnd = a.getDimension(R.styleable.EasyImageView_topEndCorner, 0F)
    }
    if (a.hasValue(R.styleable.EasyImageView_bottomStartCorner)) {
      bottomStart = a.getDimension(R.styleable.EasyImageView_bottomStartCorner, 0F)
    }
    if (a.hasValue(R.styleable.EasyImageView_bottomEndCorner)) {
      bottomEnd = a.getDimension(R.styleable.EasyImageView_bottomEndCorner, 0F)
    }
    a.recycle()
    val size = RectF(topStart, topEnd, bottomEnd, bottomStart)
    setCorner(cornerType, size)
  }

  fun setCorner(cornerType: Int, size: RectF) {
    shapeAppearanceModel = ShapeAppearanceModel.Builder().apply {
      setCorner(this, CornerFamily.ROUNDED, cornerType.and(0xF), size)
      setCorner(this, CornerFamily.CUT, cornerType.shr(4), size)
    }.build()
  }

  private fun setCorner(builder: ShapeAppearanceModel.Builder, @CornerFamily family: Int, type: Int, size: RectF) {
    builder.apply {
      when (type) {
        1 -> setTopLeftCorner(family, size.left)
        2 -> setTopRightCorner(family, size.top)
        3 -> {
          setTopLeftCorner(family, size.left)
          setTopRightCorner(family, size.top)
        }
        4 -> setBottomLeftCorner(family, size.bottom)
        5 -> {
          setTopLeftCorner(family, size.left)
          setBottomLeftCorner(family, size.bottom)
        }
        6 -> {
          setTopRightCorner(family, size.top)
          setBottomLeftCorner(family, size.bottom)
        }
        7 -> {
          setTopLeftCorner(family, size.left)
          setTopRightCorner(family, size.top)
          setBottomLeftCorner(family, size.bottom)
        }
        8 -> setBottomRightCorner(family, size.right)
        9 -> {
          setTopLeftCorner(family, size.left)
          setBottomRightCorner(family, size.right)
        }
        10 -> {
          setTopRightCorner(family, size.top)
          setBottomRightCorner(family, size.right)
        }
        11 -> {
          setTopLeftCorner(family, size.left)
          setTopRightCorner(family, size.top)
          setBottomRightCorner(family, size.right)
        }
        12 -> {
          setBottomLeftCorner(family, size.bottom)
          setBottomRightCorner(family, size.right)
        }
        13 -> {
          setTopLeftCorner(family, size.left)
          setBottomLeftCorner(family, size.bottom)
          setBottomRightCorner(family, size.right)
        }
        14 -> {
          setTopRightCorner(family, size.top)
          setBottomLeftCorner(family, size.bottom)
          setBottomRightCorner(family, size.right)
        }
        15 -> setAllCorners(family, size.left)
      }
    }
  }
}