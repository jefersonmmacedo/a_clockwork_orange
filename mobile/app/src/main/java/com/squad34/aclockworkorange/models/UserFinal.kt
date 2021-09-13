package com.squad34.aclockworkorange.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

object UserFinal {
}data class UserFromValidator(
    val __v: Int,
    val _id: String?,
    val createdAt: String?,
    val email: String?,
    val lastname: String?,
    val name: String?,
    val password: String?,
    val role: String?,
    val updatedAt: String?,
    val error: String?
): Parcelable {
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
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(createdAt)
        parcel.writeString(email)
        parcel.writeString(lastname)
        parcel.writeString(name)
        parcel.writeString(password)
        parcel.writeString(role)
        parcel.writeString(updatedAt)
        parcel.writeString(error)
    }

    companion object CREATOR : Parcelable.Creator<UserFromValidator> {
        override fun createFromParcel(parcel: Parcel): UserFromValidator {
            return UserFromValidator(parcel)
        }

        override fun newArray(size: Int): Array<UserFromValidator?> {
            return arrayOfNulls(size)
        }
    }
}