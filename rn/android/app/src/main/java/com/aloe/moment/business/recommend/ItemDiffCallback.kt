package com.aloe.moment.business.recommend

import androidx.recyclerview.widget.DiffUtil
import com.aloe.bean.WanBean

class ItemDiffCallback : DiffUtil.ItemCallback<WanBean>() {
  override fun areItemsTheSame(oldItem: WanBean, newItem: WanBean): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: WanBean, newItem: WanBean): Boolean = oldItem.author == newItem.author &&
      oldItem.title == newItem.title && oldItem.desc == newItem.desc
}