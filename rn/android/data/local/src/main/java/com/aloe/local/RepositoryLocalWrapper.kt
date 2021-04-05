package com.aloe.local

import com.aloe.socket.SocketData
import com.aloe.socket.SocketDataWrapper

open class RepositoryLocalWrapper(local: RepositoryLocal, socket:SocketData) :SocketDataWrapper(socket), RepositoryLocal by local