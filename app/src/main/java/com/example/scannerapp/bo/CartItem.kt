package com.example.scannerapp.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
 data class CartItem(
    @SerializedName("product")
    @Expose
    var product: Product,

    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0
)