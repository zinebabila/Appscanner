package com.example.scannerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scanner.bo.Customer
import com.example.scanner.bo.Data
import com.example.scanner.security.TokenManager
import com.example.scannerapp.bo.RealPathUtil
import com.example.systemposfront.controller.DataController
import com.example.systemposfront.interfaces.AccountEnd
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SingUpUser: AppCompatActivity() {
    private lateinit var first:EditText
    private lateinit var last:EditText
    private lateinit var tel:EditText
    private  lateinit var email:EditText
    val RESULT_LOAD_IMG = 1
    private lateinit var imagefile:String
    private lateinit var pass:EditText
    private lateinit var image: ImageView
    private lateinit var importe:Button
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
        image=findViewById(R.id.imageProfilS)
        importe=findViewById(R.id.import_imgS)
        findViewById<View>(R.id.canc2).setOnClickListener {

            goToSecondActivity()
        }
        importe.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@SingUpUser, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
        findViewById<View>(R.id.addP).setOnClickListener {
            if(CheckAllFields(email!!,pass!!,first!!,last!!,tel!!,Address!!)) {
                addUser()
                goToSecondActivity()
            }
        }
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
                imagefile= RealPathUtil.getRealPathFromURI_API11to18(this@SingUpUser,data.data!!)
                var bitmap= BitmapFactory.decodeFile(imagefile)
                image.setImageBitmap(bitmap)
            }
        }else {
            Toast.makeText(applicationContext, "Vous n'avez pas choisi d'image", Toast.LENGTH_LONG)
                .show()
        }
    }
    private fun goToSecondActivity( ) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
    private fun addUser() {
        val file = File(imagefile)
        val uploadFile = MultipartBody.Part.createFormData(
            "imageFile",
            file.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )

        val requestfirst= RequestBody.create(MediaType.parse("multipart/form-data"),first.text.toString() )
        val requestlast= RequestBody.create(MediaType.parse("multipart/form-data"),last.text.toString() )
        val requestPhone= RequestBody.create(MediaType.parse("multipart/form-data"), tel.text.toString())
        val requestAdresse= RequestBody.create(MediaType.parse("multipart/form-data"), Address.text.toString())
        val requestemail= RequestBody.create(MediaType.parse("multipart/form-data"), email.text.toString())
        val requestpass= RequestBody.create(MediaType.parse("multipart/form-data"), pass.text.toString())

        AccountEnd.authToken = session.gettokenDetails()
        DataApi = AccountEnd.retrofit.create(DataController::class.java)
        DataApi.addCostumer(requestfirst,requestlast,requestPhone,requestAdresse,uploadFile,requestemail,requestpass).enqueue(object : Callback<Customer> {
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