package com.example.scannerapp.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class DataReq {
    @SerializedName("account_id")
    @Expose
    var account_id:Long?=0
    @SerializedName("coupon")
    @Expose
    var coupon:Coupon?=null
    @SerializedName("products")
    @Expose
    var products = arrayListOf<CartItem>()

    @SerializedName("somme")
    @Expose
    var somme:Double?=0.0
    @SerializedName("soldWallet")
    @Expose
    var soldeWallet:Double?=0.0
    @SerializedName("currency_id")
    @Expose
    var currency_id:Long?=0
    @SerializedName("status")
    @Expose
    var status: String? = null
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
    @SerializedName("adresse")
    @Expose
    var adresse: String? = null
    @SerializedName("comment")
    @Expose
    var comment: String? = null
    @SerializedName("id_customer")
    @Expose
    var id_customer: Long? = null

    @SerializedName("rating")
    @Expose
    var rating:Double?=0.0
    override fun toString(): String {
        return "Data( account_id=$account_id, coupon_id=$coupon, products=$products, somme=$somme, currency_id=$currency_id)"
    }


}