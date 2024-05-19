package com.example.tinderlikeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.test2.R

class ConfigureProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configure_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editName: EditText = view.findViewById(R.id.edit_profile_name)
        val editAge: EditText = view.findViewById(R.id.edit_profile_age)
        val editBio: EditText = view.findViewById(R.id.edit_profile_bio)
        val saveProfileButton: Button = view.findViewById(R.id.save_profile_button)

        saveProfileButton.setOnClickListener {
            val name = editName.text.toString()
            val age = editAge.text.toString()
            val bio = editBio.text.toString()

            // Validate input
            if (name.isBlank() || age.isBlank() || bio.isBlank()) {
                Toast.makeText(activity, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save profile information (you can save to a database or shared preferences)
                Toast.makeText(activity, "Profile saved", Toast.LENGTH_SHORT).show()

                // Hide the fields
                editName.visibility = View.GONE
                editAge.visibility = View.GONE
                editBio.visibility = View.GONE
                saveProfileButton.visibility = View.GONE

                // Optionally, navigate back to the profile fragment or update the profile information directly
                parentFragmentManager.popBackStack()
            }
        }
    }
}
