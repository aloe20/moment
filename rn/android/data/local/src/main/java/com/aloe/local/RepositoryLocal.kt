package com.aloe.local

import com.aloe.bean.TreeBean
import com.aloe.bean.TreeChildBean
import com.aloe.proto.SplashImg
import kotlinx.coroutines.flow.Flow

interface RepositoryLocal {
  fun getSplashImg(): Flow<SplashImg>
  suspend fun updateSplashImg(date: String, url: String)
  suspend fun saveString(key: String, value: String)
  fun getString(key: String): Flow<String?>
  suspend fun insertTrees(tree: List<TreeBean>)
  suspend fun loadTreeBeanLocal(): List<TreeBean>
  suspend fun loadTreeBeanById(classifyId: Int): Flow<Result<TreeBean?>>
  suspend fun loadTreeChildById(id: Int): Flow<Result<TreeChildBean?>>
}