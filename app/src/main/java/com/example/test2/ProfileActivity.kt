package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test2.databinding.ActivityProfileBinding


fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)


        val loggedInUsername = databaseHelper.getLoggedInUsername()
        if (loggedInUsername != null) {
            val userDetails = databaseHelper.getUserDetails(loggedInUsername)
            userDetails?.let {
                binding.editTextText.text = it.name?.toEditable()
                binding.editTextTextEmailAddress.text = it.email?.toEditable()
                binding.editTextTextPostalAddress.text = it.address?.toEditable()
                binding.editTextPhone2.text = it.phoneNumber?.toEditable()
                binding.editTextDate.text = it.birthday?.toEditable()
            }
        }

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