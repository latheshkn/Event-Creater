package com.example.eventcreater.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcreater.R
import com.example.eventcreater.models.upcomingEvent.upComingEventListModel

class UpcomingListAdapter(val list:ArrayList<upComingEventListModel>,val click:SendOnclikLisner):RecyclerView.Adapter<UpcomingListAdapter.Vholder>() {


    class Vholder(itemView:View):RecyclerView.ViewHolder(itemView) {
      val  categoryName=itemView.findViewById<TextView>(R.id.categoryName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.event_list_item,parent,false)
       return Vholder(view)
    }

    override fun onBindViewHolder(holder: Vholder, position: Int) {
       holder.categoryName.text=list.get(position).event_type
        holder.itemView.setOnClickListener({
            click.onClickFromAdapter(position)
        })
    }

    override fun getItemCount(): Int {
       return list.size
    }

    interface SendOnclikLisner{
        fun onClickFromAdapter(position:Int)
    }
}