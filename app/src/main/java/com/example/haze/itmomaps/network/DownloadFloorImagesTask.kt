package com.example.haze.itmomaps.network

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class DownloadFloorImagesTask(val map: Int) : AsyncTask<String, Int, MapView>() {


    override fun doInBackground(vararg params: String?): MapView {
        val url = URL("https://maps.brilzlian.me/v1/map/$map/")
        val response = url.openConnection().run {
            connect()
            publishProgress(10)
            val code = (this as? HttpURLConnection)?.responseCode

            Log.d("DownloadFloorImagesTask", "Loaded with code $code")

            if (code!! < 200 || code > 299) {
                return MapView()
            }
            getInputStream().bufferedReader()

        }
        publishProgress(50)
        val mapView: MapView = Gson().fromJson(response, MapView::class.java)
        Log.d("DownloadFloorImagesTask", url.toString())
        return mapView
    }
}