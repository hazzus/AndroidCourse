package com.example.haze.itmomaps.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class MapObject(val building: String, val map: Int, var x: Int, var y: Int, val floor: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    constructor(name: String, map: Int, s: String) : this(
            name,
            map,
            1,
            1,
            1
            //TODO implement fromString or rewrite RouteActivity to selectors
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(building)
        parcel.writeInt(map)
        parcel.writeInt(x)
        parcel.writeInt(y)
        parcel.writeInt(floor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapObject> {
        override fun createFromParcel(parcel: Parcel): MapObject {
            return MapObject(parcel)
        }

        override fun newArray(size: Int): Array<MapObject?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString() = "$map/$x/$y/$floor" // NOTE only for web!!

    override fun hashCode(): Int {
        return ((((((x shl 7) or y) shl 5) or floor) shl 5) or map)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is MapObject) {
            (other.x == this.x) and
                    (other.y == this.y) and
                    (other.floor == this.floor) and
                    (other.map == this.map) and
                    (other.building == this.building)
        } else
            false
    }

    // TODO reimplement this to network getter
    fun title() = intArrayOf(x, y, floor).joinToString(separator = "; ", prefix = "$building (", postfix = ")")

    fun convertToComment() {
        x /= 4
        y /= 4
    }

    fun getMaxFloor(): Int {
        // TODO implement this
        return 5
    }

    fun isStair(): Boolean {
        // TODO implement this
        return true
    }

    fun isCorridor(): Boolean {
        // TODO implement this
        return true
    }
}