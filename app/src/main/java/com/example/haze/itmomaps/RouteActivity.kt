package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_route_view.*
import org.w3c.dom.Text

class RouteActivity : AppCompatActivity() {

    private lateinit var building : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)

        building = intent.getStringExtra("building")

        with(building_name) {
            this.text = building
        }

        val from = intent.getStringExtra("from")
        if (from != null) findViewById<TextView>(R.id.from).text = from

        val to = intent.getStringExtra("to")
        if (to != null) findViewById<TextView>(R.id.to).text = to
    }

    fun showRoutes(view: View) {
        val from = findViewById<EditText>(R.id.from).text.toString()
        val to = findViewById<EditText>(R.id.to).text.toString()
        val intent = Intent(this, ShowRoutesActivity::class.java).apply {
            putExtra("from", from)
            putExtra("to", to)
            putExtra("building", building)
        }
        startActivity(intent)
    }


    fun swapRoutes(view: View) {
        val from = findViewById<TextView>(R.id.from).text.toString()
        findViewById<TextView>(R.id.from).text = findViewById<TextView>(R.id.to).text.toString()
        findViewById<TextView>(R.id.to).text = from
    }

}