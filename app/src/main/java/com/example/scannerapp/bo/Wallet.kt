package com.example.scannerapp.bo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
class Wallet {
   @SerializedName("id")
   @Expose
     val id: Long? = null
    @SerializedName("solde")
    @Expose
    var solde:Double? = null

    @SerializedName("currency")
    @Expose
     val currency: Currency? = null


}
