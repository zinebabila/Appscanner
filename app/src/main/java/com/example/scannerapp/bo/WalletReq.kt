package com.example.scannerapp.bo

import com.example.scanner.bo.Customer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class WalletReq {
    @SerializedName("wallet")
    @Expose
    var wallet:Wallet?=null
    @SerializedName("user")
    @Expose
    var user:Customer?=null
}