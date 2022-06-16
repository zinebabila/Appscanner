package com.example.scannerapp


import android.database.MatrixCursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanner.bo.Account
import com.example.scanner.security.TokenManager
import com.example.scannerapp.bo.Transaction
import com.example.scannerapp.bo.Wallet
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat


class TransactionActivity : AppCompatActivity() {
    lateinit var session: TokenManager
    lateinit var apiService:AccountController
    lateinit var  list: MutableSet<Transaction?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_activity)
        session = TokenManager(applicationContext)
        val toolbar: Toolbar = findViewById(R.id.toolbar343)
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
                    list = merchant.user?.transactions!!
                    if(list!=null){
                        val columns = arrayOf("_id","Date", "Somme", "Currency")


                        val matrixCursor = MatrixCursor(columns)

                        startManagingCursor(matrixCursor)

                        for(l in list){
                            val df = DecimalFormat("0.000000")


                        matrixCursor.addRow(arrayOf<Any>(l?.id.toString(), l?.dateTransaction.toString(), df.format(l?.somme),l?.currency?.symbol.toString()))

                        }

                        val from = arrayOf("Date", "Somme","Currency")

                        val to = intArrayOf(R.id.textViewCol1, R.id.textViewCol2,R.id.textViewCol3)

                        val adapter = SimpleCursorAdapter(this@TransactionActivity, R.layout.row_item, matrixCursor, from, to, 0)


                        val lv: ListView = findViewById<View>(R.id.lv) as ListView
                        lv.setAdapter(adapter)
                    }

                } else {
                    println("error")
                }
            }
            override fun onFailure(call: Call<Account>, t: Throwable) {
                println(t.message)
            }

        })





    }

}