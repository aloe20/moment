/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.aloe.socket

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel

internal class ClientUdp {
    //    private val sendFlow: MutableStateFlow<ByteArray> = MutableStateFlow(byteArrayOf())
//    var receiveFlow: MutableStateFlow<ByteArray> = MutableStateFlow(byteArrayOf())
//        private set
    private var context: ChannelHandlerContext? = null
    private var channel: Channel? = null

    //    private var job: Job? = null
    fun addUdpListener() {
        // val size = Int.SIZE_BYTES + Short.SIZE_BYTES
        /*sendFlow.collect {
            if (it.size >= size) {
                val host = it.slice(0 until Int.SIZE_BYTES).joinToString(".") { byte -> byte.toUByte().toString() }
                val port = it.slice(Int.SIZE_BYTES until size)
                    .fold(0) { acc, byte -> acc.shl(Byte.SIZE_BITS).plus(byte.toUByte().toShort()) }
                when (host) {
                    "0.0.0.1" -> job = CoroutineScope(Dispatchers.Default).launch { start(port) }
                    "0.0.0.2" -> channel?.takeIf { ch -> ch.isOpen && ch.isActive }?.close()
                    else -> {
                        val data = it.sliceArray(size until it.size)
                        println("发送udp数据($host:$port)：${String(data)}")
                        context?.writeAndFlush(
                            DatagramPacket(Unpooled.copiedBuffer(data), InetSocketAddress(host, port))
                        )
                    }
                }
            }
        }*/
    }

    fun sendData(host: String, port: Int, data: ByteArray) {
//        sendFlow.value = host.split(".").map { it.toInt().toByte() }.plus(port.shr(Byte.SIZE_BITS).toByte())
//            .plus(port.and(UByte.MAX_VALUE.toInt()).toByte()).toByteArray().plus(data)
    }

    private fun start(localPort: Int) {
        val group = NioEventLoopGroup()
        runCatching {
            val strap = Bootstrap()
            strap.group(group).channel(NioDatagramChannel::class.java)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(object : SimpleChannelInboundHandler<DatagramPacket>() {
                    override fun channelActive(ctx: ChannelHandlerContext) {
                        super.channelActive(ctx)
                        context = ctx
                        println("UDP(${getIp()}:$localPort)通道已建立")
                    }

                    override fun channelRead0(ctx: ChannelHandlerContext?, msg: DatagramPacket) {
                        // var content = ""
                        /*receiveFlow.value = msg.sender().run {
                            sendData(hostName, port, "aloe".toByteArray())
                            content += "$hostName:$port"
                            hostName.split(".").map { it.toInt().toByte() }.plus(port.shr(Byte.SIZE_BITS).toByte())
                                .plus(port.and(UByte.MAX_VALUE.toInt()).toByte())
                        }.toByteArray().plus(
                            msg.content().run {
                                println("收到UDP数据($content)：${toString(CharsetUtil.UTF_8)}")
                                array().sliceArray(0 until writerIndex())
                            }
                        )*/
                    }

                    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
                        ctx?.fireExceptionCaught(cause)
                        ctx?.close()
                        context?.fireExceptionCaught(cause)
                        context?.close()
                        context = null
                        println("UDP通道已断开")
                    }
                })
            channel = strap.bind(localPort).sync().channel()
            channel?.closeFuture()?.await()
        }
        group.shutdownGracefully()
//        job?.cancel()
    }
}
