package com.example.haze.itmomaps

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.haze.itmomaps.api.MapsRepositoryProvider
import com.example.haze.itmomaps.api.objects.MapObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_leave_comment.*


class LeaveMapCommentActivity : AppCompatActivity() {
    private lateinit var where: MapObject
    private var buildingId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_comment)

        where = intent.getParcelableExtra("location")
        buildingId = intent.getIntExtra("buildingId", 1)

        with(location) {
            this.text = where.toString()
        }

    }

    fun postComment(view: View) {
        val author: String = author.text.toString()
        val comment: String = body.text.toString()
        val type: String = type.text.toString()
        val api = MapsRepositoryProvider.provideMapRepository()
        api.postComment(buildingId, where.floor!!, where.x!!, where.y!!, author, comment, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        Toast.makeText(applicationContext, "Posted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("postComment", result.code().toString())
                        Toast.makeText(applicationContext, "Sorry, your comment not posted", Toast.LENGTH_SHORT).show()
                    }
                }, { error ->
                    Log.e("postComment", error.localizedMessage)
                    Toast.makeText(applicationContext, "Sorry, your comment not posted", Toast.LENGTH_SHORT).show()
                })
        super.onBackPressed()
    }

}