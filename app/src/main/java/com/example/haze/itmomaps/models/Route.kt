package com.example.haze.itmomaps.models

import java.lang.Exception
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

class WrongMappingException(message: String) : Exception(message)
class NoSuchWayException(message: String) : Exception(message)

class Route(val from: MapObject, val to: MapObject) {

    private val parent = HashMap<MapObject, MapObject>()

    init {
        if (from.map != to.map) {
            throw WrongMappingException("Route from different buildings")
        }
        val floors = from.getMaxFloor()
        // BFS

        //floor starts with 0
        val visited = Array(100) { Array(100) { Array(floors + 1) { false } } }
        val q = ArrayDeque<MapObject>()
        visited[from.x][from.y][from.floor] = true
        q.addLast(from)
        while (!q.isEmpty()) {
            val cur = q.pop()
            if (cur == to)
                break
            for (i in -1..1) {
                for (j in -1..1) {
                    if (cur.x + i in 0..99 && cur.y + j in 0..99) {
                        val next = MapObject(cur.building, cur.map, cur.x + i, cur.y + j, cur.floor)
                        if (next.isCorridor() && !visited[next.x][next.y][next.floor]) {
                            q.addLast(next)
                            parent[next] = cur
                            visited[next.x][next.y][next.floor] = true
                        }
                    }
                }
            }
            if (cur.isStair()) {
                for (k in -1..1) {
                    val next = MapObject(cur.building, cur.map, cur.x, cur.y, cur.floor + k)
                    if (next.floor in 1..floors && !visited[next.x][next.y][next.floor]) {
                        q.addLast(next)
                        parent[next] = cur
                        visited[next.x][next.y][next.floor] = true
                    }
                }
            }
        }
    }

    fun getRoute() : MutableList<MapObject> {
        var cur = to
        val res : MutableList<MapObject> = mutableListOf()
        res.add(cur)
        while (cur != from) {
            try {
                cur = parent[cur]!!
            } catch (error: KotlinNullPointerException) {
                throw NoSuchWayException("No such way from $from to $to")
            }
            res.add(cur)
        }
        return res.asReversed()
    }

    fun title() = "from $from to $to"
}

