package com.example.haze.itmomaps.network

import android.os.AsyncTask
import android.util.Log
import com.example.haze.itmomaps.MainActivity
import com.google.gson.Gson
import org.jetbrains.anko.db.insert
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class DownloadMapViewsTask(val activity: WeakReference<MainActivity>, val map: Int) : AsyncTask<String, Int, MapView>() {


    override fun doInBackground(vararg params: String?): MapView {
        val url = URL("https://maps.brilzlian.me/v1/map/$map/")
        val response = url.openConnection().run {
            connect()
            val code = (this as? HttpURLConnection)?.responseCode

            Log.d("DownloadMapViewsTask", "Loaded with code $code")

            if (code!! < 200 || code > 299) {
                return MapView()
            }
            getInputStream().bufferedReader()

        }
        val mapView: MapView = Gson().fromJson(response, MapView::class.java)
        Log.d("DownloadMapViewsTask", url.toString())
        return mapView
    }

    override fun onPostExecute(result: MapView?) {
        activity.get()?.let {
            val actvt = it
            result!!.pictures!!.forEach {
                actvt.db.insert("Pictures", "building" to Integer.valueOf(map), "floor" to Integer.valueOf(it.floor!!), "url" to it.url)
            }
        }
    }
}