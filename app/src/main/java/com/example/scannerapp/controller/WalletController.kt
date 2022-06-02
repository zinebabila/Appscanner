package com.example.scannerapp.controller

import com.example.scanner.bo.Customer
import com.example.scanner.bo.Data
import com.example.scannerapp.bo.Wallet
import com.example.scannerapp.bo.WalletReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WalletController {
    @POST("/modifierwallet")
    fun modifierwallet(@Body post: WalletReq) : Call<Wallet>
}