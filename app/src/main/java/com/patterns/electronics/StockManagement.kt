package com.patterns.electronics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.patterns.electronics.adapters.itemAdapter
import com.patterns.electronics.adapters.managerAdapter

class StockManagement : AppCompatActivity() {




    lateinit var item_ref : DatabaseReference

    lateinit var recycler : RecyclerView

    lateinit var searchField : EditText

    lateinit var current : ArrayList<Item>
    lateinit var items : ArrayList<Item>


    lateinit var name_radio : RadioButton
    lateinit var cat_radio : RadioButton
    lateinit var man_radio : RadioButton

    lateinit var title_order : Button
    lateinit var man_order : Button
    lateinit var price_order : Button

    lateinit var Sorter : SortStrategy


    var titleStatus = false
    var manStatus = false
    var priceStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_management_activity)

        items = ArrayList<Item>()

        current = ArrayList()

        searchField = findViewById(R.id.item_management_search)

        name_radio = findViewById(R.id.manager_name_radio)
        cat_radio = findViewById(R.id.manager_cat_radio)
        man_radio = findViewById(R.id.manager_man_radio)

        title_order = findViewById(R.id.manager_sort_name)
        man_order = findViewById(R.id.manager_sort_man)
        price_order = findViewById(R.id.manager_sort_price)


        val titles = ArrayList<String>()



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

                    Log.i("Item",item.toString())


                    if(!titles.contains(name))
                    {
                        items.add((item))
                        titles.add(name)
                    }




                }

                current.addAll(items)

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





    fun filterManager(v : View)
    {

        var strategy : FilterStrategy
        var temp: ArrayList<Item>

        val search = searchField.text.toString().toLowerCase()



        if (name_radio.isChecked)
        {

            strategy = TitleFilter()
            temp = strategy.filter(search,items)


            current.clear()
            current.addAll(temp)

            val adapter = itemAdapter(temp)
            recycler.adapter = adapter
        }
        else if(cat_radio.isChecked)
        {

            strategy = CategoryFilter()
            temp = strategy.filter(search,items)

            current.clear()
            current.addAll(temp)

            val adapter = itemAdapter(temp)
            recycler.adapter = adapter

        }
        else if(man_radio.isChecked)
        {

            strategy = ManufacturFilter()

            temp = strategy.filter(search,items)


            current.clear()
            current.addAll(temp)

            val adapter = itemAdapter(temp)
            recycler.adapter = adapter
        }




    }


    fun sortManagerName(v : View)
    {
        price_order.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        man_order.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)


        Sorter = TitleSort()


        titleStatus = if(!titleStatus) {
            title_order.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0)
            current = Sorter.ascending(current)
            true
        } else {
            current = Sorter.descending(current)
            title_order.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0)
            false
        }


        val adapter = itemAdapter(current)
        recycler.adapter = adapter

    }

    fun sortManagerManufacturer(v : View)
    {
        Sorter = ManufacturSort()


        title_order.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        price_order.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

        manStatus = if(!manStatus) {
            man_order.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0)
            current = Sorter.ascending(current)
            true
        } else {
            current = Sorter.descending(current)
            man_order.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0)
            false
        }

        val adapter = itemAdapter(current)
        recycler.adapter = adapter
    }


    fun sortManagerPrice(v : View)
    {
        Sorter = PriceSort()

        title_order.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        man_order.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

        priceStatus = if(!priceStatus) {
            price_order.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0)
            current = Sorter.ascending(current)
            true
        } else {
            current = Sorter.descending(current)
            price_order.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0)
            false
        }

        val adapter = itemAdapter(current)
        recycler.adapter = adapter
    }












}