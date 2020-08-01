package com.eyrafabdullayev.foursquareclone.adapter

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eyrafabdullayev.foursquareclone.R
import com.eyrafabdullayev.foursquareclone.model.PlaceModel
import kotlinx.android.synthetic.main.layout_places.view.*

class RecyclerViewAdapter(private val placeList: ArrayList<PlaceModel>, private val context: Activity, private val listener: Listener) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(place: PlaceModel)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            place: PlaceModel,
            position: Int,
            context: Activity,
            listener: Listener
        ) {
           val imageId = when (place.type) {
               "Bar" -> {
                   R.drawable.bar
               }
               "Restaurant" -> {
                   R.drawable.restaurant
               }
               else -> {
                   R.drawable.home
               }
           }
           val bitmap = BitmapFactory.decodeResource(context.resources,imageId)
           itemView.list_marker.setImageBitmap(bitmap)
           itemView.setOnClickListener {
               listener.onItemClick(place)
           }
           itemView.name.text = place.name
           itemView.type.text = place.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_places,parent,false)
        return RowHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(placeList[position],position,context,listener)
    }
}