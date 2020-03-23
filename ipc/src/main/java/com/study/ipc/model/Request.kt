package com.study.ipc.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
@Parcelize
class Request(
    val type: Int,
    val serviceId: String,
    val methodName: String?,
    val parameters: Array<Parameters?>
) : Parcelable {
    companion object {
        //获得单例对象
        val GET_INSTANCE = 0

        //执行方法
        val GET_METHOD = 1
    }
}