package com.patterns.electronics

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ItemPage : AppCompatActivity()
{

    lateinit var item_ref: DatabaseReference

    lateinit var intent_name : String

    lateinit var title : TextView
    lateinit var man : TextView
    lateinit var price : TextView
    lateinit var cat : TextView
    lateinit var amount : TextView
    lateinit var color : TextView

    lateinit var image : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_page_activity)

        intent_name = intent.getStringExtra("name").toString()
        Log.i("Item Page",intent_name)

        image = findViewById(R.id.item_page_image)
        title = findViewById(R.id.item_page_name)
        man = findViewById(R.id.item_page_man)
        price = findViewById(R.id.item_page_price)
        cat = findViewById(R.id.item_page_cat)
        amount = findViewById(R.id.item_page_amount)
        color = findViewById(R.id.item_page_color)

        item_ref = FirebaseDatabase.getInstance().getReference("Item")


        item_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children)
                {
                    if(snap.child("name").value.toString().equals(intent_name))
                    {
                        title.text = snap.child("name").value.toString()
                        man.text = snap.child("manufacturer").value.toString()
                        price.text = snap.child("price").value.toString()
                        cat.text = snap.child("category").value.toString()
                        amount.text = snap.child("amount").value.toString()
                        color.text = snap.child("color").value.toString()

                        val storage = FirebaseStorage.getInstance()
                        val gsReference = storage.getReferenceFromUrl(snap.child("image_uri").value.toString())
                        gsReference.downloadUrl.addOnCompleteListener {
                            Picasso.get().load(it.result).into(image) }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }
}