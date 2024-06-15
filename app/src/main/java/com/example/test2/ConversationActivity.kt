package com.example.test2

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        //pentru functia de back
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        messages = loadMessages()
        messageAdapter = MessageAdapter(this, messages, dbHelper.getLoggedInUsername().toString())
        messageRecyclerView.adapter = messageAdapter

        //trimitere
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            sendMessage()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
