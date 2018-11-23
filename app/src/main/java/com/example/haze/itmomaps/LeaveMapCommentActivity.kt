package com.example.haze.itmomaps

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_leave_comment.*


class LeaveMapCommentActivity : AppCompatActivity() {
    lateinit var locationName: String
    var x: Int = 0
    var y: Int = 0
    var floor: Int = 0
    var map: Int = 0


    private lateinit var author : TextView
    private lateinit var comment : TextView
    private lateinit var type : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_comment)

        locationName = intent.getStringExtra("location")
        x = intent.getIntExtra("x", 0)
        y = intent.getIntExtra("y", 0)
        floor = intent.getIntExtra("floor", 1)
        map = intent.getIntExtra("map", 1)

        author =  findViewById<TextView>(R.id.author)
        comment = findViewById<TextView>(R.id.body)
        type = findViewById<TextView>(R.id.type)

        with(location) {
            this.text = locationName
        }

    }

    fun postComment(view: View) {
        val author: String = this.author.text.toString()
        val comment: String = this.comment.text.toString()
        val type: String = this.type.text.toString()
        // TODO fix this
        Fuel.post("https://maps.brilzlian.me/v1/comments/$map/$floor/$x/$y")
                .jsonBody(
                        """
                        {
                            "name": "$author",
                            "comment": "$comment",
                            "type": "$type"
                        }
                        """
                ).also { Log.d("LeaveMapCommentActivity", it.toString()) }
                .response {
                    request, response, result ->
                    Log.d("LeaveMapCommentActivity", result.toString())
                }
        // TODO if ok show it to user
        super.onBackPressed()
    }

}