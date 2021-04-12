package com.patterns.electronics.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.patterns.electronics.CartItem
import com.patterns.electronics.R
import com.squareup.picasso.Picasso

class checkoutAdapter(val items : ArrayList<CartItem>) : RecyclerView.Adapter<checkoutAdapter.ViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_list_item, parent, false)

        val remove = v.findViewById<Button>(R.id.remove_cart_button)
        remove.setOnClickListener {

            val name = v.findViewById<TextView>(R.id.cart_item_name)
            val itemName = name.text.toString()


            val cartRef = FirebaseDatabase.getInstance().getReference("Cart")


            for(i in 0 until items.size)
            {
                if(items[i].flyweight.name == itemName)
                {
                   items.removeAt(i)
                    break
                }
            }


            cartRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children)
                    {
                        val cName = snap.child("flyweight").child("name").value.toString()

                        if(cName == itemName)
                        {
                            cartRef.child(snap.key.toString()).removeValue()
                            break
                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            notifyDataSetChanged()



        }

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
      return items.size
    }



    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(item: CartItem)
        {
          val name = itemView.findViewById<TextView>(R.id.cart_item_name)
            val price = itemView.findViewById<TextView>(R.id.cart_item_price)
            val image = itemView.findViewById<ImageView>(R.id.cart_item_image)
            val man = itemView.findViewById<TextView>(R.id.cart_item_man)


            name.text = item.flyweight.name
            price.text = item.price.toString()
            man.text = item.flyweight.manufacturer


            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(item.flyweight.imageURI)
            gsReference.downloadUrl.addOnCompleteListener { Log.i("url",it.result.toString())
                Picasso.get().load(it.result).into(image) }


        }


    }

}

