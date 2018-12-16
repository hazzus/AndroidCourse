package com.example.haze.itmomaps

import java.util.*
import kotlin.collections.HashMap


class Route(val from: String, val to: String, val building : String) {

    // TODO implement this class fully
    private val parent = HashMap<Triple<Int, Int, Int>, Triple<Int, Int, Int>>()
    private var fromPoint : Triple<Int, Int, Int> = Triple(1, 1, 1) // = getCoordinates(from)
    private var toPoint : Triple<Int, Int, Int> = Triple(1, 2, 1)   // = getCoordinated(to)


    private fun isCorridor(x: Int, y: Int, floor: Int) : Boolean {
        //TODO implement this
        return true
    }

    private fun isStair(t: Triple<Int, Int, Int>) : Boolean {
        //TODO implement this
        return false
    }

    private fun getCoordinates(name : String) : Triple<Int, Int, Int> {
        // TODO implement this
        return Triple(1, 1, 1)
    }

    init {
        val floors = 5 //TODO getBuildingFloors(building)
        // BFS

        //floor starts with 0
        val visited = Array(100, { i -> Array(100, { i -> Array(floors, { i -> false}) }) })
        val q = ArrayDeque<Triple<Int, Int, Int>>()
        visited[fromPoint.first][fromPoint.second][fromPoint.third] = true
        q.addLast(fromPoint)
        while (!q.isEmpty()) {
            val cur = q.pop()
            if (cur == toPoint) break
            for (i in -1..1) {
                for (j in -1..1) {
                    if (cur.first + i in 0..99 && cur.second + j in 0..99) {
                        if (!visited[cur.first + i][cur.second + j][cur.third] && isCorridor(cur.first + i, cur.second + j, cur.third)) {
                            q.addLast(Triple(cur.first + i, cur.second + j, cur.third))
                            parent[Triple(cur.first + i, cur.second + j, cur.third)] = cur
                            visited[cur.first + i][cur.second + j][cur.third] = true
                        }
                    }
                }
            }
            if (isStair(cur)) {
                for (k in -1..1) {
                    if (cur.third + k in 0..(floors - 1) && !visited[cur.first][cur.second][cur.third + k]) {
                        q.addLast(Triple(cur.first, cur.second, cur.third + k))
                        parent[Triple(cur.first, cur.second, cur.third + k)] = cur
                        visited[cur.first][cur.second][cur.third + k] = true
                    }
                }
            }
        }
    }

    fun getRoute() : MutableList<Triple<Int, Int, Int>> {
        var cur = toPoint
        val res : MutableList<Triple<Int, Int, Int>> = mutableListOf()
        res.add(cur)
        while (cur != fromPoint) {
            cur = parent[cur]!!
            res.add(cur)
        }
        return res.asReversed()
    }

    fun title() = "from $from to $to in $building"

}