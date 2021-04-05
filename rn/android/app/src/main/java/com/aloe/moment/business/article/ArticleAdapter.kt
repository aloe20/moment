package com.aloe.moment.business.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aloe.bean.WanBean
import com.aloe.bean.WebBean
import com.aloe.moment.R
import com.aloe.moment.business.recommend.ItemDiffCallback
import com.aloe.moment.databinding.ItemArticleBinding

class ArticleAdapter : PagingDataAdapter<WanBean, RecyclerView.ViewHolder>(ItemDiffCallback()), View.OnClickListener {
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.also { bean ->
      with(holder.itemView.getTag(R.id.item_binding) as ItemArticleBinding) {
        tvTitle.text = HtmlCompat.fromHtml(bean.title ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
        tvDate.text = if (bean.niceDate.isNullOrEmpty()) bean.niceShareDate else bean.niceDate
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    binding.root.setTag(R.id.item_binding, binding)
    val holder = object : RecyclerView.ViewHolder(binding.root) {}
    holder.itemView.setTag(R.id.item_holder, holder)
    holder.itemView.setOnClickListener(this)
    return holder
  }

  override fun onClick(v: View) {
    (v.getTag(R.id.item_holder) as? RecyclerView.ViewHolder)?.also { viewHolder ->
      val position = viewHolder.bindingAdapterPosition
      if (position != RecyclerView.NO_POSITION) {
        getItem(position)?.also {
          v.findNavController().navigate(R.id.frag_web, bundleOf("data" to WebBean(it.title, it.link)))
        }
      }
    }
  }
}