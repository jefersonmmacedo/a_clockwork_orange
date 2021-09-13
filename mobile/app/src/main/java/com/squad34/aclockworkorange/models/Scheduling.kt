package com.squad34.aclockworkorange.models

import android.os.Parcel
import android.os.Parcelable

object Schedulingdata {data class DateScheduling(
    val __v: Int,
    val _id: String?,
    val _idUser: String?,
    val createdAt: String?,
    val date: String?,
    val day: String?,
    val email: String?,
    val lastname: String?,
    val location: String?,
    val name: String?,
    val role: String?,
    val shift: String?,
    val type: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(_idUser)
        parcel.writeString(createdAt)
        parcel.writeString(date)
        parcel.writeString(day)
        parcel.writeString(email)
        parcel.writeString(lastname)
        parcel.writeString(location)
        parcel.writeString(name)
        parcel.writeString(role)
        parcel.writeString(shift)
        parcel.writeString(type)
    }

    companion object CREATOR : Parcelable.Creator<DateScheduling> {
        override fun createFromParcel(parcel: Parcel): DateScheduling {
            return DateScheduling(parcel)
        }

        override fun newArray(size: Int): Array<DateScheduling?> {
            return arrayOfNulls(size)
        }
    }
}
}