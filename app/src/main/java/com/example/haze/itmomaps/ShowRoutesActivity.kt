package com.example.haze.itmomaps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_routes.*

class ShowRoutesActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_routes)

        val layoutManager = LinearLayoutManager(this)

        with(routes_recycle) {
            this.layoutManager = layoutManager
            adapter = RouteAdapter(createRoutes(4, intent.extras))
            setHasFixedSize(true)
        }
    }

    private fun createRoutes(count: Int, extras: Bundle?) : List<Route> {
        // TODO rewrite for routes building algorithm
        // extras are from and to

        val res = mutableListOf<Route>()
        repeat(count) {
            if (extras != null) {
                res.add(Route(extras.get("from") as String, extras.get("to") as String))
            }
        }
        return res
    }
}