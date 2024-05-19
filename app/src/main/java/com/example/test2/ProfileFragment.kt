package com.example.tinderlikeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.test2.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val profileNameAge: TextView = view.findViewById(R.id.profile_name_age)
        val profileBio: TextView = view.findViewById(R.id.profile_bio)
        val configureProfileButton: Button = view.findViewById(R.id.configure_profile_button)

        // Set profile image, name and age, bio
        profileImage.setImageResource(R.drawable.profile_picture) // Replace with your image resource
        profileNameAge.text = getString(R.string.user_age)
        profileBio.text = getString(R.string.user_text_description)

        // Set up button click listener
        configureProfileButton.setOnClickListener {
            // Navigate to the profile configuration fragment
            val configureProfileFragment = ConfigureProfileFragment()

            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, configureProfileFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
