package com.aloe.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WanBannerBean(
  val desc: String = "",
  val id: Int = 0,
  val imagePath: String = "",
  val isVisible: Int = 0,
  val order: Int = 0,
  val title: String = "",
  val type: Int = 0,
  val url: String = ""
)
