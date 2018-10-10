package com.example.haze.itmomaps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_leave_comment.*


class LeaveMapCommentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_comment)

        with (location) {
            this.text = intent.getStringExtra("where")
        }
    }

}