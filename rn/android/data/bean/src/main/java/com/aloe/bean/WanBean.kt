package com.aloe.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WanBean(
  val author: String? = null,
  val chapterId: Int = 0,
  val chapterName: String = "",
  val desc: String? = null,
  val envelopePic: String? = null,
  val fresh: Boolean = false,
  val id: Int,
  val link: String? = null,
  val niceDate: String? = null,
  val niceShareDate: String? = null,
  val projectLink: String? = null,
  val shareUser: String? = null,
  val superChapterName: String? = null,
  val tags: List<WebBean>? = null,
  var title: String? = null
)
