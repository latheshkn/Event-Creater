package com.example.eventcreater.models.upcomingEvent

data class UpcomingEventList(
    val status:String,
    val message:String,
    val data:ArrayList<upComingEventListModel>
)
