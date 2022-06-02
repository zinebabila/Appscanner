package com.example.scannerapp.bo

import com.example.scanner.bo.Customer
import kotlinx.serialization.Serializable

@Serializable
class Review {

    private var id: Long? = null
    private var rate = 0

    private var comment: String? = null

    private var customer: Customer? = null



}