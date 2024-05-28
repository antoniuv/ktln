package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test2.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.buttonprofilesubmit.setOnClickListener {

            val birthday = binding.editTextDate.text.toString()
            val name = binding.editTextText.text.toString()
            val email = binding.editTextTextEmailAddress.text.toString()
            val phone = binding.editTextPhone2.text.toString()
            val address = binding.editTextTextPostalAddress.text.toString()

            databaseHelper.updateUserDetails(name,email,address,phone,birthday)
            val result = databaseHelper.updateUserDetails(name, email, address, phone, birthday)
            if (result > 0) {
                Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update user details", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}