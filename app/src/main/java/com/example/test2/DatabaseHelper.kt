package com.example.test2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.math.max

data class UserDetails(
    var name: String?,
    var email: String?,
    var address: String?,
    var phoneNumber: String?,
    var birthday: String?
)

data class Message(
    val sender: String,
    val receiver: String,
    val content: String,
    val timestamp: Long
)

data class Chat(
    val matchUsername: String,
    val lastMessage: String,
    val timestamp: Long
)



class DatabaseHelper(private val context: Context)
    :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_BIRTHDAY = "birthday"

        // Matches table
        private const val TABLE_MATCHES = "matches"
        private const val COLUMN_USER1 = "user1"
        private const val COLUMN_USER2 = "user2"

        // Messages table
        private const val TABLE_MESSAGES = "messages"
        private const val COLUMN_SENDER = "sender"
        private const val COLUMN_RECEIVER = "receiver"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_PHONE_NUMBER TEXT, " +
                "$COLUMN_BIRTHDAY TEXT)")

        val createMatchesTable = ("CREATE TABLE $TABLE_MATCHES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER1 TEXT, " +
                "$COLUMN_USER2 TEXT)")

        val createMessagesTable = ("CREATE TABLE $TABLE_MESSAGES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_SENDER TEXT, " +
                "$COLUMN_RECEIVER TEXT, " +
                "$COLUMN_CONTENT TEXT, " +
                "$COLUMN_TIMESTAMP INTEGER)")

        db?.execSQL(createTableQuery)
        db?.execSQL(createMatchesTable)
        db?.execSQL(createMessagesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MATCHES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        if (userExists) {
            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("loggedInUsername", username)
                apply()
            }
        }
        cursor.close();
        return userExists
    }

    fun getLoggedInUsername(): String? {
        val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPref.getString("loggedInUsername", null)
    }

    fun updateUserDetails(
        name: String,
        email: String,
        address: String,
        phoneNumber: String,
        birthday: String
    ): Int {
        return try {
            val username = getLoggedInUsername()
            if (username != null) {
                val values = ContentValues().apply {
                    put(COLUMN_NAME, name)
                    put(COLUMN_EMAIL, email)
                    put(COLUMN_ADDRESS, address)
                    put(COLUMN_PHONE_NUMBER, phoneNumber)
                    put(COLUMN_BIRTHDAY, birthday)
                }
                val db = writableDatabase
                val selection = "$COLUMN_USERNAME = ?"
                val selectionArgs = arrayOf(username)
                db.update(TABLE_NAME, values, selection, selectionArgs)
            } else {
                Log.e("DatabaseHelper", "No logged-in user found")
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DatabaseHelper", "Error updating user details", e)
            0
        }
    }

    fun getUserDetails(username: String): UserDetails? {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        return if (cursor.moveToFirst()) {
            val userDetails = UserDetails(
                name = cursor.getString(max(cursor.getColumnIndex(COLUMN_NAME), 0)),
                email = cursor.getString(max(cursor.getColumnIndex(COLUMN_EMAIL), 0)),
                address = cursor.getString(max(cursor.getColumnIndex(COLUMN_ADDRESS), 0)),
                phoneNumber = cursor.getString(max(cursor.getColumnIndex(COLUMN_PHONE_NUMBER), 0)),
                birthday = cursor.getString(max(cursor.getColumnIndex(COLUMN_BIRTHDAY), 0))
            )
            cursor.close()
            userDetails
        } else {
            cursor.close()
            null
        }
    }

    // Insert match
    fun insertMatch(user1: String, user2: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USER1, user1)
            put(COLUMN_USER2, user2)
        }
        val db = writableDatabase
        return db.insert(TABLE_MATCHES, null, values)
    }

    // Check if match exists
    fun isMatch(user1: String, user2: String): Boolean {
        val db = readableDatabase
        val selection = "(($COLUMN_USER1 = ? AND $COLUMN_USER2 = ?) OR ($COLUMN_USER1 = ? AND $COLUMN_USER2 = ?))"
        val selectionArgs = arrayOf(user1, user2, user2, user1)
        val cursor = db.query(TABLE_MATCHES, null, selection, selectionArgs, null, null, null)

        val matchExists = cursor.count > 0
        cursor.close()
        return matchExists
    }

    // Insert message
    fun insertMessage(sender: String, receiver: String, content: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_SENDER, sender)
            put(COLUMN_RECEIVER, receiver)
            put(COLUMN_CONTENT, content)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        val db = writableDatabase
        return db.insert(TABLE_MESSAGES, null, values)
    }

    // Get chat messages
    fun getMessages(user1: String?, user2: String): List<Message> {
        val db = readableDatabase
        val selection = "(($COLUMN_SENDER = ? AND $COLUMN_RECEIVER = ?) OR ($COLUMN_SENDER = ? AND $COLUMN_RECEIVER = ?))"
        val selectionArgs = arrayOf(user1, user2, user2, user1)
        val cursor = db.query(TABLE_MESSAGES, null, selection, selectionArgs, null, null, "$COLUMN_TIMESTAMP ASC")

        val messages = mutableListOf<Message>()
        while (cursor.moveToNext()) {
            val message = Message(
                sender = cursor.getString(max(cursor.getColumnIndex(COLUMN_SENDER),0)),
                receiver = cursor.getString(max(cursor.getColumnIndex(COLUMN_RECEIVER),0)),
                content = cursor.getString(max(cursor.getColumnIndex(COLUMN_CONTENT),0)),
                timestamp = cursor.getLong(max(cursor.getColumnIndex(COLUMN_TIMESTAMP),0))
            )
            messages.add(message)
        }
        cursor.close()
        return messages
    }

    // Get matches for logged-in user
    fun getMatches(username: String): List<String> {
        val db = readableDatabase
        val selection = "$COLUMN_USER1 = ? OR $COLUMN_USER2 = ?"
        val selectionArgs = arrayOf(username, username)
        val cursor = db.query(TABLE_MATCHES, null, selection, selectionArgs, null, null, null)

        val matches = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val user1 = cursor.getString(max(cursor.getColumnIndex(COLUMN_USER1),0))
            val user2 = cursor.getString(max(cursor.getColumnIndex(COLUMN_USER2),0))
            if (user1 == username) {
                matches.add(user2)
            } else {
                matches.add(user1)
            }
        }
        cursor.close()
        return matches
    }

    // Get latest message between two users
    fun getLatestMessage(user1: String, user2: String): Message? {
        val db = readableDatabase
        val selection = "(($COLUMN_SENDER = ? AND $COLUMN_RECEIVER = ?) OR ($COLUMN_SENDER = ? AND $COLUMN_RECEIVER = ?))"
        val selectionArgs = arrayOf(user1, user2, user2, user1)
        val cursor = db.query(TABLE_MESSAGES, null, selection, selectionArgs, null, null, "$COLUMN_TIMESTAMP DESC", "1")

        return if (cursor.moveToFirst()) {
            val message = Message(
                sender = cursor.getString(max(cursor.getColumnIndex(COLUMN_SENDER),0)),
                receiver = cursor.getString(max(cursor.getColumnIndex(COLUMN_RECEIVER),0)),
                content = cursor.getString(max(cursor.getColumnIndex(COLUMN_CONTENT),0)),
                timestamp = cursor.getLong(max(cursor.getColumnIndex(COLUMN_TIMESTAMP),0))
            )
            cursor.close()
            message
        } else {
            cursor.close()
            null
        }
    }

    fun loadChats(): List<Chat> {
        val loggedInUsername = getLoggedInUsername() ?: return emptyList()
        Log.d("DatabaseHelper", "Logged in user: $loggedInUsername")
        val matches = getMatches(loggedInUsername)
        Log.d("DatabaseHelper", "Matches found: $matches")
        val chats = mutableListOf<Chat>()

        for (match in matches) {
            val latestMessage = getLatestMessage(loggedInUsername, match)
            Log.d("DatabaseHelper", "Latest message for match $match: $latestMessage")
            if (latestMessage != null) {
                val chat = Chat(
                    matchUsername = match,
                    lastMessage = latestMessage.content,
                    timestamp = latestMessage.timestamp
                )
                chats.add(chat)
            }
            else {
                val chat = Chat(
                    matchUsername = match,
                    lastMessage = "",
                    timestamp = 1
                )
                chats.add(chat)
            }
        }

        val sortedChats = chats.sortedByDescending { it.timestamp }
        Log.d("DatabaseHelper", "Sorted chats: $sortedChats")
        return sortedChats
    }


}