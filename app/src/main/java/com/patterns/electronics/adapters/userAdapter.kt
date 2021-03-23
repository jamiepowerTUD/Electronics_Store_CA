package com.patterns.electronics.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.patterns.electronics.*

class userAdapter(val user : ArrayList<User>) : RecyclerView.Adapter<userAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)

        v.setOnClickListener {

            val i = Intent(v.context, UserProfile::class.java)
            val name = v.findViewById<TextView>(R.id.username_field)
            i.putExtra("name",name.text.toString())
            v.context.startActivity(i)

        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bindItems(user[position])
    }

    override fun getItemCount(): Int {
        return user.size
    }


    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(user : User)
        {
            val name = itemView.findViewById(R.id.username_field) as TextView
            val email = itemView.findViewById(R.id.user_email_field) as TextView
            val address = itemView.findViewById(R.id.user_address_field) as TextView
            val image = itemView.findViewById(R.id.user_image) as ImageView

            name.text = user.name
            email.text = user.email
            address.text = user.address
            image.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_user))
        }


    }



}