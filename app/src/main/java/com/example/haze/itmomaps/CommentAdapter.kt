package com.example.haze.itmomaps

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_view.view.*


class CommentViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
    val author : TextView = layout.author
    val title : TextView = layout.title
    val body : TextView = layout.body
    val type : TextView = layout.type
}

class CommentAdapter(private val allComments: List<Comment>) :
        RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        assert(this.allComments.isNotEmpty())
        val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_view, parent, false) as LinearLayout
        // set some parameters on view
        return CommentViewHolder(layout)
    }

    override fun getItemCount() = allComments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = allComments[position]
        holder.author.text = comment.author
        holder.title.text = comment.title
        holder.body.text = comment.body
        holder.type.text = comment.type
    }
}