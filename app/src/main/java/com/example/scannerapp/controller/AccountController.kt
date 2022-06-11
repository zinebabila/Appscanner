package com.example.systemposfront.controller


import android.util.Config
import com.example.scanner.bo.Account
import com.example.scanner.bo.Customer
import com.example.scanner.bo.JwtRequest
import com.example.scanner.bo.JwtResponse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*

interface AccountController {

    @POST("/authenticate")
     fun login(@Body post: JwtRequest) : Call<JwtResponse>

    @GET("/Account/getOne/{id}")
    fun getAccount(@Path("id") id:Long): Call<Account>

    @Multipart
    @PUT("/Account/update")
     fun ModiffierAccount(@Part("account")id: RequestBody,
                          @Part("first")first: RequestBody,
                          @Part("last")last: RequestBody,
                          @Part("Phone")phone: RequestBody,
                          @Part("adresse")adresse: RequestBody,
                          @Part  imageFile:MultipartBody.Part): Call<Account>

    @GET("/images/get/{id}")
    fun getImage(@Path("id") id:Long): Call<String>

}