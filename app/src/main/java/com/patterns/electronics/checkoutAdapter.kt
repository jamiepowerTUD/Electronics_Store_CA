package com.patterns.electronics

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class checkoutAdapter(val items : ArrayList<CartItem>) : RecyclerView.Adapter<checkoutAdapter.ViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_list_item, parent, false)
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
            val amount = itemView.findViewById<TextView>(R.id.cart_item_amount)
            val image = itemView.findViewById<ImageView>(R.id.cart_item_image)

            name.text = item.item_name
            price.text = item.price.toString()
            amount.text = item.amount.toString()


            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(item.imageURI)
            gsReference.downloadUrl.addOnCompleteListener { Log.i("url",it.result.toString())
                Picasso.get().load(it.result).into(image) }


        }


    }

}

