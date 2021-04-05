package com.aloe.local.store

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.aloe.proto.SplashImg
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
internal object SplashImgSerializer : Serializer<SplashImg> {
  private lateinit var dataStore: DataStore<SplashImg>
  override val defaultValue: SplashImg = SplashImg.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): SplashImg = try {
    SplashImg.parseFrom(input)
  } catch (e: CorruptionException) {
    throw CorruptionException("Cannot read proto.", e)
  }

  override suspend fun writeTo(t: SplashImg, output: OutputStream) = t.writeTo(output)

  private fun getDataStore(context: Context): DataStore<SplashImg> {
    if (!this::dataStore.isInitialized) {
      dataStore = DataStoreFactory.create(SplashImgSerializer) {
        File("${context.filesDir.path}/datastore", "splash.pb")
      }
    }
    return dataStore
  }

  internal fun getSplashImg(context: Context): Flow<SplashImg> = getDataStore(context).data

  internal suspend fun updateSplashImg(context: Context, date: String, url: String) {
    getDataStore(context).updateData { it.toBuilder().setDate(date).setImgUrl(url).build() }
  }
}