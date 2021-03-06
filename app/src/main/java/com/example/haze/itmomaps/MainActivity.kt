package com.example.haze.itmomaps

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.haze.itmomaps.api.MapsRepositoryProvider
import com.example.haze.itmomaps.api.objects.MapObject
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.floor_content.*
import org.jetbrains.anko.db.insert
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var urls: MutableList<String>
    lateinit var db: SQLiteDatabase
    private var currentX: Float = 0.0f
    private var currentY: Float = 0.0f
    private var path: Array<Parcelable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java).apply {
                putExtra("buildingId", buildingSelector.selectedItemPosition + 1)
            }
            startActivity(intent)
        }

        val buildingNames = arrayOf("Kronverksky", "Lomonosova", "Grivcova")

        // TODO update map on buldingSelector value changed!
        floorPicker.min = 1
        floorPicker.max = 5
        if (savedInstanceState != null) {
            floorPicker.value = savedInstanceState.getInt("currentFloor")
        }

        floorPicker.setValueChangedListener { value: Int, _ ->
            getPictureWithGlide(value - 1)
        }

        path = intent.getParcelableArrayExtra("path")
        if (path != null) {
            val first = path!![0]
            if (first is MapObject)
                floorPicker.value = first.floor!!
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
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        //TODO add InternetConnection Listener
        db = MyDatabaseOpenHelper(applicationContext).writableDatabase
        urls = mutableListOf()
        if (isConnected) {
            for (i in 1..1) {
                val api = MapsRepositoryProvider.provideMapRepository()
                api.getMap(i)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe( { result ->
                            result.pictures!!.forEach {
                                db.insert("Pictures", "building" to Integer.valueOf(i), "floor" to Integer.valueOf(it.floor!!), "url" to it.url)
                            }
                        }, { error ->
                            Log.e("API.getMap", error.localizedMessage)
                            Toast.makeText(applicationContext, "No possibility to update map", Toast.LENGTH_LONG).show()
                        })
                //DownloadMapViewsTask(WeresourcesakReference(this), i).execute()
            }
        }

        SetImagesFromDatabase(WeakReference(this)).execute()

    }

    private fun drawTheWay(resource: BitmapDrawable): BitmapDrawable {
        val bm = resource.bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
        bm.prepareToDraw()
        val canvas = Canvas(bm)
        val paint = Paint()
        val blockWidth = canvas.width / 100F
        val blockHeight = canvas.height / 100F
        val radius = 0.5F * if (blockWidth > blockHeight) blockHeight else blockWidth
        for (obj in this.path!!) {
            if (obj is MapObject)
                if (obj.floor!! == floorPicker.value) {
                    val drawX = (obj.x!! + 0.5F) * blockWidth
                    val drawY = (obj.y!! + 0.5F) * blockHeight
                    canvas.drawCircle(drawX, drawY, radius, paint)
                }
        }
        return BitmapDrawable(resources, bm)
    }


    fun getPictureWithGlide(i: Int) {
        Glide.with(floorView)
                .load(urls[i])
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(object : SimpleTarget<Drawable>() {
                    //I have no idea why it doesnt work other way
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        var res = resource as BitmapDrawable
                        if (path != null)
                            res = drawTheWay(res)
                        floorView.setImageDrawable(res as Drawable)
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
        val position = MapObject(
                floorPicker.value,
                (currentX * 100).toInt(),
                (currentY * 100).toInt()
        )
        when (item!!.itemId) {
            R.id.comment -> {
                position.convertToComment()
                val intent = Intent(this, LeaveMapCommentActivity::class.java).apply {
                    putExtra("buildingId", buildingSelector.selectedItemPosition + 1)
                    putExtra("location", position)
                }
                startActivity(intent)
            }
            R.id.view -> {
                position.convertToComment()
                val intent = Intent(this, ShowMapCommentsActivity::class.java).apply {
                    putExtra("buildingId", buildingSelector.selectedItemPosition + 1)
                    putExtra("location", position)
                }
                startActivity(intent)
            }
            R.id.from -> {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    putExtra("buildingId", buildingSelector.selectedItemPosition + 1)
                    putExtra("from", position)
                }
                startActivity(intent)
            }
            R.id.to -> {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    putExtra("buildingId", buildingSelector.selectedItemPosition + 1)
                    putExtra("to", position)
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
