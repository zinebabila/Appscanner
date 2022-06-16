package com.example.scannerapp.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
class ReqTransaction {
        @SerializedName("id_user")
        @Expose
     var id_user: Long? = null
    @SerializedName("somme")
    @Expose
     var somme = 0.0
    @SerializedName("id_currency")
    @Expose
     var id_currency: Long? = null
}