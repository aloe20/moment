package com.aloe.socket

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SocketModule {
  @Provides
  @Singleton
  @Named("sendUdp")
  internal fun getLiveData(): MutableLiveData<ByteArray> = MutableLiveData()

  @Provides
  @Singleton
  @Named("receiveUdp")
  internal fun getLiveData2(): MutableLiveData<ByteArray> = MutableLiveData()

  @Provides
  @Singleton
  fun getSocketData(impl: SocketDataImpl): SocketData = impl
}