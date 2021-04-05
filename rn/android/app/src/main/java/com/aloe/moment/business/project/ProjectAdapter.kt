package com.aloe.moment.business.project

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aloe.bean.WanBean
import com.aloe.bean.WebBean
import com.aloe.moment.R
import com.aloe.moment.business.recommend.ItemDiffCallback
import com.aloe.moment.databinding.ItemProjectBinding

class ProjectAdapter(private val showTag: Boolean = false) :
  PagingDataAdapter<WanBean, RecyclerView.ViewHolder>(ItemDiffCallback()), View.OnClickListener {
  private var drawable: Drawable? = null
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.also { bean ->
      with(holder.itemView.getTag(R.id.item_binding) as ItemProjectBinding) {
        tvTitle.text = HtmlCompat.fromHtml(bean.title ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
        tvDesc.text = HtmlCompat.fromHtml(bean.desc ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
        tvDate.text = if (bean.niceDate.isNullOrEmpty()) bean.niceShareDate else bean.niceDate
        tvAuthor.text = if (bean.author.isNullOrEmpty()) bean.shareUser else bean.author
        chipClassify.text = bean.chapterName
        image.load(bean.envelopePic)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    if (drawable == null) {
      ContextCompat.getDrawable(parent.context, R.drawable.ic_user)?.mutate()?.also {
        it.setBounds(0, 0, it.intrinsicWidth.shr(1), it.intrinsicHeight.shr(1))
        drawable = it
      }
    }
    return with(ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)) {
      tvAuthor.setCompoundDrawablesRelative(drawable, null, null, null)
      chipClassify.visibility = if (showTag) View.VISIBLE else View.GONE
      val holder = object : RecyclerView.ViewHolder(this.root) {}
      root.setTag(R.id.item_binding, this)
      root.setTag(R.id.item_holder, holder)
      root.setOnClickListener(this@ProjectAdapter)
      if (showTag) {
        chipClassify.visibility = View.VISIBLE
        chipClassify.setTag(R.id.item_holder, holder)
        chipClassify.setOnClickListener(this@ProjectAdapter)
        tvDesc.maxLines = 5
      } else {
        chipClassify.visibility = View.GONE
        tvDesc.maxLines = 6
      }
      holder
    }
  }

  override fun onClick(v: View) {
    (v.getTag(R.id.item_holder) as? RecyclerView.ViewHolder)?.also { viewHolder ->
      val position = viewHolder.bindingAdapterPosition
      if (position != RecyclerView.NO_POSITION) {
        getItem(position)?.also {
          val nav = v.findNavController()
          if (v == viewHolder.itemView) {
            nav.navigate(R.id.frag_web, bundleOf("data" to WebBean(it.title, it.link, it.projectLink)))
          } else if (v.id == R.id.chipClassify) {
            nav.navigate(R.id.frag_project_classify, bundleOf("cid" to it.chapterId))
          }
        }
      }
    }
  }
}