package com.example.test2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(private val context: Context)
    :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

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
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long{
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean{
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


}
