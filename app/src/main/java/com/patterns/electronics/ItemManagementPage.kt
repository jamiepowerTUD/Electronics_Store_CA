package com.patterns.electronics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ItemManagementPage : AppCompatActivity()
{


    lateinit var item_ref : DatabaseReference
    lateinit var cart_ref : DatabaseReference


    lateinit var intent_name : String

    lateinit var title : TextView
    lateinit var man : TextView
    lateinit var price : TextView
    lateinit var cat : TextView
    lateinit var amount : TextView
    lateinit var color : TextView
    lateinit var image : ImageView

    lateinit var price_field : EditText
    lateinit var amount_field : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_management_page)

        intent_name = intent.getStringExtra("name").toString()
        Log.i("Item Page", intent_name)

        title = findViewById(R.id.item_management_name)
        man = findViewById(R.id.item_management_man)
        cat = findViewById(R.id.item_management_category)
        color = findViewById(R.id.item_management_color)
        image = findViewById(R.id.item_manager_image)

        price = findViewById(R.id.item_management_price)
        amount = findViewById(R.id.item_management_amount)

        price_field = findViewById(R.id.update_price_field)
        amount_field = findViewById(R.id.update_amount_field)




        item_ref = FirebaseDatabase.getInstance().getReference("Item")
        cart_ref = FirebaseDatabase.getInstance().getReference("Cart")


        item_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    if (snap.child("name").value.toString().equals(intent_name)) {
                        title.text = snap.child("name").value.toString()
                        man.text = snap.child("manufacturer").value.toString()
                        price.text = snap.child("price").value.toString()
                        cat.text = snap.child("category").value.toString()
                        amount.text = snap.child("amount").value.toString()
                        color.text = snap.child("color").value.toString()


                        val storage = FirebaseStorage.getInstance()
                        val gsReference = storage.getReferenceFromUrl(snap.child("image_uri").value.toString())
                        gsReference.downloadUrl.addOnCompleteListener {
                            Picasso.get().load(it.result).into(image)
                        }


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }





        fun updateAmount(v : View)
        {
            if(amount_field.text.isEmpty())
            {
                amount_field.error = "Amount field is Empty"
            }
            else if(amount_field.text.toString().toInt() <= 0)
            {

                amount_field.error = "Amount must be bigger than 0"
            }
            else
            {
                item_ref.addListenerForSingleValueEvent(object : ValueEventListener
                {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(snap in snapshot.children)
                        {
                            val name = snap.child("name").value.toString()

                            if(name == title.text.toString())
                            {
                                Log.i("Firebase", snap.key.toString())
                                Log.i("Firebase", snap.child("amount").value.toString())

                                item_ref.child(snap.key.toString()).child("amount").setValue(amount_field.text.toString().toInt())

                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            }

        }



    fun updatePrice(v : View)
    {
        if(price_field.text.isEmpty())
        {
            price_field.error = "Price must not be empty"
        }
        else if(price_field.text.toString().toDouble() <= 0 )
        {
            price_field.error = "Price must be bigger than 0"
        }
        else
        {
            item_ref.addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap in snapshot.children)
                    {
                        val name = snap.child("name").value.toString()

                        if(name == title.text.toString())
                        {

                            item_ref.child(snap.key.toString()).child("price").setValue(price_field.text.toString().toDouble())

                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }




    fun deleteItem(v : View)
    {


        item_ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {
                    val name = snap.child("name").value.toString()

                    if(name == title.text.toString())
                    {

                        item_ref.child(snap.key.toString()).removeValue()


                        cart_ref.addListenerForSingleValueEvent(object : ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(cart in snapshot.children)
                                {
                                    val cart_name = snap.child("item_name").value.toString()

                                    if(cart_name == title.text.toString())
                                    {
                                        cart_ref.child(cart.key.toString()).removeValue()

                                    }

                                }

                                Toast.makeText(this@ItemManagementPage,"Item Deleted",Toast.LENGTH_LONG).show()
                                val i = Intent(this@ItemManagementPage,StockManagement::class.java)
                                startActivity(i)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })



                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }




}