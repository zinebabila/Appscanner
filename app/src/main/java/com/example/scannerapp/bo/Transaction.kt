package com.example.scannerapp.bo

import com.example.scanner.bo.Customer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Transaction {
    @SerializedName("id")
    @Expose
     val id: Long? = null
    @SerializedName("dateTransaction")
    @Expose
     val dateTransaction: String? = null
    @SerializedName("somme")
    @Expose
     val somme = 0.0
    @SerializedName("currency")
    @Expose
     val currency: Currency? = null
    @SerializedName("user")
    @Expose
    private val user: Customer? = null
}