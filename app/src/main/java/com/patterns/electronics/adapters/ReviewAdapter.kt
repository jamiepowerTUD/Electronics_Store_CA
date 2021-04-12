package com.patterns.electronics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patterns.electronics.R
import com.patterns.electronics.Review

class reviewAdapter(val reviews : ArrayList<Review>) : RecyclerView.Adapter<reviewAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.review_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }



    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(review : Review)
        {
            val name = itemView.findViewById(R.id.review_username) as TextView
            val score = itemView.findViewById(R.id.review_score) as TextView
            val date = itemView.findViewById(R.id.review_date) as TextView
            val comment = itemView.findViewById(R.id.review_comment) as TextView

            name.text = review.name
            score.text = "${review.score.toString()} / 5"
            date.text = review.dateTime
            comment.text = review.comment


        }


    }


}