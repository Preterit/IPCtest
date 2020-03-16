package com.study.ipcdemo.location

import com.study.ipc.annotation.ServiceId

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
@ServiceId("LocationManager")
class LocationManager {

    companion object {
        private val ourInstance = LocationManager()
        @JvmStatic
        fun getDefault(): LocationManager {
            return ourInstance
        }
    }

    private fun LocationManager() {}

    private var location: Location? = null

    fun setLocation(location: Location?) {
        this.location = location
    }

    fun getLocation(): Location? {
        return location
    }

}