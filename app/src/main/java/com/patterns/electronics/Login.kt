package com.patterns.electronics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Login : AppCompatActivity()
{

    lateinit var email : EditText
    lateinit var password : EditText

    lateinit var mAuth : FirebaseAuth

    lateinit var user_ref : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_pass)
        mAuth = FirebaseAuth.getInstance()

        email.setText("jamie@mail.com")
        password.setText("samplepass")

        user_ref = FirebaseDatabase.getInstance().getReference("User")

    }


    fun login(v : View)
    {
        if(email.text.isEmpty())
        {
            email.error = "Email Required"
        }
        else if(password.text.isEmpty())
        {
            password.error = "Password Required"
        }
        else
        {
            Log.i("Firebase","Got this far")

            mAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnSuccessListener{

                val id = it.user?.uid.toString()



                user_ref.addListenerForSingleValueEvent(object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children)
                        {
                            val cid = snap.child("userID").value.toString()

                            if(id == cid){

                                val admin = snap.child("admin").value.toString().toBoolean()

                                val name = snap.child("name").value.toString()

                                val sharedPreference =  getSharedPreferences("username",
                                    Context.MODE_PRIVATE)
                                val editor = sharedPreference.edit()
                                editor.putString("name",name)
                                editor.apply()


                                if(admin)
                                {
                                    val i = Intent(this@Login, AdminConsole::class.java)
                                    startActivity(i)
                                }
                                else
                                {
                                    val i = Intent(this@Login, Homepage::class.java)
                                    startActivity(i)
                                }


                            }


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })



            }.addOnFailureListener {

                Toast.makeText(this,"Login Unsuccessful",Toast.LENGTH_LONG).show()
            }




        }


    }






}