package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.api.objects.Room
import kotlinx.android.synthetic.main.activity_show_routes.*

class ShowRoutesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_routes)

        val layoutManager = LinearLayoutManager(this)

        with(routes_recycle) {
            this.layoutManager = layoutManager
            adapter = RouteAdapter(createRoutes(1, intent.extras), ::showRoute)
            setHasFixedSize(true)
        }
    }

    private fun createRoutes(count: Int, extras: Bundle?): List<Route> {
        val res = mutableListOf<Route>()
        repeat(count) {
            if (extras != null) {
                res.add(Route(extras.get("from") as Room, extras.get("to") as Room, extras.getInt("maxFloor")))
            }
        }
        return res
    }

    private fun showRoute(route : Route) {
        try {
            val path = route.getRoute().toTypedArray()
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("path", path)
            }
            startActivity(intent)
        } catch (error: NoSuchWayException) {
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            Log.e("Route", error.toString())
        }
    }
}