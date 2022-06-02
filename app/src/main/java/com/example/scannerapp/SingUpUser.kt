package com.example.scannerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.scanner.bo.Customer
import com.example.scanner.bo.Data
import com.example.scanner.security.TokenManager
import com.example.systemposfront.controller.DataController
import com.example.systemposfront.interfaces.AccountEnd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingUpUser: AppCompatActivity() {
    private lateinit var first:EditText
    private lateinit var last:EditText
    private lateinit var tel:EditText
    private  lateinit var email:EditText

    private lateinit var pass:EditText
    private lateinit var image:EditText
    private lateinit var Address:EditText
    private lateinit var session:TokenManager
    private lateinit var DataApi:DataController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = TokenManager(applicationContext)
        setContentView(R.layout.activity_add_product)
        Address=findViewById(R.id.Adresse)
        first = findViewById(R.id.first)
        last = findViewById(R.id.last)
        tel = findViewById(R.id.tel)
        email = findViewById(R.id.email)
        pass = findViewById(R.id.password)
        image=findViewById(R.id.Image)
        findViewById<View>(R.id.canc2).setOnClickListener {

            goToSecondActivity()
        }
        findViewById<View>(R.id.addP).setOnClickListener {
            if(CheckAllFields(email!!,pass!!,first!!,last!!,tel!!,Address!!)) {
                addUser()
                goToSecondActivity()
            }
        }
    }
    private fun goToSecondActivity( ) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
    private fun addUser() {
        var data=Data()
        data.email=email.text.toString()
        data.firstName=first.text.toString()
        data.lastName=last.text.toString()
        data.numTel=tel.text.toString()
        data.password=pass.text.toString()
        if(image.text.length!=0){
            data.urlImage=image.text.toString()
        }
        AccountEnd.authToken = session.gettokenDetails()
        DataApi = AccountEnd.retrofit.create(DataController::class.java)
        DataApi.addCostumer(data).enqueue(object : Callback<Customer> {
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {

                print("okkkkkkkkkkkk")
            }
            override fun onFailure(call: Call<Customer>, t: Throwable) {
                println(t.message)
            }
        })


    }





    private fun CheckAllFields(
        email: EditText,
        pass: EditText,
        first: EditText,
        last: EditText,
        tel: EditText,
        address: EditText
    ): Boolean {
        if (email.length() == 0) {
            email.error = "Email is required"
            return false
        }
        if (pass.length() == 0) {
            pass.error = "Password is required"
            return false
        }

        if (first?.length() == 0) {
            first?.error = "First Name is required"
            return false
        }
        if (last?.length() == 0) {
            last?.error = "Last Name is required"
            return false
        }
        if (tel?.length() == 0) {
            tel?.error = "Phone Number is required"
            return false
        }
        if (Address?.length() == 0) {
            Address?.error = "Address is required"
            return false
        }
        return true
    }
}