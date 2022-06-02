package com.example.scannerapp.bo

import com.google.gson.annotations.Expose
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*
@Serializable
class Coupon {

    @SerialName("id")
    @Expose
     var id: Long? = null
    @SerialName("code")
    @Expose
    private var code: String? = null
    @SerialName("reduction")
    @Expose
    private var reduction: Double? = null
    @SerialName("expirationDate")
    @Expose
    private var expirationDate: Date? = null
}