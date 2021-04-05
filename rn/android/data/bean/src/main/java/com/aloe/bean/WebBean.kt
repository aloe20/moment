package com.aloe.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WebBean(val name: String? = null, val url: String? = null, val github: String? = null) : Parcelable
