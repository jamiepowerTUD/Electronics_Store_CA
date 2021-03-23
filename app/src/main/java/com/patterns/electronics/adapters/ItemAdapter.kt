package com.patterns.electronics.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.patterns.electronics.Item
import com.patterns.electronics.ItemPage
import com.patterns.electronics.R
import com.squareup.picasso.Picasso

class itemAdapter(val items : ArrayList<Item>) : RecyclerView.Adapter<itemAdapter.ViewHolder>()
{



    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(item: Item)
        {
            val name = itemView.findViewById(R.id.item_name) as TextView
            val price  = itemView.findViewById(R.id.item_price) as TextView
            val man = itemView.findViewById(R.id.product_man) as TextView
            val cat = itemView.findViewById(R.id.product_cat) as TextView
            val image = itemView.findViewById(R.id.recycler_image) as ImageView
            name.text = item.name
            price.text = "€${item.price}"
            man.text = item.manufacturer
            cat.text = item.category

            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(item.image_uri)
            gsReference.downloadUrl.addOnCompleteListener { Log.i("url",it.result.toString())
                Picasso.get().load(it.result).into(image) }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        v.setOnClickListener {

            val i = Intent(v.context, ItemPage::class.java)
            val name = v.findViewById<TextView>(R.id.item_name)
            i.putExtra("name",name.text.toString())
            v.context.startActivity(i)

        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


}