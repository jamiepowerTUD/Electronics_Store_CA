package com.patterns.electronics

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AdminConsole : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_console)



    }




    fun gotoAddItem(v : View)
    {
        val i = Intent(this , AddItem::class.java)
        startActivity(i)
    }

    fun gotoCatalogue(v : View)
    {
        val i = Intent(this , Catalogue::class.java)
        startActivity(i)
    }

    fun gotoShoppingCart(v : View)
    {
        val i = Intent(this , ShoppingCart::class.java)
        startActivity(i)
    }

    fun gotoStockManager(v : View)
    {
        val i = Intent(this , StockManagement::class.java)
        startActivity(i)
    }

    fun gotoUserManager(v : View)
    {
        val i = Intent(this , UserManagement::class.java)
        startActivity(i)
    }




}