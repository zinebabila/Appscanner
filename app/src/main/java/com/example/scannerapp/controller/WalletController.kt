package com.example.scannerapp.controller


import com.example.scannerapp.bo.ReqTransaction
import com.example.scannerapp.bo.Transaction
import com.example.scannerapp.bo.Wallet
import com.example.scannerapp.bo.WalletReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WalletController {
    @POST("/modifierwallet")
    fun modifierwallet(@Body post: WalletReq) : Call<Wallet>
    @POST("/Transaction")
    fun ajouterTransaction(@Body post: ReqTransaction):Call<Transaction>
}