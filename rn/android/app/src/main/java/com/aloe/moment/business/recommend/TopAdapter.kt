package com.aloe.moment.business.recommend

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aloe.bean.WanBean

class TopAdapter : ListAdapter<WanBean, RecyclerView.ViewHolder>(ItemDiffCallback()) {
  private val itemDelegate = object : ItemDelegate() {
    override fun getItem(index: Int): WanBean? = this@TopAdapter.getItem(index)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    itemDelegate.onCreateViewHolder(parent, View.VISIBLE)

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
    itemDelegate.onBindViewHolder(holder, getItem(position))
}