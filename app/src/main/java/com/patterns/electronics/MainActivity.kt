package com.patterns.electronics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    lateinit var reg : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      reg  = findViewById<TextView>(R.id.reg_link)


    }

    fun gotoLogin(v : View)
    {
        val i = Intent(this , Login::class.java)
        startActivity(i)

    }



    fun gotoRegister(v : View)
    {
        reg.setTextColor(getColor(R.color.light_blue))
        val i = Intent(this , Register::class.java)
        startActivity(i)

    }

}