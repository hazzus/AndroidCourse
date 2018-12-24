package com.example.haze.itmomaps

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.haze.itmomaps.models.MapObject
import kotlinx.android.synthetic.main.activity_leave_comment.*


class LeaveMapCommentActivity : AppCompatActivity() {
    private lateinit var where: MapObject

    private lateinit var author : TextView
    private lateinit var comment : TextView
    private lateinit var type : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_comment)

        where = intent.getParcelableExtra("location")

        author =  findViewById<TextView>(R.id.author)
        comment = findViewById<TextView>(R.id.body)
        type = findViewById<TextView>(R.id.type)

        with(location) {
            this.text = where.title()
        }

    }

    fun postComment(view: View) {
        val author: String = this.author.text.toString()
        val comment: String = this.comment.text.toString()
        val type: String = this.type.text.toString()
        // TODO post method for this shit
        // TODO if ok show it to user
        super.onBackPressed()
    }

}