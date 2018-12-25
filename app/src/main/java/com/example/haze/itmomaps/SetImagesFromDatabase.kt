package com.example.haze.itmomaps

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
                    Log.v("ololo", "except")
                }
            }
            return it.urls
        }
    }

    override fun onPostExecute(result: MutableList<String>?) {
        activity.get()?.let {
            it.getPictureWithGlide(it.floorPicker.value - 1)
        }
    }
}