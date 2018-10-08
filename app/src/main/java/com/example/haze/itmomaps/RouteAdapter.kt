package com.example.haze.itmomaps

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.route_text_view.view.*

class RouteViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
    val routeTitle: TextView = layout.route_title
}

class RouteAdapter(private val allRoutes : List<Route>) :
        RecyclerView.Adapter<RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_text_view, parent, false) as LinearLayout
        // set some parameters on view
        return RouteViewHolder(layout)
    }

    override fun getItemCount() = allRoutes.size

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.routeTitle.text = allRoutes[position].title()
    }
}