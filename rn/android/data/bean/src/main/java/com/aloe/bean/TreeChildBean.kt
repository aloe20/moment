package com.aloe.bean

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(
  tableName = "tree_child",
  foreignKeys = [ForeignKey(
    entity = TreeClassifyBean::class,
    parentColumns = ["classify_id"],
    childColumns = ["parent_id"]
  )],
  indices = [Index("id"), Index("parent_id")]
)
data class TreeChildBean(
  @PrimaryKey var id: Int = 0,
  @ColumnInfo(name = "parent_id") @Json(name = "parentChapterId") var parentId: Int = 0,
  var name: String = "",
  @Ignore var link: String? = null
) : Parcelable