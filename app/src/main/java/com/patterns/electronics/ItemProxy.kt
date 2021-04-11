package com.patterns.electronics

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class ItemProxy : FirebaseProxy {

    lateinit var item_ref : DatabaseReference
    lateinit var cart_ref : DatabaseReference


    lateinit var image_ref : StorageReference
    lateinit var storage : FirebaseStorage


    /**
     *
     * Takes a varag parameter , the item could have an associated image which needs its own
     * parameter
     */
    override fun add(vararg ts: Any) {

        item_ref = FirebaseDatabase.getInstance().getReference("Item")

        when(ts.size)
        {
            1 -> {
                val item = ts[0] as Item

                item_ref.push().setValue(item)
            }
            2 ->
            {
                storage = FirebaseStorage.getInstance()

                val item = ts[0] as Item

                item_ref.push().setValue(item)

                image_ref = storage.getReference(item.name)

                val image = ts[1] as Uri


                image_ref.putFile(image)

            }

        }

    }

    override fun update(obj: String, key: String, value: Any) {

        item_ref = FirebaseDatabase.getInstance().getReference("Item")

        item_ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {
                    val name = snap.child("name").value.toString()

                    if(name == obj)
                    {

                        item_ref.child(snap.key.toString()).child(key).setValue(value)

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun delete(obj : String) {

        item_ref = FirebaseDatabase.getInstance().getReference("Item")

        cart_ref = FirebaseDatabase.getInstance().getReference("Cart")

        item_ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {
                    val name = snap.child("name").value.toString()

                    if(name == obj)
                    {

                        item_ref.child(snap.key.toString()).removeValue()


                        cart_ref.addListenerForSingleValueEvent(object : ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(cart in snapshot.children)
                                {
                                    val cart_name = snap.child("item_name").value.toString()

                                    if(cart_name == obj)
                                    {
                                        cart_ref.child(cart.key.toString()).removeValue()

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
    }





}