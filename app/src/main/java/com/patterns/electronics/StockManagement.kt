package com.patterns.electronics

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.patterns.electronics.adapters.managerAdapter

class StockManagement : AppCompatActivity() {




    lateinit var item_ref : DatabaseReference

    lateinit var recycler : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_management_activity)

        val items = ArrayList<Item>()


        recycler = findViewById(R.id.manager_recycler)

        item_ref = FirebaseDatabase.getInstance().getReference("Item")

        item_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    Log.i("Firebase", snap.value.toString())
                    Log.i("Firebase", snap.child("name").value.toString())
                    val name = snap.child("name").value.toString()
                    val price = snap.child("price").value.toString().toDouble()
                    val amount = snap.child("amount").value.toString().toInt()
                    val color = snap.child("color").value.toString()
                    val category = snap.child("category").value.toString()
                    val man = snap.child("manufacturer").value.toString()
                    val image_uri = snap.child("image_uri").value.toString()

                    val item = Item(name, price, amount, man, color, category, image_uri)

                    items.add((item))

                }

                val adapter = managerAdapter(items)
                val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@StockManagement)
                recycler.layoutManager = mLayoutManager
                recycler.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}