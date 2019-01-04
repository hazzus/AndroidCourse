package com.example.haze.itmomaps.api.objects

data class FindObject(
        val type: String? = null,
        val room: Room? = null,
        val self: MapObject? = null,
        val top: MapObject? = null,
        val down: MapObject? = null
)