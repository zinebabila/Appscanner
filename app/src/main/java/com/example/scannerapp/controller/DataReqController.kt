package com.example.scannerapp.controller

import com.example.scanner.bo.Data
import com.example.scannerapp.bo.Command
import com.example.scannerapp.bo.DataReq
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*

interface DataReqController {
    @POST("/data/add")
    fun addCommand(@Body post: DataReq) : Call<Command>
    @Multipart
    @PUT("/data/image")
    fun addImage(@Part("account")id: RequestBody ,@Part  imageFile: MultipartBody.Part) : Call<String>
}