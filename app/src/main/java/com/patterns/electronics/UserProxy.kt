package com.patterns.electronics

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserProxy : FirebaseProxy {


    lateinit var user_ref : DatabaseReference

    override fun add(vararg ts: Any) {

        user_ref = FirebaseDatabase.getInstance().getReference("User")

        val user = ts[0] as User

        user_ref.push().setValue(user)

    }

    override fun update(obj: String, key: String, value: Any) {
        TODO("Not yet implemented")
    }

    override fun delete(obj: String) {
        TODO("Not yet implemented")
    }


}