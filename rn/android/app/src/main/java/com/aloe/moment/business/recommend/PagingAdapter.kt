package com.aloe.moment.business.recommend

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aloe.bean.WanBean

class PagingAdapter : PagingDataAdapter<WanBean, RecyclerView.ViewHolder>(ItemDiffCallback()) {
  private val itemDelegate = object : ItemDelegate() {
    override fun getItem(index: Int): WanBean? = this@PagingAdapter.getItem(index)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.also { itemDelegate.onBindViewHolder(holder, it) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    itemDelegate.onCreateViewHolder(parent, View.GONE)
}