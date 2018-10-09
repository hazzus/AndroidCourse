package com.example.haze.itmomaps

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var floorView: PhotoView
    private lateinit var floorPicker: com.shawnlin.numberpicker.NumberPicker
    private lateinit var buildingSelector: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }

        val buildingNames = arrayOf("Kronv", "Lomo", "Grivc")
        // THIS TAKES REALLY FUCKING BIG MEMORY
        // TODO fix this to server download
        val currentBuilding = Building("EATMore",
                arrayOf(
                        BitmapFactory.decodeResource(resources, R.drawable.floor1),
                        BitmapFactory.decodeResource(resources, R.drawable.floor2),
                        BitmapFactory.decodeResource(resources, R.drawable.floor3),
                        BitmapFactory.decodeResource(resources, R.drawable.floor4),
                        BitmapFactory.decodeResource(resources, R.drawable.floor5)),
                5)

        floorView = findViewById(R.id.photo_view)
        floorPicker = findViewById(R.id.number_picker)
        floorPicker.minValue = 1
        floorPicker.maxValue = currentBuilding.numberOfFloors
        floorView.setImageBitmap(currentBuilding.floors[floorPicker.value - 1])
        floorPicker.setOnValueChangedListener { _, _, newValue -> floorView.setImageBitmap(currentBuilding.floors[newValue - 1]) }
        registerForContextMenu(floorView)
        floorView.setOnLongClickListener { openContextMenu(it); true }
        nav_view.setNavigationItemSelectedListener(this)

        buildingSelector = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, buildingNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingSelector.adapter = adapter

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("currentFloor", floorPicker.value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            floorPicker.value = savedInstanceState.getInt("currentFloor")
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
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
