package com.study.ipc.model

import android.os.Parcel
import android.os.Parcelable


/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
class Request(
    private val type: Int,
    private val serviceId: String,
    private val methodName: String,
    private val parameters: Array<Parameters>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArray(Parameters)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(serviceId)
        parcel.writeString(methodName)
        parcel.writeTypedArray(parameters, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        //获得单例对象
        const val GET_INSTANCE = 0

        //执行方法
        const val GET_METHOD = 1

        @JvmStatic
        val CREATOR = object : Parcelable.Creator<Request> {
            override fun createFromParcel(parcel: Parcel): Request {
                return Request(parcel)
            }

            override fun newArray(size: Int): Array<Request?> {
                return arrayOfNulls(size)
            }
        }
    }
}