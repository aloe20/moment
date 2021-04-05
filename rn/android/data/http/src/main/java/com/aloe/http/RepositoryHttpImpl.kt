package com.aloe.http

import com.aloe.bean.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class RepositoryHttpImpl @Inject constructor(private val httpApi: HttpApi) : RepositoryHttp {
  override suspend fun getWanBanner(): Flow<Result<List<WanBannerBean>?>> =
    getFlowData { httpApi.getWanBanner().data }

  override suspend fun getWanTop(): Flow<Result<List<WanBean>?>> = getFlowData { httpApi.getWanTop().data }

  override suspend fun getWanRecommend(page: Int, cid: Int?): Flow<Result<List<WanBean>?>> =
    getFlowData { httpApi.getWanRecommend(page, cid).data?.datas }

  override suspend fun getProjectClassify(): Flow<Result<List<ClassifyBean>?>> =
    getFlowData { httpApi.getProjectClassify().data }

  override suspend fun getProject(page: Int): Flow<Result<List<WanBean>?>> =
    getFlowData { httpApi.getProject(page).data?.datas }

  override suspend fun getProjectClassifyList(cid: Int, page: Int): Flow<Result<List<WanBean>?>> =
    getFlowData { httpApi.getProjectClassifyList(page, cid).data?.datas }

  override suspend fun getArticleClassify(): Flow<Result<List<ClassifyBean>?>> =
    getFlowData { httpApi.getArticleClassify().data }

  override suspend fun getArticleList(id: Int, page: Int, keyword: String?): Flow<Result<List<WanBean>?>> =
    getFlowData { httpApi.getArticleList(id, page, keyword).data?.datas }

  override suspend fun getNavi(): Flow<Result<List<NaviBean>?>> = getFlowData { httpApi.getNavi().data }
  override suspend fun getTree(): Flow<Result<List<TreeBean>?>> = getFlowData { httpApi.getTree().data }

  override suspend fun getHotkey(): Flow<Result<List<HotkeyBean>?>> = getFlowData { httpApi.getHotkey().data }
  override suspend fun getVideoList(): Flow<Result<List<VideoBean>?>> =
    getFlowData { httpApi.getVideoList(20, "video", 0).data }

  private suspend fun <T> getFlowData(block: suspend () -> T): Flow<Result<T>> = flow {
    try {
      emit(Result.success(block.invoke()))
    } catch (e: Exception) {
      e.printStackTrace()
      emit(Result.failure<T>(e))
    }
  }
}