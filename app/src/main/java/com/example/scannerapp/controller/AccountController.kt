package com.example.systemposfront.controller


import com.example.scanner.bo.Account
import com.example.scanner.bo.Customer
import com.example.scanner.bo.JwtRequest
import com.example.scanner.bo.JwtResponse

import retrofit2.Call
import retrofit2.http.*

interface AccountController {

    @POST("/authenticate")
     fun login(@Body post: JwtRequest) : Call<JwtResponse>

    @GET("/Account/getOne/{id}")
    fun getAccount(@Path("id") id:Long): Call<Account>
    @PUT("/Account/update")
     fun ModiffierAccount(@Body post: Account): Call<Account>


}