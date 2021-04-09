package com.patterns.electronics

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File
import java.net.URI


class AddItem : AppCompatActivity() {


    lateinit var title : EditText
    lateinit var manufacturer : EditText
    lateinit var price : EditText
    lateinit var amount : EditText
    lateinit var color : EditText
    lateinit var category : Spinner
    lateinit var item_image : ImageView



    lateinit var proxy : FirebaseProxy

    lateinit var imageUri : Uri

    val REQUEST_CODE = 7777


    val categories = arrayOf("", "Computer", "Phone", "Accessory", "TV")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)

        title = findViewById(R.id.item_title_field)
        manufacturer = findViewById(R.id.item_manufacture_field)
        price = findViewById(R.id.price_field)
        amount = findViewById(R.id.item_quantity)
        category = findViewById(R.id.category_spinner)
        color = findViewById(R.id.item_colour_field)
        item_image = findViewById(R.id.item_image)





        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, categories
        )
        category.adapter = adapter





    }


    fun uploadImage(v: View)
    {

       val intent = Intent()
      intent.type = "image/*"
       intent.action = Intent.ACTION_GET_CONTENT
       startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE)




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            RESULT_OK -> {
                Log.i("Image", "Image Found")
                imageUri = data?.data!!
                Picasso.get().load(imageUri).into(item_image)



            }


        }

    }




    fun addItem(v: View)
    {
        var valid = true

        if(title.text.isEmpty())
        {
            title.error = "Title Required"
            valid = false
        }

        if(color.text.isEmpty())
        {
            color.error = "Title Required"
            valid = false
        }



        if(manufacturer.text.isEmpty())
        {
            manufacturer.error = "Manufacturer Required"
            valid = false
        }

        if(price.text.isEmpty())
        {
            price.error = "Price Required"
            valid = false
        }

        if(amount.text.isEmpty())
        {
            manufacturer.error = "Amount Required"
            valid = false
        }

        if(category.selectedItem.toString() == "")
        {
            Toast.makeText(this, "Category Required", Toast.LENGTH_LONG).show()
            valid = false
        }


        if(!this::imageUri.isInitialized)
        {
            Toast.makeText(this, "Image not uploaded", Toast.LENGTH_LONG).show()
            valid = false
        }


        if(valid)
        {


            proxy = ItemProxy()

            val item = Item(title.text.toString(),price.text.toString().toDouble(),amount.text.toString().toInt(),manufacturer.text.toString(),color.text.toString(),category.selectedItem.toString(),"gs://electronics-store-ca.appspot.com/${title.text}")

            proxy.add(item,imageUri)

            title.setText("")
            price.setText("")
            amount.setText("")
            color.setText("")
            manufacturer.setText("")
            category.setSelection(0)
            item_image.setImageResource(0)
            Toast.makeText(this,"Item Added", Toast.LENGTH_SHORT).show()


        }


    }
}