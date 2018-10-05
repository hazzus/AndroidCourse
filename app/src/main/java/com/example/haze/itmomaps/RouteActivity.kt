package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText

class RouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)
    }

    fun showRoutes(view: View) {
        val from = findViewById<EditText>(R.id.from).text.toString()
        val to = findViewById<EditText>(R.id.to).text.toString()
        val intent = Intent(this, ShowRoutesActivity::class.java)
        startActivity(intent)
    }

}