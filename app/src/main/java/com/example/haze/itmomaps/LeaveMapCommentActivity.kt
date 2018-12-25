package com.example.haze.itmomaps

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.haze.itmomaps.models.MapObject
import kotlinx.android.synthetic.main.activity_leave_comment.*


class LeaveMapCommentActivity : AppCompatActivity() {
    private lateinit var where: MapObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_comment)

        where = intent.getParcelableExtra("location")

        with(location) {
            this.text = where.title()
        }

    }

    fun postComment(view: View) {
        val author: String = author.text.toString()
        val comment: String = body.text.toString()
        val type: String = type.text.toString()
        // TODO post method for this shit
        // TODO if ok show it to user
        super.onBackPressed()
    }

}