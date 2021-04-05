package com.aloe.http

import com.aloe.bean.*
import com.aloe.result.HttpResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 获取音频资源https://www.bensound.com/royalty-free-music/1，解析html
 * 获取头条视频
 * 1、请求视频列表
 * https://m.toutiao.com/list/?tag=video&ac=wap&count=20&format=json_raw&as=A146C01537C53E8&cp=6057B5131EB82E1&min_behot_time=0&_signature=l8yhQQAA96p.FoM8pGV7IJfMoV&i=
 * 2、解析video_id，加载视频详情页数据
 * http://i.snssdk.com/video/urls/1/toutiao/mp4/{video_id}
 * 3、解析main_url，BASE64解密获取视频播放链接
 */
internal interface HttpApi {
  /**
   * 推荐Banner
   */
  @GET("https://www.wanandroid.com/banner/json")
  suspend fun getWanBanner(): HttpResult<List<WanBannerBean>?>

  /**
   * 推荐置顶
   */
  @GET("https://www.wanandroid.com/article/top/json")
  suspend fun getWanTop(): HttpResult<List<WanBean>?>

  /**
   * 推荐列表
   * @param page 页码，从0开始
   */
  @GET("https://www.wanandroid.com/article/list/{page}/json")
  suspend fun getWanRecommend(@Path("page") page: Int, @Query("cid") cid: Int?): HttpResult<HttpResult<List<WanBean>?>?>

  /**
   * 项目列表
   * @param page 页码，从0开始
   */
  @GET("https://wanandroid.com/article/listproject/{page}/json")
  suspend fun getProject(@Path("page") page: Int): HttpResult<HttpResult<List<WanBean>?>?>

  @GET("https://www.wanandroid.com/project/tree/json")
  suspend fun getProjectClassify(): HttpResult<List<ClassifyBean>?>

  @GET("https://www.wanandroid.com/project/list/{page}/json")
  suspend fun getProjectClassifyList(
    @Path("page") page: Int,
    @Query("cid") cid: Int
  ): HttpResult<HttpResult<List<WanBean>?>?>

  @GET("https://wanandroid.com/wxarticle/chapters/json")
  suspend fun getArticleClassify(): HttpResult<List<ClassifyBean>?>

  @GET("https://wanandroid.com/wxarticle/list/{id}/{page}/json")
  suspend fun getArticleList(
    @Path("id") id: Int,
    @Path("page") page: Int,
    @Query("k") keyword: String?
  ): HttpResult<HttpResult<List<WanBean>?>?>

  /**
   * 导航列表
   */
  @GET("https://www.wanandroid.com/navi/json")
  suspend fun getNavi(): HttpResult<List<NaviBean>?>

  @GET("https://www.wanandroid.com/tree/json")
  suspend fun getTree(): HttpResult<List<TreeBean>?>

  @GET("https://www.wanandroid.com/hotkey/json")
  suspend fun getHotkey(): HttpResult<List<HotkeyBean>?>

  @Headers("Cookie: tt_webid=6941338939450574367")
  @GET("https://m.toutiao.com/list/?format=json_raw&as=A146C01537C53E8")
  suspend fun getVideoList(
    @Query("count") count: Int,
    @Query("tag") tag: String,
    @Query("min_behot_time") hotTime: Long
  ): HttpResult<List<VideoBean>>
}