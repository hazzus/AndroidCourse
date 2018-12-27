package com.example.haze.itmomaps.api

import com.example.haze.itmomaps.models.ReceivedComment
import com.example.haze.itmomaps.models.SentComment
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
}
