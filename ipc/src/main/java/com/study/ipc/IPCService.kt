package com.study.ipc

import android.app.Service
import android.content.Intent
import android.os.IBinder


/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
abstract class IPCService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    class IPCService0 : IPCService()
    class IPCService1 : IPCService()
    class IPCService2 : IPCService()
}
