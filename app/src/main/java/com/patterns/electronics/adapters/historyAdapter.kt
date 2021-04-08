package com.patterns.electronics.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.patterns.electronics.CartItem
import com.patterns.electronics.History
import com.patterns.electronics.R
import com.squareup.picasso.Picasso

class historyAdapter(val trans : ArrayList<History>) : RecyclerView.Adapter<historyAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(trans[position])
    }

    override fun getItemCount(): Int {
      return  trans.size
    }


    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(item: History)
        {
            val name = itemView.findViewById<TextView>(R.id.history_item_name)
            val price = itemView.findViewById<TextView>(R.id.history_item_price)
            val amount = itemView.findViewById<TextView>(R.id.history_item_amount)
            val date = itemView.findViewById<TextView>(R.id.history_item_date)
            val image = itemView.findViewById<ImageView>(R.id.history_item_image)

            name.text = item.name
            price.text = "€ " + item.price.toString()
            amount.text = "QTY : " +item.amount.toString()
            date.text = item.datetime

            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(item.image_uri)
            gsReference.downloadUrl.addOnCompleteListener { Log.i("url",it.result.toString())
                Picasso.get().load(it.result).into(image) }




        }


    }



}