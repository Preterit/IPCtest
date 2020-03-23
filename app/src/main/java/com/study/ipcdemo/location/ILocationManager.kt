package com.study.ipcdemo.location

import com.study.ipc.annotation.ServiceId

/**
 * Date:2020/3/23
 * author:lwb
 * Desc:
 */
@ServiceId("LocationManager")
interface ILocationManager {
    val location: Location?
}
