package com.example.haze.itmomaps.api.objects

import android.os.Parcel
import android.os.Parcelable

data class Room (
        val coordinates : Coordinates? = null,
        val type: String? = null,
        val name: String? = null,
        val description: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Coordinates::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(coordinates, flags)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "$type : $name"
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}