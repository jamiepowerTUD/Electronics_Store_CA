package com.patterns.electronics

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class UserProfile : AppCompatActivity()
{

    lateinit var user_ref : DatabaseReference


    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var address : TextView
    lateinit var card : TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        val intent_name = intent.getStringExtra("name").toString()

        name = findViewById(R.id.user_profile_name)
        email = findViewById(R.id.user_profile_email)
        address = findViewById(R.id.user_profile_address)
        card = findViewById(R.id.user_profile_card)


        user_ref = FirebaseDatabase.getInstance().getReference("User")
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






    }
}