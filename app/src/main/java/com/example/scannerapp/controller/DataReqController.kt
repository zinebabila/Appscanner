package com.example.scannerapp.controller

import com.example.scanner.bo.Data
import com.example.scannerapp.bo.Command
import com.example.scannerapp.bo.DataReq

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DataReqController {
    @POST("/data/add")
    fun addCommand(@Body post: DataReq) : Call<Command>
}