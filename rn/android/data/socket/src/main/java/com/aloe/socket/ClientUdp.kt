package com.aloe.socket

import androidx.lifecycle.MutableLiveData
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import java.net.InetSocketAddress

internal class ClientUdp(
  sendLiveData: MutableLiveData<ByteArray>,
  private val receiveLiveData: MutableLiveData<ByteArray>
) {
  private lateinit var context: ChannelHandlerContext

  init {
    sendLiveData.observeForever {
      var ip = ""
      var port = 0
      it.forEachIndexed { index, byte ->
        if (index < 4) {
          @Suppress("EXPERIMENTAL_API_USAGE")
          ip = ip.plus('.').plus(byte.toUByte())
        } else if (index < 6) {
          port = port.shl(8).plus(byte)
        }
      }
      if (this::context.isInitialized) {
        context.writeAndFlush(
          DatagramPacket(
            Unpooled.copiedBuffer(it.sliceArray(IntRange(6, it.size-1))),
            InetSocketAddress(ip.substring(1), port)
          )
        )
      }
    }
  }

  fun run(port: Int) {
    val group = NioEventLoopGroup()
    try {
      val strap = Bootstrap()
      strap.group(group).channel(NioDatagramChannel::class.java)
        .option(ChannelOption.SO_BROADCAST, true)
        .handler(object : SimpleChannelInboundHandler<DatagramPacket>() {
          override fun channelActive(ctx: ChannelHandlerContext) {
            super.channelActive(ctx)
            context = ctx
          }

          override fun channelRead0(ctx: ChannelHandlerContext, msg: DatagramPacket) {
            receiveLiveData.postValue(msg.sender().run {
              hostName.split(".").map { it.toInt().toByte() }.plus(port.shr(8).toByte()).plus(port.and(0xFF).toByte())
            }.toByteArray().plus(msg.content().array().sliceArray(IntRange(0, msg.content().writerIndex()-1))))
          }

          override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            ctx.fireExceptionCaught(cause)
            ctx.close()
          }
        })
      val channel = strap.bind(port).sync().channel()
      channel.closeFuture().await()
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      group.shutdownGracefully()
    }
  }
}

