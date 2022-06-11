package com.example.systemposfront.controller

import com.example.scanner.bo.Account
import com.example.scanner.bo.Customer
import com.example.scanner.bo.Data
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DataController {
    @Multipart
    @POST("/registerNewMerchant")
    fun addCostumer(
                    @Part("first")first: RequestBody,
                    @Part("last")last: RequestBody,
                    @Part("Phone")phone: RequestBody,
                    @Part("adresse")adresse: RequestBody,
                    @Part  imageFile:MultipartBody.Part,
                    @Part("email")email: RequestBody,
                    @Part("password")password: RequestBody,
                   ) : Call<Customer>
  //  @Multipart
 //   @POST("/registerNewMerchant")
  //  fun addCostumer(@Field("Account")  account: Account, @Field("imageFile")  file: MultipartBody.Part): Call<Account>

}