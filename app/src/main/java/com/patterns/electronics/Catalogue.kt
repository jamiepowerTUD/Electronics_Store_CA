package com.patterns.electronics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.patterns.electronics.adapters.itemAdapter

class Catalogue : AppCompatActivity() {

    lateinit var items: ArrayList<Item>


    lateinit var item_ref: DatabaseReference
    lateinit var recycler : RecyclerView

    lateinit var searchField : EditText

    lateinit var name_radio : RadioButton
    lateinit var cat_radio : RadioButton
    lateinit var man_radio : RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.catalogue_activity)

        name_radio = findViewById(R.id.name_radio)
        cat_radio = findViewById(R.id.cat_radio)
        man_radio = findViewById(R.id.man_radio)


        searchField = findViewById(R.id.search_box)

        items = ArrayList()

        recycler = findViewById(R.id.item_recycler)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = mLayoutManager
        val adapter = itemAdapter(items)
        recycler.adapter = adapter

        item_ref = FirebaseDatabase.getInstance().getReference("Item")

        item_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(snap in snapshot.children)
                {
                    Log.i("Firebase",snap.value.toString())
                    Log.i("Firebase",snap.child("name").value.toString())
                    val name = snap.child("name").value.toString()
                    val price = snap.child("price").value.toString().toDouble()
                    val amount = snap.child("amount").value.toString().toInt()
                    val color = snap.child("color").value.toString()
                    val category = snap.child("category").value.toString()
                    val man  = snap.child("manufacturer").value.toString()
                    val image_uri = snap.child("image_uri").value.toString()

                    val item = Item(name,price,amount,man,color,category,image_uri)

                    items.add(item)
                    adapter.notifyDataSetChanged()
                }





            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }



    fun filter(v : View)
    {
        val temp = ArrayList<Item>()
        val search = searchField.text.toString().toLowerCase()
        if (name_radio.isChecked)
        {

            for(i in items)
            {
                if(i.name.toLowerCase().contains(search))
                {
                    temp.add(i)
                }

            }

            val adapter = itemAdapter(temp)
            recycler.adapter = adapter
        }
        else if(cat_radio.isChecked)
        {
            for(i in items)
            {
                if(i.category.toLowerCase().contains(search))
                {
                    temp.add(i)
                }
            }

            val adapter = itemAdapter(temp)
            recycler.adapter = adapter

        }
        else if(man_radio.isChecked)
        {
            for(i in items)
            {
                if(i.manufacturer.equals(search,ignoreCase = true))
                {
                    temp.add(i)
                }
            }

            val adapter = itemAdapter(temp)
            recycler.adapter = adapter
        }



    }



}