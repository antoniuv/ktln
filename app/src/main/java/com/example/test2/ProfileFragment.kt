package com.example.test2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


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

        // setam datele pentru profil (imagine, varsa, descriere)
        profileImage.setImageResource(R.drawable.profile_picture)
        profileNameAge.text = getString(R.string.user_age)
        profileBio.text = getString(R.string.user_text_description)

        configureProfileButton.setOnClickListener {
            Toast.makeText(activity, "Am apasat pe configureaza profil", Toast.LENGTH_SHORT).show()
        }
    }
}