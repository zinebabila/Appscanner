package com.example.scannerapp.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Product {
    @SerializedName("id")
    @Expose
     var id: Long? = null
    @SerializedName("title")
    @Expose
     var title: String? = null
    @SerializedName("description")
    @Expose
     var description: String? = null

    @SerializedName("reduction")
    @Expose
     var reduction: Double? = null
    @SerializedName("prix")
    @Expose
    var prix: Double? = null
    @SerializedName("qteStock")
    @Expose
     var qteStock = 0



}