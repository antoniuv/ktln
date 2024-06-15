package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatFragment : Fragment(), ChatAdapter.OnItemClickListener {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chats: MutableList<Chat>
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        //initializeaza baza de daet
        dbHelper = DatabaseHelper(requireContext())

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(context)

        //de aici adauga conversatiile
        chats = loadChats()
        Log.d("ChatFragment", "Chats loaded: $chats")
        chatAdapter = ChatAdapter(requireContext(), chats, this)
        chatRecyclerView.adapter = chatAdapter

        return view
    }

    private fun loadChats(): MutableList<Chat> {
        return dbHelper.loadChats().toMutableList()
    }

    override fun onItemClick(chat: Chat) {
        val intent = Intent(context, ConversationActivity::class.java)
        intent.putExtra("MATCH_USERNAME", chat.matchUsername)
        startActivity(intent)
    }
}

