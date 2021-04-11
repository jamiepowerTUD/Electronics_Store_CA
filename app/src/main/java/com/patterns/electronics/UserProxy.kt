package com.patterns.electronics

import com.google.firebase.database.*

class UserProxy : FirebaseProxy {


    lateinit var user_ref : DatabaseReference

    override fun add(vararg ts: Any) {

        user_ref = FirebaseDatabase.getInstance().getReference("User")

        val user = ts[0] as User

        user_ref.push().setValue(user)

    }

    override fun update(obj: String, key: String, value: Any) {


        user_ref = FirebaseDatabase.getInstance().getReference("User")

        user_ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {
                    val id = snap.child("userID").value.toString()

                    if(id == obj)
                    {

                        user_ref.child(snap.key.toString()).child(key).setValue(value)

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }




    override fun delete(obj: String) {

        user_ref = FirebaseDatabase.getInstance().getReference("User")

        val history_ref = FirebaseDatabase.getInstance().getReference("History")

        user_ref.addListenerForSingleValueEvent(object  : ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {

                    val id = snap.child("userID").value.toString()

                    if(obj == id)
                    {
                        user_ref.child(snap.key.toString()).removeValue()


                        history_ref.addListenerForSingleValueEvent(object : ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {

                                for(hist in snapshot.children)
                                {
                                    val hid = hist.child("user_id").value.toString()

                                    if(hid == obj)
                                    {
                                      history_ref.child(hist.key.toString()).removeValue()
                                    }


                                }



                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })



                    }


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        TODO("Not yet implemented")
    }


}