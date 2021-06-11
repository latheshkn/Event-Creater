package com.example.eventcreater.models.upcomingEvent

data class upComingEventListModel(
    val id: String,
    val event_type: String,
    val event_creater: String,
    val place: String,
    val date:String,
    val time:String,
    val upcoming:String,
)
