package com.example.scannerapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.scanner.bo.Account
import com.example.scanner.bo.Customer
import com.example.scanner.security.TokenManager
import com.example.scannerapp.bo.*
import com.example.scannerapp.controller.DataReqController
import com.example.scannerapp.controller.WalletController
import com.example.scannerapp.databinding.ActivityMainBinding
import com.example.scannerapp.interfaces.AuthenEndReq
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor


class scannerActivity : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var text:TextView
    lateinit var session: TokenManager
    private lateinit var apiService: AccountController
    private lateinit var apiServicewallet: WalletController
        private lateinit var binding: ActivityMainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            session = TokenManager(applicationContext)
             executor = ContextCompat.getMainExecutor(this)
              biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                        reglerPaymentFailure()
                        gotoAuthentification()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                        reglerPayment()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            //  val toolbar: Toolbar = findViewById(R.id.toolbar_main)
            setSupportActionBar(toolbar)
             //supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
            //upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
            supportActionBar?.setHomeAsUpIndicator(upArrow)
      text=findViewById(R.id.text)
            if (ContextCompat.checkSelfPermission(
                this@scannerActivity, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }

     //   val aniSlide: Animation =
      //      AnimationUtils.loadAnimation(this@scannerActivity, R.anim.scanner_animation)
      //  binding.barcodeLine.startAnimation(aniSlide)
    }

    private fun reglerPaymentFailure() {
        var data=DataReq()
        data.status="faillure"
        var accou:DataReqController
        accou = AuthenEndReq.retrofit.create(DataReqController::class.java)
        accou.addCommand(data).enqueue(object : retrofit2.Callback<Command>{
            override fun onResponse(call: Call<Command>, response: Response<Command>) {

                print("okkkkkkkkkkkk")
            }
            override fun onFailure(call: Call<Command>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun gotoAuthentification() {
        val intent = Intent(this, LoginActivity::class.java)
        println("echhhhhhhhhhhhhhhhhhhhec")
        startActivity(intent)
    }


    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue


                    //Don't forget to add this line printing value or finishing activity must run on main thread
                    runOnUiThread {
                        cameraSource.stop()
                        validerpayment(scannedValue)

                    }
                }else
                {
                    text.text="error"

                }
            }
        })
    }


    private fun validerpayment(scannedValue: String) {
        lateinit var decrypted: String
        var currency_id: Long? = 0
        var soldewallet: Double? = 0.0
        var currencyName: String? =null
        var FirstName: String? =null
        var LastName: String? =null
        try {
            println(scannedValue)
            decrypted = AESUtils.decrypt(scannedValue)
            var json = JSONObject(decrypted)

            // var json=JSONObject(scannedValue)
            println(json)
            currency_id = json.getString("currency_id").toLong()
            currencyName = json.getString("currencyName")
            soldewallet = json.getString("soldCurrency").toDouble()
            FirstName = json.getString("accountFirstName")
            LastName = json.getString("accountlast_Name")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val dialog = Dialog(this, R.style.DialogStyle)
        dialog.setContentView(R.layout.layout_custom_dialog)
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
        var texttitle:TextView=dialog.findViewById(R.id.txttite)
        texttitle.text="validate your payment"

        var textview:TextView=dialog.findViewById(R.id.txtDesc)
        textview.text="you have placed an order with "+FirstName+" "+LastName+"\n the sum is "+  soldewallet + currencyName
        var bntclose:Button=dialog.findViewById(R.id.btn_no)
        bntclose.setOnClickListener(View.OnClickListener { dialog.dismiss()
            reglerPaymentFailure()
        goParent()
        })

        val btnClose: ImageView = dialog.findViewById(R.id.btn_close)

        btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
            goParent() })
        var btnYes:Button=dialog.findViewById(R.id.btn_yes)
        btnYes.setOnClickListener(View.OnClickListener { dialog.dismiss()



            biometricPrompt.authenticate(promptInfo) })

        dialog.show()

    }
    fun reglerPayment() {
        lateinit var decrypted: String
        var currency_id: Long? = 0
        var soldewallet: Double? = 0.0
        var somme: Double? = 0.0
        try {
            println(scannedValue)
            decrypted = AESUtils.decrypt(scannedValue)
            var json = JSONObject(decrypted)

            // var json=JSONObject(scannedValue)
            println(json)
            currency_id = json.getString("currency_id").toLong()
            somme = json.getString("sommef").toDouble()
            soldewallet = json.getString("soldCurrency").toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
            var okk = 0
            AccountEnd.authToken = session.gettokenDetails()
            apiService = AccountEnd.retrofit.create(AccountController::class.java)
            apiService.getAccount(session.getidAccount())
                .enqueue(object : retrofit2.Callback<Account> {
                    override fun onResponse(call: Call<Account>, response: Response<Account>) {
                        if (response.body() != null) {
                            var merchant = response.body()!!
                            for (wallet in merchant.user?.wallets!!) {
                                println("wallllllllllllllllllllllllllllllllllllllllllllet")
                                println(currency_id)
                                println(somme)
                                if (wallet?.currency?.id == currency_id) {
                                    //  var soldefinal= wallet?.solde?.times(wallet?.currency?.percentageToDollar!!)
                                    if (wallet?.solde!! >= soldewallet!!) {
                                        //  soldefinal -= somme
                                        //  var soldewallet=soldefinal/ wallet?.currency?.percentageToDollar!!
                                        if (soldewallet != null) {
                                            wallet!!.solde = wallet!!.solde?.minus(soldewallet)
                                        }
                                        println(wallet!!.solde)
                                        modifierwallet(wallet!!, merchant.user!!)
                                        success()
                                        println("**************************************")

                                        okk = 1
                                    } else {
                                        reglerPaymentFailure()

                                        val dialog = Dialog(this@scannerActivity, R.style.DialogStyle)
                                        dialog.setContentView(R.layout.layout_costumer_dialogue_pay)
                                        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
                                        var texttitle:TextView=dialog.findViewById(R.id.txttite2)
                                        texttitle.text="Payment failed"
                                        val animationView: LottieAnimationView = dialog.findViewById(R.id.animation_view2)
                                        animationView.setAnimation(R.raw.paymentfailed)
                                        animationView.repeatCount = 100
                                        animationView.playAnimation()
                                        val btnClose: ImageView = dialog.findViewById(R.id.btn_close2)

                                        btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
                                            goParent()
                                        })
                                        var btnYes:Button=dialog.findViewById(R.id.btn_yes2)
                                        btnYes.setOnClickListener(View.OnClickListener { dialog.dismiss()
                                            goParent()
                                        })

                                        dialog.show()
                                        okk = 1
                                    }
                                }


                            }
                            if (okk == 0) {

                                reglerPaymentFailure()
                                val dialog = Dialog(this@scannerActivity, R.style.DialogStyle)
                                dialog.setContentView(R.layout.layout_costumer_dialogue_pay)
                                dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
                                var texttitle:TextView=dialog.findViewById(R.id.txttite2)
                                texttitle.text="Payment failed"
                                val animationView: LottieAnimationView = dialog.findViewById(R.id.animation_view2)
                                animationView.setAnimation(R.raw.paymentfailed)
                                animationView.repeatCount = 100
                                animationView.playAnimation()
                                val btnClose: ImageView = dialog.findViewById(R.id.btn_close2)

                                btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
                                    goParent()
                                })
                                var btnYes:Button=dialog.findViewById(R.id.btn_yes2)
                                btnYes.setOnClickListener(View.OnClickListener { dialog.dismiss()
                                    goParent()
                                })

                                dialog.show()


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


    private fun haverating(){
        val dialogBuilder = AlertDialog.Builder(this@scannerActivity)
        dialogBuilder.setTitle("You Rating")
        dialogBuilder.setMessage("solde suffisant Success !")
        val inflater = layoutInflater
        val dialogLayout  = inflater.inflate(R.layout.activity_rating, null)
        val rBar  = dialogLayout.findViewById< RatingBar>(R.id.rBar)
        val comment  = dialogLayout.findViewById< TextView>(R.id.coment)
        dialogBuilder.setView(dialogLayout)
            // if the dialog is cancelable
            .setCancelable(true)


            // positive button text and action
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                regler_payement(scannedValue, rBar.rating.toString(),comment.text.toString())
                goTosegond()

            })
            .setPositiveButton("OK") { dialogInterface,
                                       i ->
                    dialogInterface.cancel()
                regler_payement(scannedValue, rBar.rating.toString(),comment.text.toString())
                    goTosegond()


            }
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        // show alert dialog
        alert.show()




    }

    private fun goParent() {
        val intent = Intent(this, scannerActivity::class.java)
        println("echhhhhhhhhhhhhhhhhhhhec")
        startActivity(intent)
    }

    private fun modifierwallet(wallet: Wallet, merchant: Customer) {
        var walletReq=WalletReq()
        walletReq.wallet=wallet
        walletReq.user=merchant
        AccountEnd.authToken = session.gettokenDetails()
        apiServicewallet = AccountEnd.retrofit.create(WalletController::class.java)
        apiServicewallet.modifierwallet(walletReq).enqueue(object : retrofit2.Callback<Wallet> {
            override fun onResponse(call: Call<Wallet>, response: Response<Wallet>) {
                    var wallet = response.body()!!




            }

            override fun onFailure(call: Call<Wallet>, t: Throwable) {
                println(t.message)
            }

        })
    }

    private fun goTosegond() {
        val intent = Intent(this, ActivityMain::class.java)
        println("niceeeeeeeeeeeeeeeeee")
        startActivity(intent)
    }


    fun regler_payement(scannedValue:String,rating:String,comment:String){


        var firstName:String?=null
        var lastName:String?=null
        var numTel:String?=null
        var urlimage:String?=null
        var id:Long?=null
        var adresse:String?=null
        var email:String?=null

        AccountEnd.authToken = session.gettokenDetails()
        apiService = AccountEnd.retrofit.create(AccountController::class.java)
        apiService.getAccount(session.getidAccount()).enqueue(object : retrofit2.Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.body() != null) {
                  var  costumer=response.body()!!
                    println(costumer.user?.firstName+"       "+costumer.user?.lastName+"             ")

                     firstName=costumer.user?.firstName
                    lastName=costumer.user?.lastName
                    numTel=costumer.user?.numTel
                    urlimage=costumer.user?.urlImage
                    id=costumer.user?.id
                    adresse= costumer.user?.adresse
                    email=costumer.email
                    lateinit var decrypted:String
                    var data=DataReq()
                    try {
                        decrypted = AESUtils.decrypt(scannedValue)
                        var json=JSONObject(decrypted)
                       //  data.custumer=merchant
                       // var json=JSONObject(scannedValue)
                        var c=Coupon()
                        c.id=json.getString("coupon").toLong()
                        if(rating.length!=0){
                        data.rating=rating.toDouble()}
                        if(comment.length!=0){
                            data.comment=comment
                        }

                        data.firstName=firstName
                        println(data.firstName +"                         okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                        data.lastName=lastName
                        data.id_customer=id
                        data.numTel=numTel
                        data.urlimage=urlimage
                        data.email=email
                        data.adresse=adresse
                        data.coupon=c
                        data.status="success"
                        data.soldeWallet=json.getString("soldCurrency").toDouble()
                        data.account_id=json.getString("account_id").toLong()
                        data.currency_id=json.getString("currency_id").toLong()
                        data.somme=json.getString("somme").toDouble()
                        println("******************************************")
                        println(json.toString())
                        println(json.getJSONArray("products").length())

                        for(i in 0 until json.getJSONArray("products").length()){

                            var pro=Product()
                            var objetarra=json.getJSONArray("products").getJSONObject(i)
                            var q=objetarra.getInt("quantity")
                            pro.id=objetarra.getJSONObject("product").getLong("id")
                            var cart=CartItem(pro,q)
                            data.products.add(cart)
                            println(data.products[i].product.id)
                        }



                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    var accou:DataReqController
                    accou = AuthenEndReq.retrofit.create(DataReqController::class.java)
                    accou.addCommand(data).enqueue(object : retrofit2.Callback<Command>{
                        override fun onResponse(call: Call<Command>, response: Response<Command>) {

                            print("okkkkkkkkkkkk")
                        }
                        override fun onFailure(call: Call<Command>, t: Throwable) {
                            println(t.message)
                        }
                    })
                } else {
                    println("error")
                }

            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                println(t.message)
            }

        })

    }
    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@scannerActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }
    fun success(){
        println("je suiiis iciiiii")
        val dialog = Dialog(this@scannerActivity, R.style.DialogStyle)
        dialog.setContentView(R.layout.layout_costumer_dialogue_pay)
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
        var texttitle:TextView=dialog.findViewById(R.id.txttite2)
        texttitle.text="Payment Succecful"
        val animationView: LottieAnimationView = dialog.findViewById(R.id.animation_view2)
        animationView.setAnimation(R.raw.successful)
        animationView.repeatCount = 100
        animationView.playAnimation()
        val btnClose: ImageView = dialog.findViewById(R.id.btn_close2)

        btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
            haverating()
        })
        var btnYes:Button=dialog.findViewById(R.id.btn_yes2)
        btnYes.setOnClickListener(View.OnClickListener { dialog.dismiss()
            haverating()
        })
       dialog.show()

    }

}