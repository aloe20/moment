package com.aloe.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.aloe.bean.TreeBean
import com.aloe.bean.TreeClassifyBean
import com.aloe.bean.TreeChildBean

@Dao
abstract class TreeDao {

  @Transaction
  open suspend fun insertTree(treeBean: List<TreeBean>) {
    treeBean.forEach {
      insertTreeClassify(it)
      insertTrees(it.children)
    }
  }

  @Insert
  abstract suspend fun insertTreeClassify(treeClassify: TreeClassifyBean)

  @Insert
  abstract suspend fun insertTrees(treeBean: List<TreeChildBean>)

  @Transaction
  @Query("SELECT * FROM tree_classify WHERE classify_id IN (SELECT DISTINCT(parent_id) FROM tree_child)")
  abstract suspend fun loadTreeBeanLocal(): List<TreeBean>

  @Transaction
  @Query("SELECT * FROM tree_classify WHERE classify_id = :classifyId AND classify_id IN (SELECT DISTINCT(parent_id) FROM tree_child WHERE parent_id = :classifyId LIMIT 1)")
  abstract suspend fun loadTreeBeanById(classifyId: Int): TreeBean?

  @Query("select * from tree_child where id = :id limit 1")
  abstract suspend fun loadTreeChildById(id: Int): TreeChildBean?
}