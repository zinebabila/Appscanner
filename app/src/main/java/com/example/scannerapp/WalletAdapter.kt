package com.example.scannerapp


import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.bo.Wallet

import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat


class WalletAdapter(var context: Context, var products: MutableSet<Wallet?>? =HashSet()) :
    RecyclerView.Adapter<WalletAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WalletAdapter.ViewHolder {
        // The layout design used for each list item
        val view = LayoutInflater.from(context).inflate(R.layout.walletrow, p0, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = products!!.size

    override fun onBindViewHolder(viewHolder: WalletAdapter.ViewHolder, position: Int) {

        products?.elementAt(position)?.let { viewHolder.bindProduct(it) }


        // (context as ProfilActivity).coordinator

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView


        init {
            itemImage = itemView.findViewById(R.id.wallet_im)
            itemTitle = itemView.findViewById(R.id.wallet_na)
            itemDetail = itemView.findViewById(R.id.wallet_dollard)
        }

        fun bindProduct(product: Wallet) {

            run("http://api.coingecko.com/api/v3/simple/price?ids=Bitcoin%2CRavencoin%2CEthereum%2CTether%2Clitecoin&vs_currencies=usd",product)
            //  val parser:JSONObject= JSONObject(result)

            val df = DecimalFormat("0.00")
            itemTitle.text =df.format(product.solde)
        //  var solde=  product?.solde?.times(product?.currency?.percentageToDollar!!)
           // import java.text.DecimalFormat;



            Picasso.get().load(product.currency?.imageCurrency).fit().into(itemImage)

            }
        fun run(url: String,product: Wallet) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    var str_response = response.body()!!.string()
                    //creating json object
                    val parser: JSONObject = JSONObject(str_response)
                    var tether=parser.getJSONObject(product.currency?.currencyName).getString("usd").toDouble()

                    println("imppppppppppppppppppppppppppppppportant")
                    println(tether)

                    val df = DecimalFormat("0.00")
                    Handler(Looper.getMainLooper()).post {
                        itemDetail.text = df.format(product?.solde?.times(tether)) + "$"
                    }


                    //creating json array
                }

            })

        }
        }



    }

