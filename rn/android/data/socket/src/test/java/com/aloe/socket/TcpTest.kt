package com.aloe.socket

import com.aloe.proto.SocketData
import com.aloe.proto.SocketMsg
import com.google.protobuf.ByteString
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import org.junit.Test
import java.net.InetSocketAddress

class TcpTest {
  private fun bind(port: Int) {
    val bossGroup = NioEventLoopGroup()
    val workerGroup = NioEventLoopGroup()
    try {
      val strap = ServerBootstrap()
      strap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel::class.java)
        .option(ChannelOption.SO_BACKLOG, SIZE)
        .childHandler(object : ChannelInitializer<SocketChannel>() {

          override fun initChannel(ch: SocketChannel) {
            ch.pipeline().addLast(ProtobufVarint32FrameDecoder())
              .addLast(ProtobufDecoder(SocketData.Base.getDefaultInstance()))
              .addLast(ProtobufVarint32LengthFieldPrepender())
              .addLast(ProtobufEncoder())
              .addLast(object : ChannelInboundHandlerAdapter() {
                override fun channelActive(ctx: ChannelHandlerContext) {
                  super.channelActive(ctx)
                  val address = ctx.channel().remoteAddress() as InetSocketAddress
                  println("${address.hostName}:${address.port}客户端连接成功")
                }

                override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                  super.channelRead(ctx, msg)
                  val address = ctx.channel().remoteAddress() as InetSocketAddress
                  if (msg is SocketData.Base) {
                    val result = msg.body.data.toStringUtf8()
                    println("收到客户端(${address.hostName}:${address.port})数据: $result")
                    Thread.sleep(TIME)
                    if (msg.head.msg == SocketMsg.Msg.MSG_REQ_HEARTBEAT) {
                      ctx.writeAndFlush(createData())
                    }
                  }
                }

                override fun exceptionCaught(
                  ctx: ChannelHandlerContext,
                  cause: Throwable
                ) {
                  ctx.fireExceptionCaught(cause)
                  ctx.close()
                }

                private fun createData(): SocketData.Base {
                  val head = SocketData.Head.newBuilder()
                    .setMsg(SocketMsg.Msg.MSG_RSP_HEARTBEAT).build()
                  val body = SocketData.Body.newBuilder()
                    .setData(ByteString.copyFromUtf8("心跳回复")).build()
                  return SocketData.Base.newBuilder().setHead(head).setBody(body)
                    .build()
                }
              })
          }
        })
      val future = strap.bind(port).sync()
      future.channel().closeFuture().sync()
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      bossGroup.shutdownGracefully()
      workerGroup.shutdownGracefully()
    }
  }

  @Test
  private fun tcp() {
    Thread { TcpTest().bind(PORT1) }.start()
//        Thread.sleep(TIME)
//        Thread { ClientTcp().connect("192.168.184.51", PORT1) }.start()
  }

  companion object {
    private const val TIME = 1000L
    private const val SIZE = 1024
    private const val PORT1 = 8888
  }
}