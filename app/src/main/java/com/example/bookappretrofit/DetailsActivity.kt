package com.example.bookappretrofit

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookappretrofit.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var book: BookItem
    private var phoneNumber: String = "0729635783"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting favourite icon to not be clickable in Details Activity
        binding.favourite.isClickable = false

        // Adding call service
        binding.btnCall.setOnClickListener {
            val phoneCallIntent = Intent(Intent.ACTION_DIAL)
            phoneCallIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(phoneCallIntent)
        }
    }
}