package com.aloe.socket

import com.aloe.proto.SocketData
import com.aloe.proto.SocketMsg
import com.google.protobuf.ByteString
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import java.net.InetSocketAddress

internal class ClientTcp {
  fun connect(host: String, port: Int) {
    val group = NioEventLoopGroup()
    try {
      val strap = Bootstrap()
      strap.group(group).channel(NioSocketChannel::class.java)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(object : ChannelInitializer<SocketChannel>() {

          override fun initChannel(ch: SocketChannel) {
            ch.pipeline().addLast(ProtobufVarint32FrameDecoder())
              .addLast(ProtobufDecoder(SocketData.Base.getDefaultInstance()))
              .addLast(ProtobufVarint32LengthFieldPrepender())
              .addLast(ProtobufEncoder())
              .addLast(object : ChannelInboundHandlerAdapter() {
                override fun channelActive(ctx: ChannelHandlerContext) {
                  super.channelActive(ctx)
                  val address = ctx.channel().remoteAddress() as InetSocketAddress
                  println("连接服务器${address.hostName}:${address.port}成功")
                  ctx.writeAndFlush(createData())
                }

                override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                  super.channelRead(ctx, msg)
                  val address = ctx.channel().remoteAddress() as InetSocketAddress
                  val data = msg as SocketData.Base
                  val result = data.body.data.toStringUtf8()
                  println("收到服务端(${address.hostName}:${address.port})数据: $result")
                }

                private fun createData(): SocketData.Base {
                  val head = SocketData.Head.newBuilder().setMsg(SocketMsg.Msg.MSG_REQ_HEARTBEAT).build()
                  val body = SocketData.Body.newBuilder().setData(ByteString.copyFromUtf8("心跳")).build()
                  return SocketData.Base.newBuilder().setHead(head).setBody(body).build()
                }
              })
          }
        })
      val future = strap.connect(host, port).sync()
      future.channel().closeFuture().sync()
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      group.shutdownGracefully()
    }
  }
}
