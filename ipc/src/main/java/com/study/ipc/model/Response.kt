package com.study.ipc.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
class Response(
    private val source: String?,    // 执行远程方法的返回  JSON 结果
    private val isSuccess: Boolean  // 是否成功执行远程方法
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeByte(if (isSuccess) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Response> {
        override fun createFromParcel(parcel: Parcel): Response {
            return Response(parcel)
        }

        override fun newArray(size: Int): Array<Response?> {
            return arrayOfNulls(size)
        }
    }

}