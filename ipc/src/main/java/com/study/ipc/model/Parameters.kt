package com.study.ipc.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
class Parameters(
    private val type: String,  // 参数类型 class
    private val value: String  // 参数值 json序列化后的字符串
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Parameters> {
        override fun createFromParcel(parcel: Parcel): Parameters {
            return Parameters(parcel)
        }

        override fun newArray(size: Int): Array<Parameters?> {
            return arrayOfNulls(size)
        }
    }
}