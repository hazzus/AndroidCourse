package com.example.haze.itmomaps

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.navigation.NavigationView
import com.travijuu.numberpicker.library.NumberPicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var floorView: PhotoView
    private lateinit var floorPicker: NumberPicker
    private lateinit var buildingSelector: Spinner
    private lateinit var currentBuilding: Building

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.v("123", "onCreate")
        fab.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java).apply {
                putExtra("building", buildingSelector.selectedItem.toString())
            }
            startActivity(intent)
        }

        val buildingNames = arrayOf("Kronv", "Lomo", "Grivc")
        // THIS TAKES REALLY FUCKING BIG MEMORY
        // TODO fix this to server download
        currentBuilding = Building("EATMore",
                arrayOf(
                        R.drawable.floor1,
                        R.drawable.floor2,
                        R.drawable.floor3,
                        R.drawable.floor4,
                        R.drawable.floor5),
                5)


        floorPicker = findViewById(R.id.number_picker)
        floorPicker.min = 1
        floorPicker.max = currentBuilding.numberOfFloors
        floorView = findViewById(R.id.photo_view)
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
        floorView.setOnLongClickListener { openContextMenu(it); true }

        nav_view.setNavigationItemSelectedListener(this)

        buildingSelector = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, buildingNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingSelector.adapter = adapter

    }

    override fun onPause() {
        super.onPause()
        Log.v("123", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.v("123", "onResume")
        floorView.setImageBitmap(BitmapFactory.decodeResource(resources, currentBuilding.floors[floorPicker.value - 1]))
    }

    override fun onStop() {
        super.onStop()
        Log.v("123", "onStop")
        (floorView.drawable as BitmapDrawable).bitmap.recycle()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.v("123", "onSave")
        outState?.putInt("currentFloor", floorPicker.value)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        // TODO change location and destination to coordinates ot map objects
        when (item!!.itemId) {
            R.id.comment -> {
                val intent = Intent(this, LeaveMapCommentActivity::class.java).apply {
                    putExtra("where", "LOCATION")
                }
                startActivity(intent)
            }
            R.id.from -> {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    putExtra("building", buildingSelector.selectedItem.toString())
                    putExtra("from", "LOCATION")
                }
                startActivity(intent)
            }
            R.id.to -> {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    putExtra("building", buildingSelector.selectedItem.toString())
                    putExtra("to", "DESTINATION")
                }
                startActivity(intent)
            }
            R.id.view -> {
                val intent = Intent(this, ShowMapCommentsActivity::class.java).apply {
                    putExtra("where", "LOCATION")
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
