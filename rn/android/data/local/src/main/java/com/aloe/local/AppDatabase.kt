package com.aloe.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aloe.bean.TreeClassifyBean
import com.aloe.bean.TreeChildBean
import com.aloe.local.dao.TreeDao

@Database(entities = [TreeClassifyBean::class, TreeChildBean::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun getTreeDao(): TreeDao

  companion object {
    @Volatile
    private var instance: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
      instance ?: buildDatabase(context).also { instance = it }
    }

    private fun buildDatabase(context: Context): AppDatabase =
      Room.databaseBuilder(context, AppDatabase::class.java, "moment.db").build()
  }
}