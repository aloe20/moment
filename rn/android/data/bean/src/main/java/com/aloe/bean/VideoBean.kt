package com.aloe.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoBean(@Json(name = "video_id") val videoId: String, val title: String, val abstract: String)