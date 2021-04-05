package com.aloe.bean

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "tree_classify", indices = [Index("classify_id")])
open class TreeClassifyBean(
  @PrimaryKey @ColumnInfo(name = "classify_id") var id: Int = 0,
  var name: String = ""
) : Parcelable