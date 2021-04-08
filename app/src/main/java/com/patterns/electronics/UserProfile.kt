package com.patterns.electronics

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.patterns.electronics.adapters.historyAdapter

class UserProfile : AppCompatActivity()
{

    lateinit var user_ref : DatabaseReference
    lateinit var history_ref : DatabaseReference
    lateinit var mAuth: FirebaseAuth


    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var address : TextView
    lateinit var card : TextView

    lateinit var recycler : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        val intent_name = intent.getStringExtra("name").toString()

        name = findViewById(R.id.user_profile_name)
        email = findViewById(R.id.user_profile_email)
        address = findViewById(R.id.user_profile_address)
        card = findViewById(R.id.user_profile_card)

        recycler = findViewById(R.id.history_recycler)

        mAuth = FirebaseAuth.getInstance()

        val trans = ArrayList<History>()

        user_ref = FirebaseDatabase.getInstance().getReference("User")
        history_ref = FirebaseDatabase.getInstance().getReference("History")

        user_ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

               for(snap in snapshot.children)
               {

                   if(intent_name == snap.child("name").value.toString())
                   {
                       val user_email = snap.child("email").value.toString()
                       val user_address = snap.child("address").value.toString()
                       val user_card = snap.child("card").value.toString()

                       name.text = intent_name
                       email.text = user_email
                       address.text = user_address
                       card.text = user_card





                   }

               }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        history_ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                for(snap in snapshot.children)
                {
                    val user_id = snap.child("user_id").value.toString()

                    val id = mAuth.currentUser?.uid.toString()

                    if(user_id == id)
                    {
                        val name = snap.child("name").value.toString()
                        val price = snap.child("price").value.toString().toDouble()
                        val amount = snap.child("amount").value.toString().toInt()
                        val image = snap.child("image_uri").value.toString()
                        val date = snap.child("datetime").value.toString()


                        trans.add(History(user_id,name,price,amount,image,date))
                    }


                    val adapter = historyAdapter(trans)
                    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@UserProfile)
                    recycler.layoutManager = mLayoutManager
                    recycler.adapter = adapter


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })






    }
}