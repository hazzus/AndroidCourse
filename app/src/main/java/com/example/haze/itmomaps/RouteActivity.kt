package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)
    }

    fun showRoutes(view: View) {
        val from = findViewById<EditText>(R.id.from).text.toString()
        val to = findViewById<EditText>(R.id.to).text.toString()
        val intent = Intent(this, ShowRoutesActivity::class.java).apply {
            putExtra("from", from)
            putExtra("to", to)
        }
        startActivity(intent)
    }


    fun swapRoutes(view: View) {
        val from = findViewById<TextView>(R.id.from).text.toString()
        findViewById<TextView>(R.id.from).text = findViewById<TextView>(R.id.to).text.toString()
        findViewById<TextView>(R.id.to).text = from
    }

}