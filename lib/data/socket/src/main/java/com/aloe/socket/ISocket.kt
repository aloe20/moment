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

import java.net.Inet6Address
import java.net.NetworkInterface

interface ISocket {
    fun initSocket()
    fun getUdpReceive(): ByteArray
    fun startUdp(port: Int)
    fun stopUdp()
    fun sendUdp(host: String, port: Int, data: ByteArray)
}

fun getIp(): String {
    return runCatching {
        NetworkInterface.getNetworkInterfaces().asSequence()
            .filter { it.isUp && it.inetAddresses.toList().isNotEmpty() && it.isLoopback.not() }
            .flatMap { it.inetAddresses.asSequence() }
            .filter { (it is Inet6Address && it.isLinkLocalAddress).not() }
            .first().hostName
    }.getOrDefault("")
}