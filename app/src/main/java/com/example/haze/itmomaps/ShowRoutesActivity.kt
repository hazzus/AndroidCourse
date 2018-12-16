package com.example.haze.itmomaps

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.R.id.routes_recycle
import kotlinx.android.synthetic.main.activity_show_routes.*

class ShowRoutesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_routes)

        val layoutManager = LinearLayoutManager(this)

        with(routes_recycle) {
            this.layoutManager = layoutManager
            adapter = RouteAdapter(createRoutes(1, intent.extras))
            setHasFixedSize(true)
        }
    }

    private fun createRoutes(count: Int, extras: Bundle?): List<Route> {
        // TODO rewrite for routes building algorithm
        // extras are from and to

        val res = mutableListOf<Route>()
        repeat(count) {
            if (extras != null) {
                res.add(Route(extras.get("from") as String,
                        extras.get("to") as String,
                        extras.get("building") as String))
            }
        }
        return res
    }

    fun showRoute(view: View) {
        Log.d("suka", "suka")
    }
}