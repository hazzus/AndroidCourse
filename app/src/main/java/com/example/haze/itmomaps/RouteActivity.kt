package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.haze.itmomaps.api.MapsRepositoryProvider
import com.example.haze.itmomaps.api.objects.Map
import com.example.haze.itmomaps.api.objects.MapObject
import com.example.haze.itmomaps.api.objects.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_route_view.*


class RouteActivity : AppCompatActivity() {

    private var buildingId: Int = 1
    private var maxFloor: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)

        buildingId = intent.getIntExtra("buildingId", 1)

        // setting selectors to chosen value

        val api = MapsRepositoryProvider.provideMapRepository()
        api.getMap(buildingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(::onObjectsGot) { error ->
                    Log.e("getMap", error.localizedMessage)
                    //TODO show it to user
                }
    }

    private fun onObjectsGot(result: Map) {
        val fromObject: MapObject?= intent.getParcelableExtra("to")
        val toObject: MapObject? = intent.getParcelableExtra("from")

        val adapter = ArrayAdapter(this, R.layout.map_object_spinner_item, result.objects!!)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        from.adapter = adapter
        to.adapter = adapter

        if (fromObject != null) {
            from.setSelection(indexOfRoom(result.objects, fromObject))
        }
        if (toObject != null) {
            to.setSelection(indexOfRoom(result.objects, toObject))
        }
        with(building_name) {
            this.text = result.name
        }
        maxFloor = result.pictures!!.size
    }

    private fun indexOfRoom(rooms: Array<Room>, find: MapObject): Int {
        for (i in 0..rooms.size) {
            if (rooms[i].coordinates!!.squares!!.contains(find)) {
                return i
            }
        }
        return 0
    }

    fun showRoutes(view: View) {
        val intent = Intent(this, ShowRoutesActivity::class.java).apply {
            putExtra("from", from.selectedItem as Room)
            putExtra("to", to.selectedItem as Room)
            putExtra("maxFloor", maxFloor)
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