package com.example.haze.itmomaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_route_view.*


class RouteActivity : AppCompatActivity() {

    private lateinit var building : String
    private lateinit var from : TextView
    private lateinit var to : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)

        from = findViewById<TextView>(R.id.from)
        to = findViewById<TextView>(R.id.to)
        building = intent.getStringExtra("building")

        with(building_name) {
            this.text = building
        }

        val from = intent.getStringExtra("from")
        if (from != null) this.from.text = from

        val to = intent.getStringExtra("to")
        if (to != null) this.to.text = to
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
        val from = this.from.text.toString()
        this.from.text = this.to.text.toString()
        this.to.text = from
    }

}