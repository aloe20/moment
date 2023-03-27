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

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ExcelScrollListener : RecyclerView.OnScrollListener(), View.OnTouchListener {
    private val views = mutableListOf<RecyclerView>()
    private var scrollRv: RecyclerView? = null
    private var listener: (() -> Unit)? = null
    var scrollx = 0

    fun setListener(block: () -> Unit) {
        this.listener = block
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val tmpView = v as? RecyclerView
        if (scrollRv !== v) {
            scrollRv?.also {
                it.stopScroll()
                it.removeOnScrollListener(this)
            }
            scrollRv = tmpView
            tmpView?.addOnScrollListener(this)
        }
        return false
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        scrollx += dx
        if (views.contains(recyclerView)) {
            views.forEach {
                if (it != recyclerView) {
                    it.scrollBy(dx, dy)
                    listener?.invoke()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addView(rv: RecyclerView) {
        rv.setOnTouchListener(this)
        views.add(rv)
    }

    fun contains(view: View): Boolean {
        return views.contains(view)
    }
}
