package com.example.test2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConversationActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
   // private lateinit var messageAdapter: MessageAdapter
    private lateinit var messages: MutableList<Message> // Define a Message data class if needed
    private lateinit var matchUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        matchUsername = intent.getStringExtra("MATCH_USERNAME") ?: ""

        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)

        messages = loadMessages() // Load messages from the database or mock data
       // messageAdapter = MessageAdapter(this, messages)
      //  messageRecyclerView.adapter = messageAdapter

        // Handle send button click
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages(): MutableList<Message> {
        // Load messages from the database for the matchUsername
        return mutableListOf() // Replace with actual data retrieval logic
    }

    private fun sendMessage() {
        //val messageEditText: EditText = findViewById(R.id.messageEditText)
        //val messageText = messageEditText.text.toString()

     //   if (messageText.isNotEmpty()) {
      //      val message = Message(/* Define the message fields */)
            // Save the message to the database
       //     messages.add(message)
       //     messageAdapter.notifyItemInserted(messages.size - 1)
       //     messageEditText.text.clear()
        }
    }
