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

import android.content.Context

internal class SocketImpl constructor(private val ctx: Context, private val udp: ClientUdp) :
    ISocket {
    override fun initSocket() {
        // SocketWorker.initSocket(ctx)
    }

    override fun getUdpReceive(): ByteArray {
        TODO("Not yet implemented")
    }

    // override fun getUdpReceive(): StateFlow<ByteArray> = udp.receiveFlow

    override fun startUdp(port: Int) {
        sendUdp("0.0.0.1", port, ByteArray(0))
    }

    override fun stopUdp() {
        sendUdp("0.0.0.2", 0, ByteArray(0))
    }

    override fun sendUdp(host: String, port: Int, data: ByteArray) {
        udp.sendData(host, port, data)
    }
}
