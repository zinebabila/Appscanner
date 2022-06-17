package com.example.scanner.security

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.scannerapp.LoginActivity


class TokenManager {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATE_MODE: Int =0
    constructor (con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
    companion object {
        val PREF_NAME: String = "KotlinDemo"
        val IS_LOGIN: String = "isLoggedIn"
        val KEY_NAME: String ="token"
        val KEY_Id: String ="id"
    }
        fun createloginsession (name: String,id:Long) {
            editor.putBoolean(IS_LOGIN, true)
            editor.putString(KEY_NAME, name)
            editor.putLong(KEY_Id,id)
            editor.commit()
        }
    fun checklogin(){
        if(!this.logg()){
          var i:Intent= Intent(con, LoginActivity::class.java)
            con.startActivity (i)
        }}
            fun gettokenDetails(): String{
                return pref.getString(KEY_NAME,"").toString()

            }
    fun getidAccount(): Long{
        return pref.getLong(KEY_Id,0)

    }
            fun Logoutlser (){
            editor.clear ()
            editor.commit()
            var i: Intent =Intent (con, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity (i)

        }
    fun logg():Boolean{
      return  pref.getBoolean(IS_LOGIN,false)
    }
    }