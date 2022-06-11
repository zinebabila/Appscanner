package com.example.scanner.bo

import com.example.scannerapp.bo.Image


abstract class Person {


     var id: Long? = null

    var firstName: String? = null

    var lastName: String? = null

    var numTel: String? = null
     var image: Image? = null
    var adresse: String? = null
}