package com.aloe.moment.business.recommend

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aloe.bean.WanBean
import com.aloe.bean.WebBean
import com.aloe.moment.R
import com.aloe.moment.databinding.ItemRecommendBinding
import com.google.android.material.chip.Chip

abstract class ItemDelegate {
  private val chips = mutableListOf<Chip>()
  private var drawable: Drawable? = null
  fun onCreateViewHolder(parent: ViewGroup, tagTopVisible: Int): RecyclerView.ViewHolder {
    if (drawable == null) {
      ContextCompat.getDrawable(parent.context, R.drawable.ic_user)?.mutate()?.also {
        it.setBounds(0, 0, it.intrinsicWidth.shr(1), it.intrinsicHeight.shr(1))
        drawable = it
      }
    }
    val binding = ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    binding.root.setTag(R.id.item_binding, binding)
    binding.tagTop.visibility = tagTopVisible
    binding.tvAuthor.setCompoundDrawablesRelative(drawable, null, null, null)
    val holder = object : RecyclerView.ViewHolder(binding.root) {}
    holder.itemView.setOnClickListener {
      val position = holder.bindingAdapterPosition
      if (position != RecyclerView.NO_POSITION) {
        getItem(position)?.also { bean ->
          it.findNavController().navigate(
            R.id.frag_web,
            bundleOf("data" to WebBean(bean.title, bean.link))
          )
        }
      }
    }
    return holder
  }

  abstract fun getItem(index: Int): WanBean?

  @SuppressLint("SetTextI18n")
  fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: WanBean) {
    with(holder.itemView.getTag(R.id.item_binding) as ItemRecommendBinding) {
      tvTitle.text = HtmlCompat.fromHtml(item.title ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
      tvDesc.text = HtmlCompat.fromHtml(item.desc ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
      tvAuthor.text = if (item.author.isNullOrEmpty()) item.shareUser else item.author
      chipClassify.text = "${item.superChapterName}/${item.chapterName}"
      tvDate.text = if (item.niceDate.isNullOrEmpty()) item.niceShareDate else item.niceDate
      tagNew.visibility = if (item.fresh) View.VISIBLE else View.GONE
      tvDesc.visibility = if (TextUtils.isEmpty(item.desc)) View.GONE else View.VISIBLE
      if (item.tags.isNullOrEmpty()) {
        while (chipGroup.childCount > 0) {
          val chip = chipGroup.getChildAt(0) as Chip
          chipGroup.removeView(chip)
          chips.add(chip)
        }
        chipGroup.visibility = View.GONE
      } else {
        while (chipGroup.childCount > 0) {
          val chip = chipGroup.getChildAt(0) as Chip
          chipGroup.removeView(chip)
          chips.add(chip)
        }
        item.tags?.forEach {
          val chip = if (chips.isNotEmpty()) {
            chips.removeAt(0)
          } else {
            LayoutInflater.from(holder.itemView.context)
              .inflate(R.layout.item_child_chip, holder.itemView as ViewGroup, false) as Chip
          }
          chip.text = it.name
          chipGroup.addView(chip)
        }
        chipGroup.visibility = View.VISIBLE
      }
    }
  }
}