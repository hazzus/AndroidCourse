package com.example.haze.itmomaps.api

import com.example.haze.itmomaps.api.objects.StairObject
import com.example.haze.itmomaps.api.objects.MapObject
import com.example.haze.itmomaps.api.objects.ReceivedComment
import com.example.haze.itmomaps.api.objects.SentComment
import com.example.haze.itmomaps.api.objects.Map
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response


class MapsRepository(private val apiService: MapsApiService) {
    fun getComments(map: Int, floor: Int, x: Int, y: Int) : Observable<List<ReceivedComment>> {
        return apiService.comments(map, floor, x, y)
    }

    fun postComment(map: Int, floor: Int, x: Int, y: Int, author: String, comment: String, type: String) : Observable<Response<ResponseBody>> {
        return apiService.comment(map, floor, x, y, SentComment(author, comment, type))
    }

    fun getMap(map: Int) : Observable<Map> {
        return apiService.map(map)
    }

    fun getStairs(map: Int) : Observable<List<StairObject>>{
        return apiService.stairs(map)
    }

    fun getCorridors(map: Int) : Observable<List<MapObject>> {
        return apiService.corridors(map)
    }
}
