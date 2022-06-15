package com.example.systemposfront.interfaces


import com.example.scannerapp.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


interface AccountEnd {
    companion object {

        private val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        var authToken:String?=null
        private var BASE_URL = "http://192.168.2.103:9099"

            val retrofit: Retrofit = Retrofit.Builder()
                .client( OkHttpClient.Builder()
                    .addInterceptor{
                     chain->   chain.proceed(chain.request().newBuilder().also {
                         it.addHeader("Authorization","Bearer $authToken")
                    }.build())
                    }
                    .also {
                        client ->if(BuildConfig.DEBUG){
                            val logging= HttpLoggingInterceptor()
                        logging.level = HttpLoggingInterceptor.Level.BODY
                        client.addInterceptor(logging)
                }
                        }.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()


    }




}