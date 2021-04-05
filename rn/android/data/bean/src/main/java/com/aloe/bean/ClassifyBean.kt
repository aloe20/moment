package com.aloe.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ClassifyBean(val id: Int = 0, val name: String? = "", val keyword: String? = null) : Parcelable
