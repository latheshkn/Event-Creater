package com.example.eventcreater.network

import com.example.eventcreater.models.notification.NotificationModel
import com.example.eventcreater.models.CommonModel
import com.example.eventcreater.models.imagelist.ImageListModel
import com.example.eventcreater.models.register.RegisterModel
import com.example.eventcreater.models.upcomingEvent.UpcomingEventList
import com.example.eventcreater.models.upcomingEvent.upComingEventListModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<CommonModel>

    @FormUrlEncoded
    @POST("register")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterModel>

    @Multipart
    @POST("uploadSelfie")
    suspend fun uploadImage(
        @Part profileImg: MultipartBody.Part,
        @Part("event_id") event_id: RequestBody,
        @Part("description") description: RequestBody,
    ): Response<CommonModel>


    @FormUrlEncoded
    @POST("sendNotification")
    suspend fun sendNotification(
        @Field("token") token: String,
        @Field("body") body: String,
        @Field("title") title: String,
        @Field("creater") event_creater: String,
        @Field("place") place: String,
        @Field("date") date: String,
    ): Response<NotificationModel>


    @GET("upcomingEvent")
    suspend fun upComingEventList(
    ): Response<UpcomingEventList>

    @GET("getEventImages")
    suspend fun getImageList(
        @Query("event_id") event_id: String
    ): Response<ImageListModel>

    @GET("pendingEventList")
    suspend fun getPendingEvnts(
    ): Response<UpcomingEventList>

    @FormUrlEncoded
    @POST("insertDecline")
    suspend fun postDecline(
        @Field("event_id") event_id:String,
        @Field("user_id") user_id:String,
    ): Response<CommonModel>

 @FormUrlEncoded
    @POST("insertAccept")
    suspend fun postAccepted(
        @Field("event_id") event_id:String,
        @Field("user_id") user_id:String,
    ): Response<CommonModel>

}