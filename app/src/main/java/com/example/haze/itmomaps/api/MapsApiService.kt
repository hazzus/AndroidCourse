package com.example.haze.itmomaps.api


import com.example.haze.itmomaps.api.objects.Map
import com.example.haze.itmomaps.api.objects.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MapsApiService {
    @GET("comments/{map}/{floor}/{x}/{y}/")
    fun comments(@Path("map") map: Int,
                 @Path("floor") floor: Int,
                 @Path("x") x: Int,
                 @Path("y") y: Int
    ): Observable<List<ReceivedComment>>

    @Headers("Content-Type: application/json")
    @POST("comments/{map}/{floor}/{x}/{y}/")
    fun comment(@Path("map") map: Int,
                @Path("floor") floor: Int,
                @Path("x") x: Int,
                @Path("y") y: Int,
                @Body body: SentComment
    ): Observable<Response<ResponseBody>>

    @GET("map/{map}/")
    fun map(@Path("map") map: Int) : Observable<Map>

    @GET("map/{map}/find/{floor}/{x}/{y}/")
    fun find(@Path("map") map: Int,
             @Path("floor") floor: Int,
             @Path("x") x: Int,
             @Path("y") y: Int
    ) : Observable<FindObject>

    companion object Factory {
        fun create(): MapsApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://maps.brilzlian.me/v1/")
                    .build()
            return retrofit.create(MapsApiService::class.java)
        }
    }
}