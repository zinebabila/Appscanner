package com.example.scannerapp.bo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
class Currency {
   @SerializedName("id")
   @Expose
     var id: Long? = null

    @SerializedName("currencyName")
    @Expose
     var currencyName: String? = null
    @SerializedName("percentageToDollar")
    @Expose
     var percentageToDollar: Double? = null
    @SerializedName("imageCurrency")
    @Expose
     var imageCurrency: String? = null
    @SerializedName("wallets")
    @Expose
     val wallets: Set<Wallet> = HashSet()
    @SerializedName("symbol")
    @Expose
    val symbol: String? = null


}