package com.patterns.electronics

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity()
{

    lateinit var emailField : EditText
    lateinit var passwordField : EditText
    lateinit var addressField : EditText
    lateinit var dateField : EditText
    lateinit var cvvField : EditText
    lateinit var numberField : EditText
    lateinit var nameField : EditText

    lateinit var mAuth : FirebaseAuth
    lateinit var users : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        nameField = findViewById(R.id.name_field)
        emailField = findViewById<EditText>(R.id.email_field)
        passwordField = findViewById<EditText>(R.id.password_field)
        addressField = findViewById<EditText>(R.id.shipping_field)
        dateField = findViewById<EditText>(R.id.Expiry_field)
        cvvField = findViewById<EditText>(R.id.cvv_field)
        numberField = findViewById<EditText>(R.id.card_field)


        mAuth = FirebaseAuth.getInstance()
        users = FirebaseDatabase.getInstance().getReference("User")
    }


    fun register(v: View)
    {
        // Ensures date is within 12 months and not before 2021
        val regex = "(0[1-9]|1[0-2])-2[1-9]".toRegex()
        var valid = true


        if(nameField.text.isEmpty())
        {
           nameField.error = "Name Required"
            valid = false
        }


            if(emailField.text.isEmpty())
            {
                emailField.error = "Email Required"
                 valid = false
            }

            if(passwordField.text.isEmpty())
            {
                passwordField.error = "Password Required"
                valid = false
            } else if(passwordField.text.length < 8)
              {
                    passwordField.error = "Password too short (Minimum 8 Characters)"
                  valid = false
               }


            if(addressField.text.isEmpty())
            {
                addressField.error = "Address required"
                valid = false
            }

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
                mAuth.createUserWithEmailAndPassword(
                    emailField.text.toString(),
                    passwordField.text.toString()
                ).addOnCompleteListener{

                    if(it.isSuccessful)
                    {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {

                            val card = "${numberField.text}-${dateField.text}-${cvvField.text}"
                            users.push().setValue(User(user.uid,nameField.text.toString(),emailField.text.toString(),addressField.text.toString(),card,false,false))

                            nameField.setText("")
                            emailField.setText("")
                            passwordField.setText("")
                            numberField.setText("")
                            cvvField.setText("")
                            dateField.setText("")
                            addressField.setText("")



                        }


                    }

                }
            }



    }


}