package com.study.ipcdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.study.ipc.Ipc
import com.study.ipcdemo.location.Location
import com.study.ipcdemo.location.LocationManager

/**
 * Date:2020/3/23
 * author:lwb
 * Desc:
 */
class GpsService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //定位
        LocationManager.getDefault().setLocation(Location("北京市", 550.222, 322.8383))

        /**
         * 1、在数据/服务提供方进行服务注册
         */
        Ipc.register(LocationManager::class.java)
    }
}