package com.patterns.electronics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Discount : AppCompatActivity()
{


    lateinit var codeField : EditText


    lateinit var discount_ref : DatabaseReference
    lateinit var user_ref : DatabaseReference
    lateinit var mAuth : FirebaseAuth

    lateinit var image : ImageView
    lateinit var status : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discount_activity)

        codeField = findViewById(R.id.code_field)
        status = findViewById(R.id.applied_display)

        mAuth = FirebaseAuth.getInstance()

        image = findViewById(R.id.code_status)

        discount_ref = FirebaseDatabase.getInstance().getReference("Discount")
        user_ref = FirebaseDatabase.getInstance().getReference("User")

        val uid = mAuth.currentUser?.uid

        user_ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children)
                {
                    val id = snap.child("userID").value.toString()

                    if(uid == id)
                    {
                        val dis = snap.child("discount").value.toString().toBoolean()

                        if(dis)
                        {
                            image.setImageResource(R.drawable.ic_checked)
                            status.text = "Discount Applied"
                        }

                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }



    fun validateCode(v : View)
    {
        var found = false

        if(codeField.text.isEmpty())
        {
            codeField.error = "Code field must not be empty"
        }
        else{

            val id = mAuth.currentUser?.uid.toString()

            val code = codeField.text.toString()

            discount_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                   val dis = snapshot.value.toString()



                    if(code == dis)
                    {
                        user_ref.addListenerForSingleValueEvent(object : ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {

                                for(snap in snapshot.children)
                                {
                                    val cid = snap.child("userID").value.toString()

                                    Log.i("Firebase","User found $cid")

                                    if(cid == id)
                                    {

                                        found = true
                                        user_ref.child(snap.key.toString()).child("discount").setValue(true)
                                        image.setImageResource(R.drawable.ic_checked)
                                        status.text = "Discount Applied"
                                        codeField.setText("")

                                    }


                                    if(!found)
                                        Toast.makeText(this@Discount,"Invalid Discount Code",Toast.LENGTH_SHORT).show()

                                }
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

}