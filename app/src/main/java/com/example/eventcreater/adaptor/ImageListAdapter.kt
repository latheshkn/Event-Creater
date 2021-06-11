package com.example.eventcreater.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcreater.R
import com.example.eventcreater.models.imagelist.ImageListModel
import com.example.eventcreater.models.imagelist.ImageListModelResponse
import com.example.eventcreater.models.upcomingEvent.upComingEventListModel
import com.squareup.picasso.Picasso

class ImageListAdapter(val list:ArrayList<ImageListModelResponse>):RecyclerView.Adapter<ImageListAdapter.Vholder>() {


    class Vholder(itemView:View):RecyclerView.ViewHolder(itemView) {
      val  tvDescription=itemView.findViewById<TextView>(R.id.tvDescription)
      val  IvPicture=itemView.findViewById<ImageView>(R.id.IvPicture)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.picture_item_lsit,parent,false)
       return Vholder(view)
    }

    override fun onBindViewHolder(holder: Vholder, position: Int) {
       Picasso.get().load(list.get(position).event_image).into(holder.IvPicture)
        holder.tvDescription.text=list.get(position).image_description
    }

    override fun getItemCount(): Int {
       return list.size
    }
}