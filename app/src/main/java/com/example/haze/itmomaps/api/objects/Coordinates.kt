package com.example.haze.itmomaps.api.objects

import android.os.Parcel
import android.os.Parcelable

// TODO assert all squares have same floor (or maybe not)
data class Coordinates (
        val squares: Array<MapObject>? = null,
        val door: MapObject? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArray(MapObject),
            parcel.readParcelable(MapObject::class.java.classLoader))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinates

        if (!squares!!.contentEquals(other.squares!!)) return false
        if (door != other.door) return false

        return true
    }

    override fun hashCode(): Int {
        var result = squares!!.contentHashCode()
        result = 31 * result + door.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(squares, flags)
        parcel.writeParcelable(door, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coordinates> {
        override fun createFromParcel(parcel: Parcel): Coordinates {
            return Coordinates(parcel)
        }

        override fun newArray(size: Int): Array<Coordinates?> {
            return arrayOfNulls(size)
        }
    }
}