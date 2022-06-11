package com.example.scannerapp


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.os.StrictMode.ThreadPolicy
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.scanner.bo.Account
import com.example.scanner.security.TokenManager
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL


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
        mNavigationView = findViewById(R.id.nav_view)
        menuNav = mNavigationView.menu
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        mNavigationView.setNavigationItemSelectedListener(this@ActivityMain)
        val header = mNavigationView.getHeaderView(0)
        var imagePro = header.findViewById<ImageView>(R.id.nav_header_imageView)
        var detail = header.findViewById<TextView>(R.id.nav_header_textView)
        /******************************les information du profil*****************************************/
        AccountEnd.authToken = session.gettokenDetails()
        apiService = AccountEnd.retrofit.create(AccountController::class.java)
        apiService.getAccount(session.getidAccount()).enqueue(object : retrofit2.Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.body() != null) {
                    var merchant = response.body()!!
                    if (merchant.user?.image != null) {
                        /* apiService.getImage(merchant.user?.image?.id!!)
                             .enqueue(object : retrofit2.Callback<String> {
                                 override fun onResponse(
                                     call: Call<String>,
                                     response: Response<String>
                                 ) {*/

                        val SDK_INT = Build.VERSION.SDK_INT
                        if (SDK_INT > 8) {
                            val policy = ThreadPolicy.Builder()
                                .permitAll().build()
                            StrictMode.setThreadPolicy(policy)
                            val `in`: InputStream =
                                URL("http://192.168.2.106:9099/images/get/"+merchant.user?.image?.id!!).openConnection().getInputStream()
                            var profilePic = BitmapFactory.decodeStream(`in`)

                            val stream = ByteArrayOutputStream()
                            profilePic.compress(Bitmap.CompressFormat.PNG, 100, stream)

                            imagePro.setImageBitmap(profilePic)
                            // imagePro.setImageBitmap(StringToBitMap(response.body()!!))
                        }

                              /*  }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    println(t.message)
                                }

                            })*/
                    }
                    detail.text =
                        merchant.user?.firstName + "  " + (merchant.user?.lastName) + "\n" + (merchant.user?.numTel) + "\n" +
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
    private fun hexStringToByteArray(s: String): ByteArray? {
        val b = ByteArray(s.length / 2)
        for (i in b.indices) {
            val index = i * 2
            val v = s.substring(index, index + 2).toInt(16)
            b[i] = v.toByte()
        }
        return b
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
        if (item.itemId == R.id.pp) {
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
        if (item.itemId == R.id.walets) {
            gowallet()
            return true
        }

        return false
    }

    private fun gowallet() {
        val intent = Intent(this, WalletsActivity::class.java)

        startActivity(intent)
    }

    fun StringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

}

