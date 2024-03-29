package com.example.scannerapp.interfaces


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AuthenEndReq {
    companion object {

        private val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

         private var BASE_URL = "http://192.168.86.32:8080"
       // private var BASE_URL = "http://192.168.86.31:8080"
       // private var BASE_URL = "http://192.168.2.103:9090"
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()


    }


}