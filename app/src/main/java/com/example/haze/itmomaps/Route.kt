package com.example.haze.itmomaps

import com.example.haze.itmomaps.api.objects.MapObject
import com.example.haze.itmomaps.api.objects.Room
import java.lang.Exception
import java.lang.Math.abs
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class NoSuchWayException(message: String) : Exception(message)

class RouteBuildingComparator(private val dest: MapObject) : Comparator<MapObject> {
    private fun distance(o1: MapObject?) : Int {
        return abs(o1!!.x!! - dest.x!!) + abs(o1.y!! - dest.y!!) + abs(o1.floor!! - dest.floor!!)
    }

    override fun compare(o1: MapObject?, o2: MapObject?): Int {
        return distance(o1) - distance(o2)
    }

}

class Route(private val fromRoom: Room, private val toRoom: Room, private val maxFloor: Int) {

    private val parent = HashMap<MapObject, MapObject>()
    private val from = fromRoom.coordinates!!.door
    private val to = toRoom.coordinates!!.door

    init {
        // BFS
        //floor starts with 0
        val visited = Array(100) { Array(100) { Array(maxFloor + 1) { false } } }
        // TODO NullPointerException on to!!
        val q = PriorityQueue<MapObject>(100, RouteBuildingComparator(to!!))
        visited[from!!.x!!][from.y!!][from.floor!!] = true
        q.add(from)
        while (!q.isEmpty()) {
            val cur = q.poll()
            if (cur == to)
                break
            for (i in -1..1) {
                for (j in -1..1) {
                    if (cur.x!! + i in 0..99 && cur.y!! + j in 0..99) {
                        val next = MapObject(cur.x!! + i, cur.y!! + j, cur.floor)
                        if (next.isCorridor() && !visited[next.x!!][next.y!!][next.floor!!]) {
                            q.add(next)
                            parent[next] = cur
                            visited[next.x!!][next.y!!][next.floor] = true
                        }
                    }
                }
            }
            if (cur.isStair()) {
                var next = cur.top
                if (next != null && !visited[next.x!!][next.y!!][next.floor!!]) {
                    q.add(next)
                    parent[next] = cur
                    visited[next.x!!][next.y!!][next.floor!!] = true
                }
                next = cur.down
                if (next != null && !visited[next.x!!][next.y!!][next.floor!!]) {
                    q.add(next)
                    parent[next] = cur
                    visited[next.x!!][next.y!!][next.floor!!] = true
                }
            }
        }
    }

    fun getRoute() : List<MapObject> {
        var cur : MapObject = to!!
        val res : MutableList<MapObject> = mutableListOf()
        res.add(cur)
        while (cur != from) {
            try {
                cur = parent[cur]!!
            } catch (error: KotlinNullPointerException) {
                throw NoSuchWayException("No such way from $fromRoom to $toRoom")
            }
            res.add(cur)
        }
        return res.asReversed()
    }

    fun title() = "from $fromRoom to $toRoom"
}

