package com.aloe.http

import com.aloe.bean.*
import kotlinx.coroutines.flow.Flow

interface RepositoryHttp {
  suspend fun getWanBanner(): Flow<Result<List<WanBannerBean>?>>
  suspend fun getWanTop(): Flow<Result<List<WanBean>?>>
  suspend fun getWanRecommend(page: Int, cid: Int?): Flow<Result<List<WanBean>?>>
  suspend fun getProjectClassify(): Flow<Result<List<ClassifyBean>?>>
  suspend fun getProject(page: Int): Flow<Result<List<WanBean>?>>
  suspend fun getProjectClassifyList(cid: Int, page: Int): Flow<Result<List<WanBean>?>>
  suspend fun getArticleClassify(): Flow<Result<List<ClassifyBean>?>>
  suspend fun getArticleList(id: Int, page: Int, keyword: String?): Flow<Result<List<WanBean>?>>
  suspend fun getNavi(): Flow<Result<List<NaviBean>?>>
  suspend fun getTree(): Flow<Result<List<TreeBean>?>>
  suspend fun getHotkey(): Flow<Result<List<HotkeyBean>?>>
  suspend fun getVideoList(): Flow<Result<List<VideoBean>?>>
}