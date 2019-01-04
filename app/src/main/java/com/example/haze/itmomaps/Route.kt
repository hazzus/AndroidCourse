package com.example.haze.itmomaps

import com.example.haze.itmomaps.api.MapsRepositoryProvider
import com.example.haze.itmomaps.api.objects.MapObject
import com.example.haze.itmomaps.api.objects.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.lang.Math.abs
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class NoSuchWayException(message: String) : Exception(message)

class RouteBuildingComparator(private val dest: MapObject) : Comparator<MapObject> {
    private fun distance(o1: MapObject?): Int {
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

    private val corridor = Array(100) { Array(100) { Array(maxFloor + 1) { true } } }
    private val stair = Array(100) { Array(100) { Array(maxFloor + 1) { Triple<Boolean, MapObject?, MapObject?>(false, null, null) } } }

    init {
        // BFS
        //floor starts with 0

        val api = MapsRepositoryProvider.provideMapRepository()
        api.getMap(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { result ->
                    api.getStairs(1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe {
                                for (s in it)
                                    stair[s.self!!.x!!][s.self.y!!][s.self.floor!!] = Triple(true, s.top, s.down)
                                buildRoute()
                            }
                    result.objects!!.forEach {
                        it.coordinates!!.squares!!.forEach {coordinate ->
                            corridor[coordinate.x!!][coordinate.y!!][coordinate.floor!!] = false
                        }
                    }
                }
    }

    private fun buildRoute() {
        val visited = Array(100) { Array(100) { Array(maxFloor + 1) { false } } }
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
                        val next = MapObject(cur.floor, cur.x!! + i, cur.y!! + j)
                        if (corridor[next.x!!][next.y!!][next.floor!!] && !visited[next.x!!][next.y!!][next.floor]) {
                            q.add(next)
                            parent[next] = cur
                            visited[next.x!!][next.y!!][next.floor] = true
                        }
                    }
                }
            }
            val curStair = stair[cur.x!!][cur.y!!][cur.floor!!]
            if (curStair.first) { // TODO replace with stairs array
                var next = curStair.second
                if (next != null && !visited[next.x!!][next.y!!][next.floor!!]) {
                    q.add(next)
                    parent[next] = cur
                    visited[next.x!!][next.y!!][next.floor!!] = true
                }
                next = curStair.third
                if (next != null && !visited[next.x!!][next.y!!][next.floor!!]) {
                    q.add(next)
                    parent[next] = cur
                    visited[next.x!!][next.y!!][next.floor!!] = true
                }
            }
        }
    }

    fun getRoute(): List<MapObject> {
        var cur: MapObject = to!!
        val res: MutableList<MapObject> = mutableListOf()
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

