package com.aloe.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NaviBean(val cid: Int = 0, val name: String = "", val articles: List<NaviDetailBean> = emptyList()) {
  @JsonClass(generateAdapter = true)
  data class NaviDetailBean(
    val id: Int = 0,
    val title: String = "",
    val link: String = "",
    val author: String = "",
    val niceDate: String = ""
  )
}