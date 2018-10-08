package com.example.haze.itmomaps

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_routes.*

class ShowRoutesActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_show_routes)

        val layoutManager = LinearLayoutManager(this)

        with(routes_recycle) {
            this.layoutManager = layoutManager
            adapter = RouteAdapter(createRoutes(3))
            setHasFixedSize(true)
        }
    }

    private fun createRoutes(count: Int) : List<Route> {
        val res = mutableListOf<Route>()
        repeat(count) {
            res.add(Route())
        }
        return res
    }
}