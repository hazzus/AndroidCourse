package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_comments.*
import java.util.*

class ShowMapCommentsActivity : AppCompatActivity() {

    lateinit var where : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comments)

        val layoutManager = LinearLayoutManager(this)

        where = intent.getStringExtra("where")

        with (comment_recycler) {
            this.layoutManager = layoutManager
            adapter = CommentAdapter(getComments(where))
            setHasFixedSize(false)
        }

        with (location) {
            this.text = where
        }
    }

    private fun getComments(location : String) : List<Comment> {
        // TODO get comments from server
        val res = mutableListOf<Comment>()
        val rand = Random()
        repeat(20) {
            res.add(
                    when (rand.nextInt(4)) {
                        0 -> Comment("Peter Parker",
                                "I'm a spider-man",
                                "Watch me flying on the streets",
                                "Announcement",
                                "New-York, Earth 616")
                        1 -> Comment("Miles Morales",
                                "I'm a spider-kid",
                                "Nobody can beat me!",
                                "Announcement",
                                "New-York, Earth 1610")
                        2 -> Comment("J.J.Jameson",
                                "Spider-man is a criminal",
                                "He does no good",
                                "Urgent",
                                "Daily Bugle")
                        else -> Comment("Eddie Brock",
                                "We are VENOM",
                                "Spider-man ruined our lives",
                                "Warning",
                                "New-York, Earth 616")
                    }
            )
        }
        return res
    }

    fun leaveComment(view : View) {
        val intent = Intent(this, LeaveMapCommentActivity::class.java).apply {
            putExtra("where", where)
        }
        startActivity(intent)
    }
}