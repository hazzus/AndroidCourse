package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.models.MapObject
import com.example.haze.itmomaps.models.Route
import kotlinx.android.synthetic.main.activity_show_routes.*

class ShowRoutesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_routes)

        val layoutManager = LinearLayoutManager(this)

        with(routes_recycle) {
            this.layoutManager = layoutManager
            adapter = RouteAdapter(createRoutes(1, intent.extras)) { i -> showRoute(i)}
            setHasFixedSize(true)
        }
    }

    private fun createRoutes(count: Int, extras: Bundle?): List<Route> {
        val res = mutableListOf<Route>()
        repeat(count) {
            if (extras != null) {
                res.add(Route(extras.get("from") as MapObject, extras.get("to") as MapObject))
            }
        }
        return res
    }

    private fun showRoute(route : Route) {
        val path = route.getRoute().toTypedArray()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("path", path)
        }
        startActivity(intent)
    }
}