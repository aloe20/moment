/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.aloe.basic

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.Gravity
import kotlin.math.max
import kotlin.math.min

class BubbleDrawable : Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var radius = 0F
    private val triPath = Path()
    private var triGravity = Gravity.NO_GRAVITY
    private val triRect = RectF()
    private val bodyRect = RectF()
    private var triBase = 0F
    private var triHeight = 0F
    private var triLimit = 0F
    private var isReverse = false
    private var startColor = Color.TRANSPARENT
    private var endColor = Color.TRANSPARENT
    private var isVertical = false

    fun setStyle(style: Paint.Style) = apply { paint.style = style }
    fun setStrokeWidth(width: Float) = apply { paint.strokeWidth = width }
    fun setStokeColor(color: Int) = apply { paint.color = color }
    fun setColor(startColor: Int = -1, endColor: Int = -1, isVertical: Boolean = false) = apply {
        this.isVertical = isVertical
        if (startColor != -1) {
            this.startColor = startColor
            if (endColor == -1) {
                this.endColor = startColor
            }
        }
        if (endColor != -1) {
            this.endColor = endColor
            if (startColor == -1) {
                this.startColor = endColor
            }
        }
    }

    fun setRadius(radius: Float) = apply { this.radius = radius }
    fun setTriGravity(gravity: Int) = apply { triGravity = gravity }
    fun setTriBase(base: Float) = apply { triBase = base }
    fun setTriHeight(height: Float) = apply { triHeight = height }
    fun setTriLimit(limit: Float) = apply {
        triLimit = limit
        isReverse = limit == 0F && limit.toString().first() == '-'
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        triRect.setEmpty()
        val halfStroke = paint.strokeWidth / 2
        paint.shader = LinearGradient(
            0F,
            0F,
            if (isVertical) 0F else bounds.right.toFloat(),
            if (isVertical) bounds.bottom.toFloat() else 0F,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
        if (triBase == 0F && triHeight != 0F) {
            if (triGravity.and(Gravity.START) == Gravity.START) {
                bodyRect.set(halfStroke, halfStroke, triHeight, bounds.bottom - halfStroke)
            } else if (triGravity.and(Gravity.TOP) == Gravity.TOP) {
                bodyRect.set(halfStroke, halfStroke, bounds.right - halfStroke, triHeight)
            } else if (triGravity.and(Gravity.END) == Gravity.END) {
                bodyRect.set(
                    bounds.right - triHeight,
                    halfStroke,
                    bounds.right - halfStroke,
                    bounds.bottom - halfStroke
                )
            } else if (triGravity.and(Gravity.BOTTOM) == Gravity.BOTTOM) {
                bodyRect.set(
                    halfStroke,
                    bounds.bottom - triHeight,
                    bounds.right - halfStroke,
                    bounds.bottom - halfStroke
                )
            }
        } else {
            if (radius < 0) {
                radius = min(bounds.width(), bounds.height()) / 2F
            }
            if (triGravity.and(Gravity.START) == Gravity.START) {
                val start = if (triGravity.and(Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
                    bounds.height() / 2F
                } else if (triLimit < 0) {
                    bounds.bottom + triLimit
                } else if (isReverse) {
                    bounds.bottom.toFloat()
                } else {
                    triLimit
                }
                triRect.set(halfStroke, start - triBase / 2, triHeight, start + triBase / 2)
                bodyRect.set(triRect.right, halfStroke, bounds.right - halfStroke, bounds.bottom - halfStroke)
            } else if (triGravity.and(Gravity.TOP) == Gravity.TOP) {
                val start = if (triGravity.and(Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
                    bounds.width() / 2F
                } else if (triLimit < 0) {
                    bounds.right + triLimit
                } else if (isReverse) {
                    bounds.right.toFloat()
                } else {
                    triLimit
                }
                triRect.set(start - triBase / 2, halfStroke, start + triBase / 2, triHeight)
                bodyRect.set(halfStroke, triRect.bottom, bounds.right - halfStroke, bounds.bottom - halfStroke)
            } else if (triGravity.and(Gravity.END) == Gravity.END) {
                val start = if (triGravity.and(Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
                    bounds.height() / 2F
                } else if (triLimit < 0) {
                    bounds.bottom + triLimit
                } else if (isReverse) {
                    bounds.bottom.toFloat()
                } else {
                    triLimit
                }
                triRect.set(
                    bounds.right - triHeight,
                    start - triBase / 2,
                    bounds.right - halfStroke,
                    start + triBase / 2
                )
                bodyRect.set(halfStroke, halfStroke, triRect.left, bounds.bottom - halfStroke)
            } else if (triGravity.and(Gravity.BOTTOM) == Gravity.BOTTOM) {
                val start = if (triGravity.and(Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
                    bounds.width() / 2F
                } else if (triLimit < 0) {
                    bounds.right + triLimit
                } else if (isReverse) {
                    bounds.right.toFloat()
                } else {
                    triLimit
                }
                triRect.set(
                    start - triBase / 2,
                    bounds.bottom - triHeight,
                    start + triBase / 2,
                    bounds.bottom - halfStroke
                )
                bodyRect.set(halfStroke, halfStroke, bounds.right - halfStroke, triRect.top)
            } else {
                bodyRect.set(halfStroke, halfStroke, bounds.right - halfStroke, bounds.bottom - halfStroke)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        path.reset()
        val style = paint.style
        if (style == Paint.Style.FILL_AND_STROKE) {
            paint.style = Paint.Style.FILL
            drawPath(canvas)
            path.reset()
            paint.style = Paint.Style.STROKE
            val shader = paint.shader
            paint.shader = null
            drawPath(canvas)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.shader = shader
        } else {
            drawPath(canvas)
        }
    }

    private fun drawPath(canvas: Canvas) {
        val rect = bounds
        val halfStroke = paint.strokeWidth / 2
        if (triGravity.and(Gravity.START) == Gravity.START) {
            if (triRect.top < rect.top) {
                triPath.moveTo(triRect.left, halfStroke)
                triPath.lineTo(triRect.right + radius, halfStroke)
                triPath.lineTo(triRect.right, max(triRect.bottom, radius + halfStroke))
            } else if (triRect.bottom > rect.bottom) {
                triPath.moveTo(triRect.left, rect.bottom - halfStroke)
                triPath.lineTo(triRect.right + radius, rect.bottom - halfStroke)
                triPath.lineTo(triRect.right, min(triRect.top, rect.bottom - radius - halfStroke))
            } else {
                triPath.moveTo(triRect.left, (triRect.top + triRect.bottom) / 2)
                triPath.lineTo(triRect.right, triRect.top)
                triPath.lineTo(triRect.right, triRect.bottom)
            }
        } else if (triGravity.and(Gravity.TOP) == Gravity.TOP) {
            if (triRect.left < rect.left) {
                triPath.moveTo(halfStroke, triRect.top)
                triPath.lineTo(halfStroke, triRect.bottom + radius)
                triPath.lineTo(max(triRect.right, radius + halfStroke), triRect.bottom)
            } else if (triRect.right > rect.right) {
                triPath.moveTo(rect.right - halfStroke, triRect.top)
                triPath.lineTo(rect.right - halfStroke, triRect.bottom + radius)
                triPath.lineTo(min(triRect.left, rect.right - radius - halfStroke), triRect.bottom)
            } else {
                triPath.moveTo((triRect.left + triRect.right) / 2, triRect.top)
                triPath.lineTo(triRect.left, triRect.bottom)
                triPath.lineTo(triRect.right, triRect.bottom)
            }
        } else if (triGravity.and(Gravity.END) == Gravity.END) {
            if (triRect.top < rect.top) {
                triPath.moveTo(triRect.right, halfStroke)
                triPath.lineTo(triRect.left - radius, halfStroke)
                triPath.lineTo(triRect.left, max(triRect.bottom, radius + halfStroke))
            } else if (triRect.bottom > rect.bottom) {
                triPath.moveTo(triRect.right, rect.bottom - halfStroke)
                triPath.lineTo(triRect.left - radius, rect.bottom - halfStroke)
                triPath.lineTo(triRect.left, min(triRect.top, rect.bottom - radius - halfStroke))
            } else {
                triPath.moveTo(triRect.right, (triRect.top + triRect.bottom) / 2)
                triPath.lineTo(triRect.left, triRect.top)
                triPath.lineTo(triRect.left, triRect.bottom)
            }
        } else if (triGravity.and(Gravity.BOTTOM) == Gravity.BOTTOM) {
            if (triRect.left < rect.left) {
                triPath.moveTo(halfStroke, triRect.bottom)
                triPath.lineTo(halfStroke, triRect.top - radius)
                triPath.lineTo(max(triRect.right, radius + halfStroke), triRect.top)
            } else if (triRect.right > rect.right) {
                triPath.moveTo(rect.right - halfStroke, triRect.bottom)
                triPath.lineTo(rect.right - halfStroke, triRect.top - radius)
                triPath.lineTo(min(triRect.left, rect.right - radius - halfStroke), triRect.top)
            } else {
                triPath.moveTo((triRect.left + triRect.right) / 2, triRect.bottom)
                triPath.lineTo(triRect.left, triRect.top)
                triPath.lineTo(triRect.right, triRect.top)
            }
        }
        triPath.close()
        path.addRoundRect(bodyRect, radius, radius, Path.Direction.CW)
        path.op(triPath, Path.Op.UNION)
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        if (alpha != paint.alpha) {
            paint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}