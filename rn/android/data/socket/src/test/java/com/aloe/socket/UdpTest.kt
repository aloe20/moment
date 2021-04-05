package com.aloe.socket

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import org.junit.Test

class UdpTest {
    private fun run(port: Int) {
        val group = NioEventLoopGroup()
        try {
            val strap = Bootstrap()
            strap.group(group).channel(NioDatagramChannel::class.java)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(object : SimpleChannelInboundHandler<DatagramPacket>() {
                    override fun channelActive(ctx: ChannelHandlerContext) {
                        super.channelActive(ctx)
                        println("channelActive1")
                    }

                    override fun channelRead0(ctx: ChannelHandlerContext, msg: DatagramPacket) {
                        val content = msg.content().toString(Charsets.UTF_8)
                        println(content)
                        Thread.sleep(TIME)
                        val buffer = Unpooled.copiedBuffer("ABC", Charsets.UTF_8)
                        ctx.writeAndFlush(DatagramPacket(buffer, msg.sender()))
                    }

                    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                        ctx.fireExceptionCaught(cause)
                        ctx.close()
                    }
                })
            strap.bind(port).sync().channel().closeFuture().await()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            group.shutdownGracefully()
        }
    }

    @Test
    private fun udp() {
        Thread { UdpTest().run(PORT1) }.start()
//        Thread.sleep(TIME)
//        Thread { ClientUdp().run(8889) }.start()
    }

    companion object {
        private const val TIME = 5000L
        private const val PORT1 = 8888
    }
}