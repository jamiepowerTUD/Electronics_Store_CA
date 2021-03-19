package com.patterns.electronics

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity()
{

    lateinit var email : EditText
    lateinit var password : EditText

    lateinit var mAuth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_pass)
        mAuth = FirebaseAuth.getInstance()

        email.setText("jamie@mail.com")
        password.setText("samplepass")

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
            mAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnSuccessListener{

                Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                val i = Intent(this , AdminConsole::class.java)
                startActivity(i)

            }.addOnFailureListener {

                Toast.makeText(this,"Login Unsuccessful",Toast.LENGTH_LONG).show()
            }




        }


    }






}