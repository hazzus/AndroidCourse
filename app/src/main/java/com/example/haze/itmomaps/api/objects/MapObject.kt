package com.example.haze.itmomaps.api.objects
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.haze.itmomaps.api.MapsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


data class MapObject(
        val floor: Int? = null,
        var x: Int? = null,
        var y: Int? = null
) : Parcelable {
    private var stair = false
    private var corridor = false
    var top: MapObject? = null
    var down: MapObject? = null

    init {
        val api = MapsRepositoryProvider.provideMapRepository()
        //TODO get here map
        api.find(1, floor!!, x!!, y!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result.type == "stair") {
                        stair = true
                        if (result.top != null) {
                            top = result.top
                        }
                        if (result.down != null) {
                            down = result.down
                        }
                    } else if (result.type == "corridor") {
                        corridor = true
                    }
                }, { error ->
                    Log.e("MapObject.find", error.localizedMessage)
                })
    }

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int)

    fun convertToComment() {
        x = x!!.div(4)
        y = y!!.div(4)
    }

    fun isStair() : Boolean {
        return stair
    }

    fun isCorridor() : Boolean {
        return corridor
    }


    override fun toString() = intArrayOf(x!!, y!!, floor!!).joinToString(separator = "; ", prefix = "(", postfix = ")")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(floor)
        parcel.writeValue(x)
        parcel.writeValue(y)
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
}