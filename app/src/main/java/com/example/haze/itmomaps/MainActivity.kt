package com.example.haze.itmomaps

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.LruCache
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
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var floorView: PhotoView
    private lateinit var floorPicker: com.shawnlin.numberpicker.NumberPicker
    private lateinit var buildingSelector: Spinner
    private lateinit var mMemoryCache: LruCache<String, Bitmap>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }

        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheMemory = maxMemory / 4
        mMemoryCache = object : LruCache<String, Bitmap>(cacheMemory) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
        val buildingNames = arrayOf("Kronv", "Lomo", "Grivc")
        // THIS TAKES REALLY FUCKING BIG MEMORY
        // TODO fix this to server download
        val currentBuilding = Building("EATMore",
                arrayOf(
                        R.drawable.floor1,
                        R.drawable.floor2,
                        R.drawable.floor3,
                        R.drawable.floor4,
                        R.drawable.floor5),
                5)

        floorView = findViewById(R.id.photo_view)
        floorPicker = findViewById(R.id.number_picker)
        floorPicker.minValue = 1
        floorPicker.maxValue = currentBuilding.numberOfFloors
        loadBitmap(currentBuilding.floors[floorPicker.value - 1], floorView)
        floorPicker.setOnValueChangedListener { _, _, newValue -> loadBitmap(currentBuilding.floors[newValue - 1], floorView) }
        registerForContextMenu(floorView)
        floorView.setOnLongClickListener { openContextMenu(it); true }
        nav_view.setNavigationItemSelectedListener(this)

        buildingSelector = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, buildingNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingSelector.adapter = adapter

    }

    fun loadBitmap(resId: Int, imageView: PhotoView) {
        val imageKey: String = resId.toString()

        val bitmap: Bitmap? = mMemoryCache[imageKey]?.also {
            imageView.setImageBitmap(it)
        } ?: run {
            imageView.setImageResource(resId)
            val task = BitmapWorkerTask()
            task.execute(resId)
            null
        }
    }

    private inner class BitmapWorkerTask : AsyncTask<Int, Unit, Bitmap>() {
        override fun doInBackground(vararg params: Int?): Bitmap? {
            return params[0]?.let { imageId ->
                compressBitmap(BitmapFactory.decodeResource(resources, imageId)).also { bitmap ->
                    mMemoryCache.put(imageId.toString(), bitmap)

                }
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.WEBP, 0, stream)
        val byteArray = stream.toByteArray()
        Log.v("123", byteArray.size.toString())
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
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

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.comment -> {
                startActivity(Intent(this, LeaveMapCommentActivity::class.java))
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
