package com.example.test2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val context: Context, private val chats: List<Chat>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(chat: Chat)
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matchUsernameTextView: TextView = itemView.findViewById(R.id.matchUsernameTextView)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)

        fun bind(chat: Chat) {
            matchUsernameTextView.text = chat.matchUsername
            lastMessageTextView.text = chat.lastMessage
            itemView.setOnClickListener {
                itemClickListener.onItemClick(chat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount() = chats.size
}
