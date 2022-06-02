package com.example.scannerapp


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.scanner.bo.Account
import com.example.scanner.bo.Customer
import com.example.scanner.security.TokenManager
import com.example.scannerapp.controller.CustumerController
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var session: TokenManager
    private lateinit var apiService: AccountController
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var menuNav: Menu
    lateinit var mNavigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)
        session = TokenManager(applicationContext)
        mNavigationView  = findViewById(R.id.nav_view)
        menuNav    = mNavigationView.menu
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        mNavigationView.setNavigationItemSelectedListener(this@ActivityMain)
        val header = mNavigationView.getHeaderView(0)
        var imagePro=header.findViewById<ImageView>(R.id.nav_header_imageView)
        var detail=header.findViewById<TextView>(R.id.nav_header_textView)
        /******************************les information du profil*****************************************/
        AccountEnd.authToken = session.gettokenDetails()
            apiService = AccountEnd.retrofit.create(AccountController::class.java)
            apiService.getAccount(session.getidAccount()).enqueue(object : retrofit2.Callback<Account> {
                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    if (response.body() != null) {
                        var merchant = response.body()!!

                        Picasso.get().load(merchant.user?.urlImage).fit().into(imagePro)
                        detail.text =
                            merchant.user?.firstName + "  " + (merchant.user?.lastName ) + "\n" + (merchant.user?.numTel) +"\n"+
                                    merchant.email
                    } else {
                        println("error")
                    }

                }

                override fun onFailure(call: Call<Account>, t: Throwable) {
                    println(t.message)
                }

            })
        imagePro.setOnClickListener(View.OnClickListener {
            gotoModifierActivity()
        })
        findViewById<ImageButton>(R.id.qrScanner).setOnClickListener(View.OnClickListener {
            gotoSecondActivity()
        })


    }

    private fun gotoModifierActivity() {
        val intent = Intent(this, ModifierActivity::class.java)

        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun gotoSecondActivity() {
        val intent = Intent(this, scannerActivity::class.java)

        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.pp){
                drawer.closeDrawer(GravityCompat.START)
                return true
            }
        if(item.itemId==R.id.walets){
           gowallet()
            return true
        }

        return false
    }

    private fun gowallet() {
        val intent = Intent(this, WalletsActivity::class.java)

        startActivity(intent)
    }


}

