package com.example.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lorentzos.flingswipe.SwipeFlingAdapterView

class HomeFragment : Fragment() {

    private lateinit var swipeView: SwipeFlingAdapterView
    private lateinit var adapter: SwipeAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var users: MutableList<SwipeUser>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dbHelper = DatabaseHelper(requireContext())
        swipeView = view.findViewById(R.id.frame)
        users = loadUsers()

        adapter = SwipeAdapter(requireContext(), users)
        swipeView.adapter = adapter

        swipeView.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                users.removeAt(0)
                adapter.notifyDataSetChanged()
            }

            override fun onLeftCardExit(dataObject: Any) {
                val user = dataObject as SwipeUser
                Toast.makeText(context, "Disliked ${user.username}", Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(dataObject: Any) {
                val user = dataObject as SwipeUser
                Toast.makeText(context, "Liked ${user.username}", Toast.LENGTH_SHORT).show()
                handleMatch(user.username)
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                // De scris
                // In momentul in care nu mai sunt uilizatori
            }

            override fun onScroll(scrollProgressPercent: Float) {}

        })

        return view
    }

    private fun loadUsers(): MutableList<SwipeUser> {
        //Date de test, de schimbat
        return mutableListOf(
            SwipeUser("user1", "John Doe", "john@example.com"),
            SwipeUser("zbording", "Indiana Jones", "nam"),
            SwipeUser("maradona", "haha", "nu"),
            SwipeUser("user2", "Jane Smith", "jane@example.com")
        )
    }

    private fun handleMatch(username: String) {
        val loggedInUsername = dbHelper.getLoggedInUsername()
        if (loggedInUsername != null && !dbHelper.isMatch(loggedInUsername, username)) {
            dbHelper.insertMatch(loggedInUsername, username)
        }
    }
}
