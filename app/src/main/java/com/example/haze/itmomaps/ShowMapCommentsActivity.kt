package com.example.haze.itmomaps

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.api.MapsRepositoryProvider
import com.example.haze.itmomaps.api.objects.MapObject
import com.example.haze.itmomaps.api.objects.ReceivedComment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_show_comments.*
import java.util.*

class ShowMapCommentsActivity : AppCompatActivity() {

    lateinit var where: MapObject
    private var buildingId: Int = 1
    var receivedComments: ArrayList<ReceivedComment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comments)

        val layoutManager = LinearLayoutManager(this)

        where = intent.getParcelableExtra("location")
        buildingId = intent.getIntExtra("buildingId", 1)


        if (savedInstanceState?.containsKey("comments") != null) {
            receivedComments = savedInstanceState.getParcelableArrayList("comments")!!
        }

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (savedInstanceState?.containsKey("receivedComments") != null) {
            receivedComments = savedInstanceState.getParcelableArrayList("receivedComments")!!
        } else if (isConnected) {
            val api = MapsRepositoryProvider.provideMapRepository()
            api.getComments(buildingId, where.floor!!, where.x!!, where.y!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        with(comment_recycler) {
                            this.layoutManager = layoutManager
                            adapter = CommentAdapter(result)
                            setHasFixedSize(true)
                        }
                    }, { error ->
                        Toast.makeText(applicationContext, "Something bad happened: ${error.message}", Toast.LENGTH_SHORT).show()
                    })
        } else {
            startActivity(Intent(this, NoConnectionActivity::class.java))
        }

        with(comment_recycler) {
            this.layoutManager = layoutManager
            adapter = CommentAdapter(receivedComments)
            setHasFixedSize(false)
        }

        with(location) {
            this.text = where.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            val api = MapsRepositoryProvider.provideMapRepository()
            api.getComments(buildingId, where.floor!!, where.x!!, where.y!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        with(comment_recycler) {
                            this.layoutManager = layoutManager
                            adapter = CommentAdapter(result)
                            setHasFixedSize(true)
                        }
                    }, { error ->
                        Toast.makeText(applicationContext, "Something bad happened: ${error.message}", Toast.LENGTH_SHORT).show()
                    })
        } else {
            startActivity(Intent(this, NoConnectionActivity::class.java))
        }
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d("ShowMapCommentsActivity", "save() called")
        if (receivedComments.isNotEmpty()) {
            outState?.putParcelableArrayList("receivedComments", receivedComments)
        }
    }

    fun leaveComment(view: View) {
        val intent = Intent(this, LeaveMapCommentActivity::class.java).apply {
            putExtra("location", where)
            putExtra("buildingID", buildingId)
        }
        startActivity(intent)
    }
}