package com.example.haze.itmomaps.api.objects

import android.os.Parcel
import android.os.Parcelable
import com.example.haze.itmomaps.api.objects.ReceivedComment

data class SentComment(val name: String,
                           val comment: String,
                           val type: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(comment)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReceivedComment> {
        override fun createFromParcel(parcel: Parcel): ReceivedComment {
            return ReceivedComment(parcel)
        }

        override fun newArray(size: Int): Array<ReceivedComment?> {
            return arrayOfNulls(size)
        }
    }
}