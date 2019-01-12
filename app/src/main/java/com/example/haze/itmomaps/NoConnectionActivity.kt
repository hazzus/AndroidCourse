package com.example.haze.itmomaps

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class NoConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
    }

    fun getBack(view: View) {
        super.onBackPressed()
    }

    fun getOut(view: View) {
        this.finishAffinity()
    }
}
