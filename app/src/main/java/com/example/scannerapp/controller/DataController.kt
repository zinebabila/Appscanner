package com.example.systemposfront.controller

import com.example.scanner.bo.Customer
import com.example.scanner.bo.Data
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DataController {
    @POST("/registerNewMerchant")
    fun addCostumer(@Body post: Data) : Call<Customer>
}