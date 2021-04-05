package com.aloe.moment.basic

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aloe.moment.R

class BasicItemDecoration(val recyclerView: RecyclerView) :
  DividerItemDecoration(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation) {
  init {
    ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_divider)?.also { setDrawable(it) }
  }
}