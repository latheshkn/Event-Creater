package com.example.eventcreater.models.register

data class RegisterModel(
    val status:String,
    val message:String,
    val user:RegisterModelResponse
)
