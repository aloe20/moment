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

package com.aloe.excel

import android.graphics.Rect
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.floor

class TouchListener<T>(
    private val size: Rect,
    private val topData: MutableList<T>,
    private val scrollListener: ExcelScrollListener,
    private val excelClickListener: ((View, Int, Int) -> Unit)? = null,
) : RecyclerView.SimpleOnItemTouchListener() {
    private var detector: GestureDetectorCompat? = null
    override fun onInterceptTouchEvent(
        rv: RecyclerView,
        e: MotionEvent,
    ): Boolean = false.apply {
        if (detector == null) {
            detector = GestureDetectorCompat(
                rv.context,
                Listener(rv, size, topData, scrollListener, excelClickListener),
            )
        }
        detector?.onTouchEvent(e)
    }

    override fun onTouchEvent(
        rv: RecyclerView,
        e: MotionEvent,
    ) = Unit.apply { detector?.onTouchEvent(e) }

    companion object {
        private class Listener<T>(
            private val rv: RecyclerView,
            private val size: Rect,
            private val topData: MutableList<T>,
            private val scrollListener: ExcelScrollListener,
            private val excelClickListener: ((View, Int, Int) -> Unit)? = null,
        ) : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean = false.apply {
                rv.findChildViewUnder(e.x, e.y)?.also { v ->
                    val column =
                        floor((scrollListener.scrollx + e.x - size.left) / (size.right - size.left)).toInt()
                    when {
                        e.y > size.top -> rv.getChildViewHolder(v).absoluteAdapterPosition.also { index ->
                            excelClickListener?.takeUnless { index == RecyclerView.NO_POSITION }
                                ?.invoke(
                                    v,
                                    index - (if (topData.isEmpty()) 0 else 1),
                                    column,
                                )
                        }
                        e.x > size.left -> excelClickListener?.invoke(
                            v,
                            RecyclerView.NO_POSITION,
                            column,
                        )
                        else -> excelClickListener?.invoke(
                            v,
                            RecyclerView.NO_POSITION,
                            RecyclerView.NO_POSITION,
                        )
                    }
                }
            }

            override fun onDoubleTap(e: MotionEvent): Boolean = true

            override fun onDoubleTapEvent(e: MotionEvent): Boolean = true
        }
    }
}
