package com.patterns.electronics

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

    lateinit var id : String


    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var address : TextView
    lateinit var card : TextView

    lateinit var recycler : RecyclerView

    lateinit var proxy : FirebaseProxy

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

        proxy = UserProxy()

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

                    val cid = mAuth.currentUser?.uid.toString()

                    if(user_id == cid)
                    {
                        id = cid

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





    fun updateCard(v : View)
    {
        var valid = true

        // Ensures date is within 12 months and not before 2021
        val regex = "(0[1-9]|1[0-2])-2[1-9]".toRegex()

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.update_card_dialog)

        val update = dialog.findViewById<Button>(R.id.submit_card_update)

        val numberField = dialog.findViewById<EditText>(R.id.update_card_number_field)

        val dateField = dialog.findViewById<EditText>(R.id.update_card_expiry_field)

        val cvvField = dialog.findViewById<EditText>(R.id.update_card_cvv_field)


        update.setOnClickListener {

            if(dateField.text.isEmpty())
            {
                dateField.error = "Expiry Date Required"
                valid = false
            } else if(!regex.containsMatchIn(dateField.text))
            {
                dateField.error = "Date format invalid (MM-YY)"
                valid = false
            }

            if(numberField.text.isEmpty())
            {
                numberField.error = "Card Number required"
                valid = false
            }
            else if(numberField.text.length != 16)
            {
                numberField.error = "Card Number must be 16 digits"
                valid = false
            }


            if(cvvField.text.isEmpty())
            {
                cvvField.error = "CVV required"
                valid = false
            }
            else if(cvvField.text.length < 3 || cvvField.text.length > 3)
            {
                cvvField.error = "CVV must only be 3 characters"
                valid = false
            }


            if (valid)
            {

                proxy.update(id,"card","${numberField.text.toString()}-${dateField.text.toString()}-${cvvField.text.toString()}")

                card.text = "${numberField.text.toString()}-${dateField.text.toString()}-${cvvField.text.toString()}"
                Toast.makeText(this,"Card Updated",Toast.LENGTH_SHORT).show()
                dialog.dismiss()


            }
        }


        dialog.show()

    }



    fun deleteUser(v : View)
    {
        proxy.delete(id)
    }






}