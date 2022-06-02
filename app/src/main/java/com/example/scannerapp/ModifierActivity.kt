package com.example.scannerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.scanner.bo.Account
import com.example.scanner.security.TokenManager
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class ModifierActivity : AppCompatActivity() {
    lateinit var session:TokenManager
    lateinit var apiService:AccountController
    private lateinit var first: EditText
    private lateinit var last: EditText
    private lateinit var tel: EditText

    private lateinit var adresse: EditText
    private lateinit var image: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = TokenManager(applicationContext)
        setContentView(R.layout.modifier_activity)
        val toolbar: Toolbar = findViewById(R.id.toolbar3)
        //  val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        //  supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        //upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)


     first=findViewById(R.id.firstm)
        last=findViewById(R.id.lastm)
        tel=findViewById(R.id.telm)
        image=findViewById(R.id.Imagem)
        adresse=findViewById(R.id.Adressem)

        AccountEnd.authToken = session.gettokenDetails()
        apiService = AccountEnd.retrofit.create(AccountController::class.java)
        apiService.getAccount(session.getidAccount()).enqueue(object : retrofit2.Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.body() != null) {
                    var merchant = response.body()!!
                    first.setText(merchant.user?.firstName)
                    last.setText(merchant.user?.lastName)
                    tel.setText(merchant.user?.numTel)
                    image.setText(merchant.user?.urlImage)
                    adresse.setText(merchant.user?.adresse)

                    findViewById<Button>(R.id.modifier).setOnClickListener(View.OnClickListener {
                        merchant.user?.firstName=first.text.toString()
                        merchant.user?.lastName=last.text.toString()
                        merchant.user?.numTel=tel.text.toString()
                        merchant.user?.urlImage=image.text.toString()
                        merchant.user?.adresse=adresse.text.toString()
                        AccountEnd.authToken = session.gettokenDetails()
                        apiService = AccountEnd.retrofit.create(AccountController::class.java)
                        apiService.ModiffierAccount(merchant).enqueue(object : retrofit2.Callback<Account> {
                            override fun onResponse(call: Call<Account>, response: Response<Account>)
                            {
                                println(response.body()!!.user?.firstName)
                                gosecong()
                            }

                            override fun onFailure(call: Call<Account>, t: Throwable) {
                               println(t.message)
                            }
                        })


                    })


                } else {
                    println("error")
                }

            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                println(t.message)
            }

        })
        findViewById<Button>(R.id.cancel).setOnClickListener(View.OnClickListener {
            gosecong()
        })


    }

    private fun gosecong() {
        val intent = Intent(this, ActivityMain::class.java)

        startActivity(intent)
    }

}