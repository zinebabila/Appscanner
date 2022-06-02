package com.example.scanner.bo

import com.example.scannerapp.bo.Wallet
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
class Customer : Person() {
    @SerializedName("wallets")
    @Expose
     var wallets: MutableSet<Wallet?>? = HashSet()
    @SerializedName("account")
    @Expose
    var account: Account? = null


}