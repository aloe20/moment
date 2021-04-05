package com.aloe.http

import com.aloe.bean.TreeBean
import com.aloe.local.RepositoryLocal
import com.aloe.local.RepositoryLocalWrapper
import com.aloe.socket.SocketData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class Repository(private var local: RepositoryLocal, private val http: RepositoryHttp, socket:SocketData) : RepositoryLocalWrapper(local, socket),
  RepositoryHttp by http {
  override suspend fun getTree(): Flow<Result<List<TreeBean>?>> {
    val list = local.loadTreeBeanLocal()
    return if (list.isEmpty()) {
      http.getTree().map { map ->
        map.also { result ->
          result.getOrNull()?.takeUnless { it.isEmpty() }?.also { local.insertTrees(it) }
        }
      }
    } else {
      flowOf(Result.success(list))
    }
  }
}