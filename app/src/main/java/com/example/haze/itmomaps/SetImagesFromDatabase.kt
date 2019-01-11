package com.example.haze.itmomaps

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.AsyncTask
import android.util.Log
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.select
import java.lang.ref.WeakReference

class SetImagesFromDatabase(val activity: WeakReference<MainActivity>) : AsyncTask<String, Unit, MutableList<String>>() {

    override fun doInBackground(vararg params: String?): MutableList<String> {
        activity.get()?.let {
            for (i in 1..5) {
                try {
                    it.urls.add(it.db.select("Pictures", "url").whereSimple("(building = 1) and (floor = $i)").parseSingle(StringParser).toString())
                } catch (e: SQLiteException) {
                    Log.v("ololo", "except sqlite shittt")
                }
            }
            return it.urls
        }
    }

    override fun onPostExecute(result: MutableList<String>?) {
        if (result?.size == 0) {
            activity.get()?.startActivity(Intent(activity.get(), NoConnectionActivity::class.java))
            return
        }
        activity.get()?.let {
            try {
                it.getPictureWithGlide(it.floorPicker.value - 1)
            } catch (e: ArrayIndexOutOfBoundsException) {
                Log.v("ololo", "catched at task")
                throw e
            }
        }
    }
}