package com.example.haze.itmomaps

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var currentBuilding: Building

    private var currentX: Float = 0.0f
    private var currentY: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java).apply {
                putExtra("building", buildingSelector.selectedItem.toString())
            }
            startActivity(intent)
        }

        val buildingNames = arrayOf("Kronv", "Lomo", "Grivc")
        // THIS TAKES REALLY FUCKING BIG MEMORY
        // TODO fix this to server download
        // TODO updating map from server
        currentBuilding = Building("EATMore",
                arrayOf(
                        R.drawable.floor1,
                        R.drawable.floor2,
                        R.drawable.floor3,
                        R.drawable.floor4,
                        R.drawable.floor5),
                5)


        floorPicker.min = 1
        floorPicker.max = currentBuilding.numberOfFloors
        if (savedInstanceState != null) {
            floorPicker.value = savedInstanceState.getInt("currentFloor")
        }
        floorPicker.setValueChangedListener { value: Int, _ ->
            val oldBitmap = (floorView.drawable as BitmapDrawable).bitmap
            oldBitmap.recycle()
            val newBitmap = BitmapFactory.decodeResource(resources, currentBuilding.floors[value - 1])
            floorView.setImageBitmap(newBitmap)
        }

        registerForContextMenu(floorView)

        PhotoViewAttacher(floorView).setOnPhotoTapListener { _, x, y ->
            currentX = x; currentY = y
            openContextMenu(floorView)
        }
        nav_view.setNavigationItemSelectedListener(this)

        val adapter = ArrayAdapter(this, R.layout.building_spinner_item, buildingNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingSelector.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        floorView.setImageBitmap(BitmapFactory.decodeResource(resources, currentBuilding.floors[floorPicker.value - 1]))
    }

    override fun onStop() {
        super.onStop()
        (floorView.drawable as BitmapDrawable).bitmap.recycle()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("currentFloor", floorPicker.value)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        // TODO truncate, not round
        var x: Int = (currentX * 100).toInt()
        var y: Int = (currentY * 100).toInt()
        // TODO get here the locationName, to pass it instead of Pair().toString()
        when (item!!.itemId) {
            // TODO get location name by coords
            // TODO get map Int by name of map
            R.id.comment -> {
                // This mades comments web larger then route
                x /= 4
                y /= 4
                val intent = Intent(this, LeaveMapCommentActivity::class.java).apply {
                    putExtra("x", x)
                    putExtra("y", y)
                    putExtra("floor", floorPicker.value)
                    putExtra("location", Pair(x, y).toString())
                    putExtra("map", 1)
                }
                startActivity(intent)
            }
            R.id.from -> {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    putExtra("building", buildingSelector.selectedItem.toString())
                    putExtra("from", Pair(x, y).toString())
                }
                startActivity(intent)
            }
            R.id.to -> {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    putExtra("building", buildingSelector.selectedItem.toString())
                    putExtra("to", Pair(x, y).toString())
                }
                startActivity(intent)
            }
            R.id.view -> {
                x /= 4
                y /= 4
                val intent = Intent(this, ShowMapCommentsActivity::class.java).apply {
                    putExtra("x", x)
                    putExtra("y", y)
                    putExtra("floor", floorPicker.value)
                    putExtra("location", Pair(x, y).toString())
                    putExtra("map", 1)
                }
                startActivity(intent)
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
