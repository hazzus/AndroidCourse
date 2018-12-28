package com.example.haze.itmomaps.api.objects

import java.util.*

data class Map(
        val name: String? = null,
        val pictures: Array<Picture>? = null,
        val objects: Array<Room>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Map

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