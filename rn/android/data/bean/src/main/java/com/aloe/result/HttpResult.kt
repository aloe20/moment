package com.aloe.result

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HttpResult<T>(val errorCode: Int? = 0, val data: T? = null, val datas: T? = null)
