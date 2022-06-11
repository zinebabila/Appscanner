package com.example.scannerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scanner.bo.Account
import com.example.scanner.security.TokenManager
import com.example.scannerapp.bo.RealPathUtil
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.URL


class ModifierActivity : AppCompatActivity() {
    lateinit var session:TokenManager
    lateinit var apiService:AccountController
    private lateinit var first: EditText
    private lateinit var last: EditText
    private lateinit var tel: EditText
    val RESULT_LOAD_IMG = 1
    private lateinit var adresse: EditText
    private lateinit var image: Button
    private lateinit var imagefile: String
    private lateinit var imagePro:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = TokenManager(applicationContext)
        setContentView(R.layout.modifier_activity)
        val toolbar: Toolbar = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        supportActionBar?.setHomeAsUpIndicator(upArrow)



     first=findViewById(R.id.firstm)
        last=findViewById(R.id.lastm)
        tel=findViewById(R.id.telm)
        image=findViewById(R.id.import_img)
        adresse=findViewById(R.id.Adressem)
        imagePro=findViewById<ImageView>(R.id.imageProfil)

        AccountEnd.authToken = session.gettokenDetails()
        apiService = AccountEnd.retrofit.create(AccountController::class.java)
        apiService.getAccount(session.getidAccount()).enqueue(object : retrofit2.Callback<Account> {
            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.body() != null) {
                    var merchant = response.body()!!
                    first.setText(merchant.user?.firstName)
                    last.setText(merchant.user?.lastName)
                    tel.setText(merchant.user?.numTel)

                    adresse.setText(merchant.user?.adresse)
                    val SDK_INT = Build.VERSION.SDK_INT
                    if (SDK_INT > 8) {
                        val policy = StrictMode.ThreadPolicy.Builder()
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


                    image.setOnClickListener(View.OnClickListener {
                        if (ContextCompat.checkSelfPermission(
                                this@ModifierActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            askForwritePermission()


                        }
                        else{
                            val photoPickerIntent = Intent(Intent.ACTION_PICK)
                            photoPickerIntent.type = "image/*"
                            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
                        }




                    })

                    findViewById<Button>(R.id.modifier).setOnClickListener(View.OnClickListener {
                        merchant.user?.firstName = first.text.toString()
                        merchant.user?.lastName = last.text.toString()
                        merchant.user?.numTel = tel.text.toString()
                        merchant.user?.adresse = adresse.text.toString()

                        val file = File(imagefile)
                            val uploadFile = MultipartBody.Part.createFormData(
                                "imageFile",
                                file.name,
                               RequestBody.create(MediaType.parse("multipart/form-data"), file)
                            )
                        val request=RequestBody.create(MediaType.parse("multipart/form-data"), merchant.id!!.toString())
                        val requestfirst=RequestBody.create(MediaType.parse("multipart/form-data"),first.text.toString() )
                        val requestlast=RequestBody.create(MediaType.parse("multipart/form-data"),last.text.toString() )
                        val requestPhone=RequestBody.create(MediaType.parse("multipart/form-data"), tel.text.toString())
                        val requestAdresse=RequestBody.create(MediaType.parse("multipart/form-data"), adresse.text.toString())


                        AccountEnd.authToken = session.gettokenDetails()
                        apiService = AccountEnd.retrofit.create(AccountController::class.java)
                        apiService.ModiffierAccount(request,requestfirst,requestlast,requestPhone,requestAdresse, uploadFile)
                            .enqueue(object : retrofit2.Callback<Account> {
                                override fun onResponse(
                                    call: Call<Account>,
                                    response: Response<Account>
                                ) {
                                    println("success")
                                    //  println(response.body()!!.user?.firstName)
                                    gosecong()
                                }

                                override fun onFailure(call: Call<Account>, t: Throwable) {
                                    println("onFailure")
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

    private fun askForwritePermission() {
        ActivityCompat.requestPermissions(this,  arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
    }


    @SuppressLint("RestrictedApi")
    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)

        if (reqCode==RESULT_LOAD_IMG&&resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                println((data.data!!).getPath()!!)
                imagefile=RealPathUtil.getRealPathFromURI_API11to18(this@ModifierActivity,data.data!!)
                var bitmap=BitmapFactory.decodeFile(imagefile)
              imagePro.setImageBitmap(bitmap)
            }
        }else {
            Toast.makeText(applicationContext, "Vous n'avez pas choisi d'image", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun gosecong() {
        val intent = Intent(this, ActivityMain::class.java)

        startActivity(intent)
    }



}