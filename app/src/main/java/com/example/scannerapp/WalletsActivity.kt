package com.example.scannerapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanner.bo.Account
import com.example.scanner.bo.Customer
import com.example.scanner.security.TokenManager
import com.example.scannerapp.bo.Wallet
import com.example.scannerapp.controller.CustumerController
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.text.DecimalFormat

class WalletsActivity : AppCompatActivity() {
    lateinit var adapter: WalletAdapter
    lateinit var session:TokenManager
    lateinit var apiService:AccountController
    lateinit var  list: MutableSet<Wallet?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walletactivity)
        session = TokenManager(applicationContext)
        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        //  val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
       //  supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        //upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        AccountEnd.authToken = session.gettokenDetails()
        apiService = AccountEnd.retrofit.create(AccountController::class.java)
        apiService.getAccount(session.getidAccount()).enqueue(object : retrofit2.Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.body() != null) {
                    var merchant = response.body()!!
                     list = merchant.user?.wallets!!
                    if(list!=null){
                        adapter = WalletAdapter(this@WalletsActivity, list)
                        adapter.notifyDataSetChanged()
                        var shopping_cart_recyclerView: RecyclerView = findViewById(R.id.walets_recyclerview)
                        shopping_cart_recyclerView.adapter = adapter

                        shopping_cart_recyclerView.layoutManager = LinearLayoutManager(this@WalletsActivity)
                    }
                    runurl("http://api.coingecko.com/api/v3/simple/price?ids=Bitcoin%2CRavencoin%2CEthereum%2CTether%2Clitecoin%2Cbitcash&vs_currencies=usd",list)
                    //  val parser:JSONObject= JSONObject(result)


                } else {
                    println("error")
                }
            }
            override fun onFailure(call: Call<Account>, t: Throwable) {
                println(t.message)
            }

        })





        }

    private fun runurl(url: String, list: MutableSet<Wallet?>) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println(e.message)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                var str_response = response.body()!!.string()
                //creating json object
                val parser: JSONObject = JSONObject(str_response)
                var somme:Double?=0.0
                if (somme != null) {
                    for(wallet in list) {
                        var tether =
                            parser.getJSONObject(wallet?.currency?.currencyName?.lowercase()).getString("usd")
                                .toDouble()

                        somme += wallet?.solde?.times(tether)!!


                    }
                }
                val df = DecimalFormat("0.00")
                Handler(Looper.getMainLooper()).post {
                var balance=findViewById<TextView>(R.id.total)

                balance.text="    "+df.format(somme)+"$"}
            }

            //creating json array


        })

    }


}