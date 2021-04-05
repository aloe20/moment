package com.aloe.local

import android.app.Application
import com.aloe.bean.TreeBean
import com.aloe.bean.TreeChildBean
import com.aloe.local.dao.TreeDao
import com.aloe.local.store.ConfigPreference
import com.aloe.local.store.SplashImgSerializer
import com.aloe.proto.SplashImg
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RepositoryLocalImpl @Inject constructor(private val app: Application) : RepositoryLocal {
  private val db: AppDatabase = AppDatabase.getInstance(app)
  private val treeDao: TreeDao = db.getTreeDao()

  override fun getSplashImg(): Flow<SplashImg> = SplashImgSerializer.getSplashImg(app)

  override suspend fun updateSplashImg(date: String, url: String) =
    SplashImgSerializer.updateSplashImg(app, date, url)

  override suspend fun saveString(key: String, value: String) = ConfigPreference.saveString(app, key, value)

  override fun getString(key: String): Flow<String?> = ConfigPreference.getString(app, key)

  override suspend fun insertTrees(tree: List<TreeBean>) = treeDao.insertTree(tree)

  override suspend fun loadTreeBeanLocal(): List<TreeBean> = treeDao.loadTreeBeanLocal()
  override suspend fun loadTreeBeanById(classifyId: Int): Flow<Result<TreeBean?>> =
    flowOf(Result.success(treeDao.loadTreeBeanById(classifyId)))


  override suspend fun loadTreeChildById(id: Int): Flow<Result<TreeChildBean?>> =
    flowOf(Result.success(treeDao.loadTreeChildById(id)))
}