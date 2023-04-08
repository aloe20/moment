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

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class StickyItemDecoration(private val stickyItem: Int) : RecyclerView.ItemDecoration() {
    private var stickyItemMarginTop = 0
    private var bindDataIndex = -1
    private var stickyViewType = -1
    private var prevStickyItemHeight = DEFAULT_STICKY_ITEM_HEIGHT
    private val stickyIndexList = mutableListOf<Int>()
    private val mapStickyViewHolder = mutableMapOf<Int, RecyclerView.ViewHolder>()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter
        val layoutManager = parent.layoutManager
        if (adapter == null || adapter.itemCount < 1 || layoutManager !is LinearLayoutManager) return
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        stickyIndexList.takeIf { firstVisiblePosition == 0 }?.clear()
        var shouldSticky = false
        var index = 0
        while (index <= firstVisiblePosition && !shouldSticky) {
            shouldSticky = adapter.getItemViewType(index++).and(stickyItem) != 0
        }
        val currUiFindStickyView = drawSticky(c, parent, shouldSticky)
        if (currUiFindStickyView) {
            stickyItemMarginTop = 0
            if (firstVisiblePosition + parent.childCount == adapter.itemCount && stickyIndexList.isNotEmpty()) {
                bindDataForStickyView(
                    adapter,
                    stickyIndexList[stickyIndexList.size - 1],
                    parent.measuredWidth,
                )
            }
            mapStickyViewHolder[stickyViewType].takeIf { shouldSticky }
                ?.also { drawStickyItemView(it, c) }
        }
    }

    private fun shouldSticky(
        shouldSticky: Boolean,
        adapter: RecyclerView.Adapter<*>,
        index: Int,
    ): Boolean {
        return shouldSticky && adapter.getItemViewType(index).and(stickyItem) != 0
    }

    private fun drawSticky(c: Canvas, parent: RecyclerView, shouldSticky: Boolean): Boolean {
        var index: Int
        val adapter = parent.adapter!!
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        var currUiFindStickyView = false
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            index = layoutManager.getPosition(view)
            if (shouldSticky(shouldSticky, adapter, index)) {
                currUiFindStickyView = true
                if (prevStickyItemHeight == DEFAULT_STICKY_ITEM_HEIGHT || i == 0) {
                    prevStickyItemHeight = view.height
                    stickyViewType = adapter.getItemViewType(index)
                } else if (prevStickyItemHeight == view.height) {
                    stickyViewType = adapter.getItemViewType(index)
                }
                if (mapStickyViewHolder[stickyViewType] == null) {
                    mapStickyViewHolder[stickyViewType] =
                        adapter.onCreateViewHolder(parent, stickyViewType)
                }
                val position = firstVisiblePosition + i
                stickyIndexList.takeUnless { stickyIndexList.contains(position) }?.add(position)
                bindDataForStickyView(parent, view, firstVisiblePosition, position)
                mapStickyViewHolder[stickyViewType]?.also { vh ->
                    stickyItemMarginTop = getMarginTop(view, vh)
                    if (stickyItemMarginTop == 0) {
                        getNextStickyView(adapter, layoutManager, parent)
                            ?.takeIf { it.top <= vh.itemView.height }
                            ?.also {
                                stickyItemMarginTop = vh.itemView.height - it.top
                            }
                    }
                    drawStickyItemView(vh, c)
                }
                break
            }
        }
        return currUiFindStickyView
    }

    private fun bindDataForStickyView(
        parent: RecyclerView,
        view: View,
        firstVisiblePosition: Int,
        position: Int,
    ) {
        val adapter = parent.adapter!!
        if (view.top <= 0) {
            bindDataForStickyView(adapter, firstVisiblePosition, parent.measuredWidth)
        } else if (stickyIndexList.isNotEmpty()) {
            if (stickyIndexList.size == 1) {
                bindDataForStickyView(adapter, stickyIndexList[0], parent.measuredWidth)
            } else {
                val indexOfCurrPosition = stickyIndexList.lastIndexOf(position)
                takeIf { indexOfCurrPosition >= 1 }
                    ?.bindDataForStickyView(
                        adapter,
                        stickyIndexList[indexOfCurrPosition - 1],
                        parent.measuredWidth,
                    )
            }
        }
    }

    private fun getMarginTop(view: View, vh: ViewHolder): Int {
        return if (view.top > 0 && view.top <= vh.itemView.height) {
            vh.itemView.height - view.top
        } else {
            0
        }
    }

    fun bindDataForStickyView(
        adapter: RecyclerView.Adapter<in RecyclerView.ViewHolder>,
        index: Int,
        width: Int,
    ) {
        val viewHolder = mapStickyViewHolder[adapter.getItemViewType(index)] ?: return
        bindDataIndex = index
        adapter.onBindViewHolder(viewHolder, bindDataIndex)
        if (!viewHolder.itemView.isLayoutRequested) return
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val layoutParams = viewHolder.itemView.layoutParams
        val heightSpec =
            if (layoutParams.height > 0) {
                View.MeasureSpec.makeMeasureSpec(
                    layoutParams.height,
                    View.MeasureSpec.EXACTLY,
                )
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }
        viewHolder.itemView.measure(widthSpec, heightSpec)
        viewHolder.itemView.layout(
            0,
            0,
            viewHolder.itemView.measuredWidth,
            viewHolder.itemView.measuredHeight,
        )
    }

    private fun getNextStickyView(
        adapter: RecyclerView.Adapter<*>,
        layoutManager: LinearLayoutManager,
        recyclerView: RecyclerView,
    ): View? {
        var index = 0
        var nextStickyView: View? = null
        for (i in 0 until recyclerView.childCount) {
            val view = recyclerView.getChildAt(i)
            if (adapter.getItemViewType(layoutManager.getPosition(view)).and(stickyItem) != 0) {
                nextStickyView = view
                index++
            }
            if (index == 2) {
                break
            }
        }
        return if (index > 1) nextStickyView else null
    }

    private fun drawStickyItemView(viewHolder: RecyclerView.ViewHolder, canvas: Canvas) {
        val count = canvas.save()
        canvas.translate(0F, -stickyItemMarginTop.toFloat())
        viewHolder.itemView.draw(canvas)
        canvas.restoreToCount(count)
    }

    // fun clear() = mapStickyViewHolder.clear()

    companion object {
        private const val DEFAULT_STICKY_ITEM_HEIGHT = -3
    }
}
