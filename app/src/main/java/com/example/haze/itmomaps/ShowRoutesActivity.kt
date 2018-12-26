package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.models.MapObject
import com.example.haze.itmomaps.models.NoSuchWayException
import com.example.haze.itmomaps.models.Route
import com.example.haze.itmomaps.models.WrongMappingException
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
                try {
                    res.add(Route(extras.get("from") as MapObject, extras.get("to") as MapObject))
                } catch (error: WrongMappingException) {
                    Toast.makeText(applicationContext, "Can't build routes between different buildings", Toast.LENGTH_LONG).show()
                    Log.e("Route", error.toString())
                }
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