package com.example.scanner.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Data {
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null
    @SerializedName("numTel")
    @Expose
    var numTel: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("solde")
    @Expose
    var solde: Double? = null
    @SerializedName("urlImage")
    @Expose
    var urlImage: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null


}