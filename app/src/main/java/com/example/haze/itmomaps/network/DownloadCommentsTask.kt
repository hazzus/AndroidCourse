package com.example.haze.itmomaps.network

import android.os.AsyncTask
import android.util.Log
import com.example.haze.itmomaps.ShowMapCommentsActivity
import com.example.haze.itmomaps.models.ReceivedComment
import com.example.haze.itmomaps.models.MapObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_show_comments.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class DownloadCommentsTask(val activity: WeakReference<ShowMapCommentsActivity>, val location: MapObject) : AsyncTask<String, Int, ArrayList<ReceivedComment>>() {
    override fun doInBackground(vararg params: String?): ArrayList<ReceivedComment> {
        val res = ArrayList<ReceivedComment>()
        publishProgress(0)
        val url = URL("https://maps.brilzlian.me/v1/receivedComments/${location.toWebString()}")
        val response = url.openConnection().run {
            connect()
            publishProgress(10)
            val code = (this as? HttpURLConnection)?.responseCode

            Log.d("DownloadCommentsTask", "Loaded with code $code")

            if (code!! < 200 || code > 299) {
                return res
            }
            getInputStream().bufferedReader()

        }
        publishProgress(50)
        val receivedComments : Array<ReceivedComment> = Gson().fromJson(response, Array<ReceivedComment>::class.java)
        res.addAll(receivedComments)
        Log.d("DownloadCommentsTask", "Res $res")
        return res
    }

    override fun onPostExecute(result: ArrayList<ReceivedComment>?) {
        Log.d("DownloadCommentsTask", "Post execute ran")
        activity.get()?.let {
            it.receivedComments.addAll(result!!)
            val l = it.receivedComments
            Log.d("DownloadCommentsTask", "it receivedComments written = $l")
            it.comment_recycler.adapter?.notifyDataSetChanged()
            Log.d("DownloadCommentsTask", "notified")
        }
    }
}