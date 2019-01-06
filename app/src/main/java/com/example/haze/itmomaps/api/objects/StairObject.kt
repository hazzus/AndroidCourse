package com.example.haze.itmomaps.api.objects

data class StairObject(
        val type: String? = null,
        val room: Room? = null,
        val self: MapObject? = null,
        val top: MapObject? = null,
        val down: MapObject? = null
)