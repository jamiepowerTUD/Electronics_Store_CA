package com.patterns.electronics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Homepage : AppCompatActivity()
{

    lateinit var username : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)


        val sharedPreference =  getSharedPreferences("username",
            Context.MODE_PRIVATE)

        val name = sharedPreference.getString("name","null").toString()

        Log.i("name",name)


        username = findViewById(R.id.username)
        username.text = name



    }

    fun gotoCatalog(v : View)
    {
        val i = Intent(this@Homepage, Catalogue::class.java)
        startActivity(i)
    }

    fun gotoCart(v : View)
    {
        val i = Intent(this@Homepage, ShoppingCart::class.java)
        startActivity(i)
    }


    fun gotoDiscount(v : View)
    {
        val i = Intent(this@Homepage, Discount::class.java)
        startActivity(i)
    }






}