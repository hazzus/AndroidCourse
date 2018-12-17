package com.example.haze.itmomaps

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.route_text_view.view.*

class RouteViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
    val routeTitle: TextView = layout.route_title

    fun bind(route : Route, clickListener: (Route) -> Unit) {
        layout.setOnClickListener { clickListener(route) }
    }
}

class RouteAdapter(private val allRoutes: List<Route>, private val clickListener : (Route) -> Unit) :
        RecyclerView.Adapter<RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        assert(this.allRoutes.isNotEmpty())
        val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_text_view, parent, false) as LinearLayout
        return RouteViewHolder(layout)
    }

    override fun getItemCount() = allRoutes.size

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.routeTitle.text = allRoutes[position].title() //.getRoute().joinToString(",", transform = {i -> i.toString()})
        holder.bind(allRoutes[position], clickListener)
    }
}