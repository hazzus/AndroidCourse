package com.example.haze.itmomaps

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.models.MapObject
import com.example.haze.itmomaps.network.CommentView
import com.example.haze.itmomaps.network.DownloadCommentsTask
import kotlinx.android.synthetic.main.activity_show_comments.*
import java.lang.ref.WeakReference
import java.util.*

class ShowMapCommentsActivity : AppCompatActivity() {

    lateinit var where: MapObject
    var comments: ArrayList<CommentView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comments)

        val layoutManager = LinearLayoutManager(this)


        where = intent.getParcelableExtra("location")

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (savedInstanceState?.containsKey("comments") != null) {
            comments = savedInstanceState.getParcelableArrayList("comments")!!
        } else if (isConnected) {
            //TODO (UI) let user know that there is no possibility to download comments
            DownloadCommentsTask(WeakReference(this), where).execute()
        }

        with(comment_recycler) {
            this.layoutManager = layoutManager
            adapter = CommentAdapter(comments)
            setHasFixedSize(false)
        }

        with(location) {
            this.text = where.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d("ShowMapCommentsActivity", "save() called")
        if (comments.isNotEmpty()) {
            outState?.putParcelableArrayList("comments", comments)
        }
    }

    fun leaveComment(view: View) {
        val intent = Intent(this, LeaveMapCommentActivity::class.java).apply {
            putExtra("location", where)
        }
        startActivity(intent)
    }
}