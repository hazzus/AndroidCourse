package com.example.haze.itmomaps

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.haze.itmomaps.network.DownloadMapViewsTask
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var currentBuilding: Building
    private lateinit var urls: MutableList<String>
    private lateinit var db: SQLiteDatabase
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
        val buildingNames = arrayOf("Kronverksky", "Lomonosova", "Grivcova")

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        //TODO add InternetConnection Listener
        db = MyDatabaseOpenHelper(applicationContext).writableDatabase
        if (isConnected) {
            for (i in 1..1) {
                val mapView = DownloadMapViewsTask(i).execute().get()
                mapView.pictures!!.forEach {
                    db.insert("Pictures", "building" to Integer.valueOf(i), "floor" to Integer.valueOf(it.floor!!), "url" to it.url)
                }
            }
        }
        urls = mutableListOf()
        for (i in 1..5) {
            urls.add(db.select("Pictures", "url").whereSimple("(building = 1) and (floor = $i)").parseSingle(StringParser).toString())
        }
        Log.d("ololo", urls.toString())
        floorPicker.min = 1
        floorPicker.max = 5
        if (savedInstanceState != null) {
            floorPicker.value = savedInstanceState.getInt("currentFloor")
        }
        floorPicker.setValueChangedListener { value: Int, _ ->
            Glide.with(floorView)
                    .load(urls[value - 1])
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(object : SimpleTarget<Drawable>() {
                        //I have no idea why it isnt working other way
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            floorView.setImageDrawable(resource)
                        }
                    })
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
        floorPicker
        Glide.with(floorView)
                .load(urls[floorPicker.value - 1])
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(object : SimpleTarget<Drawable>() {
                    //I have no idea why it isnt working other way
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        floorView.setImageDrawable(resource)
                    }
                })
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
