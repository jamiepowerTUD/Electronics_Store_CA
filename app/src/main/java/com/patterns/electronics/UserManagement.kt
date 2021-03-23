package com.patterns.electronics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.patterns.electronics.adapters.userAdapter

class UserManagement : AppCompatActivity()
{

    lateinit var user_ref : DatabaseReference

    lateinit var recycler : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_management_activity)

        user_ref =  FirebaseDatabase.getInstance().getReference("User")



        val users = ArrayList<User>()

        recycler = findViewById(R.id.user_recycler)


        user_ref.addListenerForSingleValueEvent(object : ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children)
                {
                  val name = snap.child("name").value.toString()
                  val email = snap.child("email").value.toString()
                  val address = snap.child("address").value.toString()

                    users.add(User("",name,email,address,"",false,false))

                }

                val adapter = userAdapter(users)
                val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@UserManagement)
                recycler.layoutManager = mLayoutManager
                recycler.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}