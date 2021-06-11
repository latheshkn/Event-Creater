package com.example.eventcreater.models.register

data class RegisterModelResponse(
    val id:String,
    val email:String,
    val password:String,
    val otp:String,
)
