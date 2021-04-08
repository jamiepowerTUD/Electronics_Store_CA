package com.patterns.electronics

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.patterns.electronics.adapters.checkoutAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ShoppingCart : AppCompatActivity()
{

    lateinit var recycler : RecyclerView

    lateinit var mAuth: FirebaseAuth

    lateinit var cart_ref : DatabaseReference
    lateinit var item_ref: DatabaseReference
    lateinit var history_ref : DatabaseReference

    lateinit var basket : ArrayList<CartItem>

    lateinit var adapter : checkoutAdapter
    lateinit var id : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout_activity)


        recycler = findViewById(R.id.checkout_recycler)

        mAuth = FirebaseAuth.getInstance()

         id = mAuth.currentUser?.uid.toString()

        basket = ArrayList()

        cart_ref = FirebaseDatabase.getInstance().getReference("Cart")
        item_ref = FirebaseDatabase.getInstance().getReference("Item")
        history_ref = FirebaseDatabase.getInstance().getReference("History")


        cart_ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
               for (snap in snapshot.children)
               {
                   if(snap.child("userid").value.toString() == id)
                   {
                       val name = snap.child("item_name").value.toString()
                       val price = snap.child("price").value.toString().toDouble()
                       val amount = snap.child("amount").value.toString().toInt()
                       val image = snap.child("imageURI").value.toString()

                       basket.add(CartItem(id,name,amount,price,image))

                   }

                   adapter = checkoutAdapter(basket)
                   val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ShoppingCart)
                   recycler.layoutManager = mLayoutManager
                   recycler.adapter = adapter



               }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })






    }


    fun checkout(v : View)
    {
        var valid = true


        cart_ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               
                for (snap in snapshot.children) {

                    val name = snap.child("item_name").value.toString()
                    val amount = snap.child("amount").value.toString().toInt()

                    item_ref.addListenerForSingleValueEvent(object  : ValueEventListener{
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onDataChange(snapshot: DataSnapshot)
                        {
                            for(item in snapshot.children)
                            {
                                val title = item.child("name").value.toString()
                                val stock = item.child("amount").value.toString().toInt()

                                if(name == title)
                                {
                                    if(amount <= stock)
                                    {
                                        Log.i("Firebase", name)
                                        Log.i("Firebase",item_ref.child(item.key.toString()).child("name").toString())
                                        item_ref.child(item.key.toString()).child("amount").setValue(stock-amount)
                                        cart_ref.child(snap.key.toString()).removeValue()

                                        val price = item.child("price").value.toString().toDouble()
                                        val image = item.child("image_uri").value.toString()
                                        val cur = LocalDateTime.now()
                                        val formatted = cur.format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))


                                        history_ref.push().setValue(History(id,title,price,amount,image,formatted))

                                        for(i in 0..basket.size)
                                        {
                                            if(basket.isNotEmpty())
                                            {
                                                if(basket.get(i).item_name == name)
                                                {
                                                    basket.removeAt(i)
                                                    break
                                                }
                                            }

                                        }

                                    }
                                    else
                                    {
                                        valid = false
                                    }

                                }



                            }

                            if(!valid)
                            {
                                Toast.makeText(this@ShoppingCart,"Checkout Complete, some items are not in stock unfortunately",Toast.LENGTH_LONG).show()
                            }
                            else
                            {
                                Toast.makeText(this@ShoppingCart,"Checkout Complete",Toast.LENGTH_LONG).show()
                            }


                            basket.clear()
                            adapter.notifyDataSetChanged()

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })



                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }





}