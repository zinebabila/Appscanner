package com.example.scanner.bo

import com.example.scanner.bo.Customer
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Account {
     @SerializedName("id")
     val id: Long? = null
     @SerializedName("email")
     var email: String? = null
     @SerializedName("password")
     var password: String? = null
     @SerializedName("user")
     val user: Customer? = null
    // @SerializedName("merchantOwner")
    // val merchantOwner: Merchant? = null


}