package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.haze.itmomaps.models.MapObject
import kotlinx.android.synthetic.main.activity_route_view.*


class RouteActivity : AppCompatActivity() {

    private lateinit var buildingName: String
    private var buildingId: Int = 1
    private lateinit var fromView: TextView
    private lateinit var toView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)

        buildingName = intent.getStringExtra("buildingName")
        buildingId = intent.getIntExtra("buildingId", 1)

        with(building_name) {
            this.text = buildingName
        }

        val allObjects = getMapObjects()

        val adapter = ArrayAdapter(this, R.layout.map_object_spinner_item, allObjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        from.adapter = adapter
        to.adapter = adapter

        // setting selectors to chosen value
        val fromObject: MapObject?= intent.getParcelableExtra("to")
        val toObject: MapObject? = intent.getParcelableExtra("from")

        if (fromObject != null) {
            val index = allObjects.indexOf(fromObject)
            from.setSelection(if (index >= 0) index else 0)
        }
        if (toObject != null) {
            val index = allObjects.indexOf(toObject)
            to.setSelection(if (index >= 0) index else 0)
        }
    }

    private fun getMapObjects() : Array<MapObject> {
        // TODO (NETWORK) implement this network messages
        return arrayOf(
                MapObject(buildingName, buildingId, 0, 0, 1),
                MapObject(buildingName, buildingId, 10, 10, 1),
                MapObject(buildingName, buildingId, 40, 40, 3)
        )
    }

    fun showRoutes(view: View) {
        val intent = Intent(this, ShowRoutesActivity::class.java).apply {
            putExtra("from", from.selectedItem as MapObject)
            putExtra("to", to.selectedItem as MapObject)
        }
        startActivity(intent)
    }


    fun swapRoutes(view: View) {
        val fromPos = from.selectedItemPosition
        val toPos = to.selectedItemPosition
        from.setSelection(toPos)
        to.setSelection(fromPos)
    }

}