package com.example.eventcreater.preference

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceaManager(val context:Context) {

    lateinit var sharedPreference:SharedPreferences

    init {
        sharedPreference=context.getSharedPreferences("My_SharedPreference",Context.MODE_PRIVATE)
    }

    fun saveUser(id:String,email:String){

      val edit=  sharedPreference.edit()

        edit.putString("id",id)
        edit.putString("email",email)
        edit.commit()

    }

    fun getid():String{

        return sharedPreference.getString("id",null)!!
    }

    fun getEmail():String{

        return sharedPreference.getString("email","")!!
    }
}