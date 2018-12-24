package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.haze.itmomaps.models.MapObject
import kotlinx.android.synthetic.main.activity_route_view.*


class RouteActivity : AppCompatActivity() {

    private lateinit var buildingName: String
    private var buildingId: Int = 1
    private lateinit var fromView: TextView
    private lateinit var toView: TextView
    private var from: MapObject? = null
    private var to: MapObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)

        buildingName = intent.getStringExtra("buildingName")
        buildingId = intent.getIntExtra("buildingId", 1)

        fromView = findViewById(R.id.from)
        toView = findViewById(R.id.to)

        with(building_name) {
            this.text = buildingName
        }

        // TODO SELECTORS (NO OTHER WAY)
        from = intent.getParcelableExtra("from")
        if (from != null) {
            fromView.text = from!!.title()
        }

        to = intent.getParcelableExtra("to")
        if (to != null){
            toView.text = to!!.title()
        }
    }

    fun showRoutes(view: View) {
        from = MapObject(buildingName, buildingId, fromView.text.toString())
        to = MapObject(buildingName, buildingId, toView.text.toString())
        val intent = Intent(this, ShowRoutesActivity::class.java).apply {
            putExtra("from", from)
            putExtra("to", to)
        }
        startActivity(intent)
    }


    fun swapRoutes(view: View) {
        fromView.text = toView.text.toString() . also { toView.text = fromView.text.toString() }
        /*
        val from = fromView.text.toString()
        fromView.text = toView.text.toString()
        toView.text = from
        */
    }

}