package com.aloe.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BannerAdapter<T>(private val layout: Int, private val loop: Boolean, private val block: ((View, Int) -> Unit)?) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val data = mutableListOf<T>()
  private var onItemClick: ((View, Int) -> Unit)? = null
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val item = LayoutInflater.from(parent.context).inflate(layout, parent, false)
    val holder = object : RecyclerView.ViewHolder(item) {}
    onItemClick?.also {
      item.setOnClickListener { view ->
        val position = holder.absoluteAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
          it.invoke(view, position % data.size)
        }
      }
    }
    return holder
  }

  override fun getItemCount(): Int = when {
    data.isEmpty() -> 0
    loop -> Int.MAX_VALUE
    else -> data.size
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    block?.invoke(holder.itemView, if (loop) position % getDataSize() else position)
  }

  internal fun getDataSize() = data.size

  fun setData(data: Collection<T>) {
    this.data.clear()
    this.data.addAll(data)
  }

  internal fun getData(index: Int) = data[index]

  internal fun setOnItemClickListener(block: ((View, Int) -> Unit)?) {
    this.onItemClick = block
  }
}
