package com.example.eventcreater.models.notification

data class NotificationModel(
    val multicast_id:String,
    val success:String,
    val failure:String,
    val canonical_ids:String,
    val results:ArrayList<NotificationModelResponse>
)
