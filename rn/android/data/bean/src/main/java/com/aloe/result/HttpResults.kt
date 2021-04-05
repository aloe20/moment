package com.aloe.result

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HttpResults<T>(val errorCode: Int? = 0, @Json(name = "datas") val data: T? = null)
