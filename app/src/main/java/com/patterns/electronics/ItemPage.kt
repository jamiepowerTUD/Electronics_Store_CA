package com.patterns.electronics

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ItemPage : AppCompatActivity()
{

    lateinit var cart_ref : DatabaseReference
    lateinit var item_ref: DatabaseReference
    lateinit var review_ref : DatabaseReference
    lateinit var user_ref : DatabaseReference

    lateinit var mAuth: FirebaseAuth

    lateinit var intent_name : String

    lateinit var title : TextView
    lateinit var man : TextView
    lateinit var price : TextView
    lateinit var cat : TextView
    lateinit var amount : TextView
    lateinit var color : TextView

    lateinit var image : ImageView
    lateinit var imageURI : String

    lateinit var progress : ProgressDialog





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_page_activity)

        intent_name = intent.getStringExtra("name").toString()
        Log.i("Item Page",intent_name)

        image = findViewById(R.id.item_page_image)
        title = findViewById(R.id.item_page_name)
        man = findViewById(R.id.item_page_man)
        price = findViewById(R.id.item_page_price)
        cat = findViewById(R.id.item_page_cat)
        amount = findViewById(R.id.item_page_amount)
        color = findViewById(R.id.item_page_color)

        progress = ProgressDialog(this)
        progress.show()


        mAuth = FirebaseAuth.getInstance()

        cart_ref = FirebaseDatabase.getInstance().getReference("Cart")
        item_ref = FirebaseDatabase.getInstance().getReference("Item")
        review_ref = FirebaseDatabase.getInstance().getReference("Review")
        user_ref = FirebaseDatabase.getInstance().getReference("User")


        item_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children)
                {
                    if(snap.child("name").value.toString().equals(intent_name))
                    {
                        title.text = snap.child("name").value.toString()
                        man.text = snap.child("manufacturer").value.toString()
                        price.text = snap.child("price").value.toString()
                        cat.text = snap.child("category").value.toString()
                        amount.text = snap.child("amount").value.toString()
                        color.text = snap.child("color").value.toString()

                        imageURI = snap.child("image_uri").value.toString()

                        val storage = FirebaseStorage.getInstance()
                        val gsReference = storage.getReferenceFromUrl(snap.child("image_uri").value.toString())
                        gsReference.downloadUrl.addOnCompleteListener {
                            Picasso.get().load(it.result).into(image) }


                        progress.dismiss()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }




    fun addToCart(v : View) {
        if (amount.text.toString().toInt() == 0)
        {
            Toast.makeText(this,"Item out of Stock",Toast.LENGTH_LONG).show()
        }
        else
        {


            val id = mAuth.currentUser?.uid

            if(amount.text.toString().toInt() > 0 )
            {
                cart_ref.push().setValue(CartItem(id.toString(),title.text.toString(),1,price.text.toString().toDouble(),imageURI))
                Toast.makeText(this,"Added to Basket",Toast.LENGTH_LONG).show()
            }






        }

    }




    fun showFeedBack(v : View)
    {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.allreviews_dialog)
        dialog.show()

        var reviews = ArrayList<Review>()

        review_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children)
                {
                    val product = snap.child("product_name").value.toString()

                    if(product == title.text.toString())
                    {
                        val name = snap.child("name").value.toString()
                        val score = snap.child("score").value.toString().toInt()
                        val date = snap.child("dateTime").value.toString()
                        val com = snap.child("comment").value.toString()

                        reviews.add(Review(name,product,score,com,date))

                        val recycler = dialog.findViewById<RecyclerView>(R.id.review_recycler)
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ItemPage)
                        recycler.layoutManager = mLayoutManager
                        val adapter = reviewAdapter(reviews)
                        recycler.adapter = adapter

                    }



                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })








    }


    fun addReview(v : View)
    {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.submit_review_dialog)
        dialog.show()

        val bar = dialog.findViewById<SeekBar>(R.id.score_seeker)
        bar.progress = 5
        val current = dialog.findViewById<TextView>(R.id.seek_no)
        val submit = dialog.findViewById<Button>(R.id.submit_review)
        val comment = dialog.findViewById<EditText>(R.id.comment_field)


        bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                current.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


        submit.setOnClickListener {


            val id = mAuth.currentUser?.uid

            user_ref.addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children) {

                        if(snap.child("userID").value.toString() == id)
                        {
                            Log.i("Firebase",snap.child("userID").value.toString())

                            val name = snap.child("name").value.toString()
                            val cur = LocalDateTime.now()
                            val formatted = cur.format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))

                            val com = comment.text.toString()
                            val score = bar.progress

                            review_ref.push().setValue(Review(name,title.text.toString(),score,com,formatted))

                            comment.setText("")
                            bar.progress = 5

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