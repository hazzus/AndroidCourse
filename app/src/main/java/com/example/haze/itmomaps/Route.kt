package com.example.haze.itmomaps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.example.haze.itmomaps.api.MapsRepositoryProvider
import com.example.haze.itmomaps.api.objects.MapObject
import com.example.haze.itmomaps.api.objects.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.Math.abs
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class NoSuchWayException(message: String) : Exception(message)

class RouteBuildingComparator(private val dest: MapObject) : Comparator<MapObject> {
    private fun distance(o1: MapObject?): Int {
        return abs(o1!!.x!! - dest.x!!) + abs(o1.y!! - dest.y!!) + abs(o1.floor!! - dest.floor!!)
    }

    override fun compare(o1: MapObject?, o2: MapObject?): Int {
        return distance(o1) - distance(o2)
    }

}

class Route(private val fromRoom: Room, private val toRoom: Room) {

    private val parent = HashMap<MapObject, MapObject>()
    private val from = fromRoom.coordinates!!.door
    private val to = toRoom.coordinates!!.door

    private val notCorridor = HashSet<MapObject>()
    private val stair = HashMap<MapObject, Pair<MapObject?, MapObject?>>()

    init {
        val api = MapsRepositoryProvider.provideMapRepository()
        api.getMap(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { result ->
                    result.objects!!.forEach {
                        it.coordinates!!.squares!!.forEach {coordinate ->
                            notCorridor.add(coordinate)
                        }

                        it.coordinates.door?.let { it1 -> notCorridor.remove(it1) }
                    }
                    api.getStairs(1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe {
                                for (s in it)
                                    stair[s.self!!] = Pair(s.top, s.down)
                                buildRoute()
                            }

                }
    }

    private fun buildRoute() {
        // TODO BUILDS NOT ALWAYS
        val visited = HashSet<MapObject>()
        val queue = PriorityQueue<MapObject>(100, RouteBuildingComparator(to!!))
        queue.add(from!!)
        visited.add(from)
        while (!queue.isEmpty()) {
            val cur = queue.poll()
            if (cur == to) break
            for (i in -1..1)
                for (j in -1..1) {
                    if (cur.x!! + i in 0..99 && cur.y!! + j in 0..99) {
                        val next = MapObject(cur.floor, cur.x!! + i, cur.y!! + j)
                        if (!notCorridor.contains(next) && !visited.contains(next)) {
                            queue.add(next)
                            parent[next] = cur
                            visited.add(next)
                        }
                    }
                }
            val curStair = stair.get(cur)
            if (curStair != null) {
                val top = curStair.first
                if (top != null && !visited.contains(top)) {
                    queue.add(top)
                    parent[top] = cur
                    visited.add(top)
                }
                val down = curStair.first
                if (down != null && !visited.contains(down)) {
                    queue.add(down)
                    parent[down] = cur
                    visited.add(down)
                }
            }
        }
        Log.i("Route", "Building finished")
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

