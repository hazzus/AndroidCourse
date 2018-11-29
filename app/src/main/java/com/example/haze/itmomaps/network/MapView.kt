package com.example.haze.itmomaps.network

import java.util.*

/**
 *
 * @param name
 * @param pictures
 * @param objects
 */
data class MapView(
        val name: kotlin.String? = null,
        val pictures: kotlin.Array<PictureView>? = null,
        val objects: kotlin.Array<MapObjectView>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapView

        if (name != other.name) return false
        if (!Arrays.equals(pictures, other.pictures)) return false
        if (!Arrays.equals(objects, other.objects)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (pictures?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + (objects?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }

}

