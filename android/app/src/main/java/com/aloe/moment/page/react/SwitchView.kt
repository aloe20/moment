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

package com.aloe.moment.page.react

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.util.Consumer
import com.aloe.moment.R
import kotlin.math.max
import kotlin.math.min

class SwitchView : View {
    private val defaultMargin = 8F
    var onCheckedChangeListener: Consumer<Boolean>? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var detector: GestureDetector
    private lateinit var pair: Pair<Float, Float>
    private val evaluator = ArgbEvaluator()
    private val point = PointF()
    private var hasScroll = false
    private var radius = 0F
    var thumbMargin = 8F
    var trackOffColor = Color.LTGRAY
    var trackOnColor = Color.RED
    var isCheck = false
        set(value) {
            field = value
            point.x = if (value) pair.second else pair.first
            changeState(false)
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle,
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwitchView, defStyle, 0)
        thumbMargin = a.getDimension(R.styleable.SwitchView_thumb, defaultMargin)
        trackOffColor = a.getColor(R.styleable.SwitchView_trackOff, Color.LTGRAY)
        trackOnColor = a.getColor(R.styleable.SwitchView_trackOn, Color.RED)
        paint.color = a.getColor(R.styleable.SwitchView_thumb, Color.WHITE)
        a.recycle()
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline) {
                outline.setRoundRect(0, 0, width, height, height / 2F)
            }
        }
        detector = GestureDetector(
            context,
            object : GestureDetector.SimpleOnGestureListener() {
                private var start = 0F
                override fun onDown(e: MotionEvent): Boolean {
                    hasScroll = false
                    start = point.x
                    return true
                }

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    point.x = if (point.x == pair.first) pair.second else pair.first
                    changeState(true)
                    return true
                }

                override fun onScroll(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    distanceX: Float,
                    distanceY: Float,
                ): Boolean {
                    hasScroll = true
                    point.x = start + e2.x - e1.x
                    point.x = if (point.x > width / 2F) {
                        min(point.x, pair.second)
                    } else {
                        max(
                            point.x,
                            pair.first,
                        )
                    }
                    updateBackground()
                    invalidate()
                    return true
                }
            },
            handler,
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        point.x = h / 2F
        point.y = h / 2F
        radius = point.y - thumbMargin
        pair = Pair(h / 2F, w - point.y)
        updateBackground()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && hasScroll) {
            point.x = if (point.x > width / 2F) pair.second else pair.first
            changeState(true)
        }
        return detector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(point.x, point.y, radius, paint)
    }

    private fun updateBackground() {
        setBackgroundColor(
            evaluator.evaluate(
                (point.x - pair.first) / (pair.second - pair.first),
                trackOffColor,
                trackOnColor,
            ) as Int,
        )
    }

    private fun changeState(onlyWidgetCallback: Boolean) {
        if (onlyWidgetCallback) {
            isCheck = point.x == pair.second
        }
        updateBackground()
        invalidate()
        onCheckedChangeListener.takeIf { onlyWidgetCallback }?.accept(isCheck)
    }
}
