package com.example.test2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConversationActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messages: MutableList<Message>
    private lateinit var matchUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        matchUsername = intent.getStringExtra("MATCH_USERNAME") ?: ""

        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        messages = loadMessages()
        messageAdapter = MessageAdapter(this, messages, dbHelper.getLoggedInUsername().toString())
        messageRecyclerView.adapter = messageAdapter

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages(): MutableList<Message> {
        val dbHelper = DatabaseHelper(this)
        return dbHelper.getMessages(dbHelper.getLoggedInUsername(), matchUsername).toMutableList()
    }

    private fun sendMessage() {
        val messageEditText: EditText = findViewById(R.id.messageEditText)
        val messageText = messageEditText.text.toString()
        val dbHelper = DatabaseHelper(this)

        if (messageText.isNotEmpty()) {
            val message = Message(
                sender = dbHelper.getLoggedInUsername().toString(),
                receiver = matchUsername,
                content = messageText,
                timestamp = System.currentTimeMillis()
            )

            dbHelper.insertMessage(message.sender, message.receiver, message.content)

            messages.add(message)
            messageAdapter.notifyItemInserted(messages.size - 1)
            messageRecyclerView.scrollToPosition(messages.size - 1)
            messageEditText.text.clear()
        }
    }
}
