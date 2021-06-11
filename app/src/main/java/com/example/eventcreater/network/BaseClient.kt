package com.example.eventcreater.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseClient {

    companion object {
        private const val BASE_URL = "http://192.168.43.236:8000/api/";

        fun <Api> buildApi(
            api: Class<Api>
        ): Api {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api)
        }
    }

}