package com.aloe.media.audio

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioBean(val url: String, val img: String, var state: Int = 0, var bitmap: Bitmap? = null) : Parcelable