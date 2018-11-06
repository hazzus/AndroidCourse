package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haze.itmomaps.network.CommentView
import com.example.haze.itmomaps.network.DownloadCommentsTask
import kotlinx.android.synthetic.main.activity_show_comments.*
import java.lang.ref.WeakReference
import java.util.*

class ShowMapCommentsActivity : AppCompatActivity() {

    var x: Int = 0
    var y: Int = 0
    var floor: Int = 0
    lateinit var map: String
    var comments: ArrayList<CommentView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comments)

        val layoutManager = LinearLayoutManager(this)

        x = intent.getIntExtra("x", 0)
        y = intent.getIntExtra("y", 0)
        map = intent.getStringExtra("map")
        floor = intent.getIntExtra("floor", 1)

        if (savedInstanceState?.containsKey("comments") != null) {
            comments = savedInstanceState.getParcelableArrayList("comments")!!
        } else {
            DownloadCommentsTask(WeakReference(this)).execute()
        }

        with(comment_recycler) {
            this.layoutManager = layoutManager
            adapter = CommentAdapter(comments)
            setHasFixedSize(false)
        }

        with(location) {
            this.text = map
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
            putExtra("where", "nowhere")
        }
        startActivity(intent)
    }
}