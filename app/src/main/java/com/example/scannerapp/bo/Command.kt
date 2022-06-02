package com.example.scannerapp.bo

import com.example.scanner.bo.Account
import com.google.gson.annotations.Expose
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import java.time.LocalDateTime
import java.util.*
@Serializable
class Command {
    @SerialName("id")
    @Expose
     var id: Long? = null
    @SerialName("dateCommand")
    @Expose

       var dateCommand:String?=null
    @SerialName("account")
    @Expose
     var account: Account? = null

}