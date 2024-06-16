package com.example.test2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class SwipeUser(val username: String, val name: String, val email: String)

class SwipeAdapter(private val context: Context, private val users: List<SwipeUser>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.item_swipe_user, parent, false)
        } else {
            view = convertView
        }

        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val emailTextView: TextView = view.findViewById(R.id.emailTextView)

        val user = users[position]

        usernameTextView.text = user.username
        nameTextView.text = user.name
        emailTextView.text = user.email

        return view
    }
}
