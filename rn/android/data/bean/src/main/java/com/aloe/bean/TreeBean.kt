package com.aloe.bean

import android.os.Parcelable
import androidx.room.Relation
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TreeBean(
  @Relation(
    parentColumn = "classify_id",
    entityColumn = "parent_id"
  ) val children: List<TreeChildBean> = emptyList()
) : TreeClassifyBean(), Parcelable