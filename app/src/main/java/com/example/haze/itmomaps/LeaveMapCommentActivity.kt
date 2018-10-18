package com.example.haze.itmomaps

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_leave_comment.*


class LeaveMapCommentActivity : AppCompatActivity() {
    lateinit var where : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_comment)

        where = intent.getStringExtra("where")

        with (location) {
            this.text = where
        }
    }

    fun postComment(view : View) {
        //TODO send comment to server
        super.onBackPressed()
    }

}