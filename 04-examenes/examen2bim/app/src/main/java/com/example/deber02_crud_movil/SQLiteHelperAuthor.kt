package com.example.deber02_crud_movil

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelperAuthor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AuthorsDatabase"

        private const val TABLE_AUTHORS = "authors"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_NATIONALITY = "nationality"
        private const val KEY_BIRTHDATE = "birthdate"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_AUTHORS_TABLE = ("CREATE TABLE $TABLE_AUTHORS("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_NAME TEXT,"
                + "$KEY_NATIONALITY TEXT,"
                + "$KEY_BIRTHDATE TEXT)")
        db.execSQL(CREATE_AUTHORS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHORS")
        onCreate(db)
    }

    // CRUD Operations
    fun addAuthor(author: Author) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, author.name)
            put(KEY_NATIONALITY, author.nationality)
            put(KEY_BIRTHDATE, author.birthdate)
        }

        db.insert(TABLE_AUTHORS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAuthor(id: Int): Author? {
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_AUTHORS,
            arrayOf(KEY_ID, KEY_NAME, KEY_NATIONALITY, KEY_BIRTHDATE),
            "$KEY_ID=?",
            arrayOf(id.toString()),
            null, null, null, null
        )

        if (cursor != null)
            cursor.moveToFirst()

        return cursor?.let {
            Author(
                it.getInt(it.getColumnIndex(KEY_ID)),
                it.getString(it.getColumnIndex(KEY_NAME)),
                it.getString(it.getColumnIndex(KEY_NATIONALITY)),
                it.getString(it.getColumnIndex(KEY_BIRTHDATE))
            )
        }
    }

    @SuppressLint("Range")
    fun getAllAuthors(): List<Author> {
        val list = ArrayList<Author>()
        val selectQuery = "SELECT * FROM $TABLE_AUTHORS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val author = Author(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_NATIONALITY)),
                    cursor.getString(cursor.getColumnIndex(KEY_BIRTHDATE))
                )
                list.add(author)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    fun updateAuthor(author: Author): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, author.name)
            put(KEY_NATIONALITY, author.nationality)
            put(KEY_BIRTHDATE, author.birthdate)
        }

        val success =  db.update(
            TABLE_AUTHORS,
            values,
            "$KEY_ID = ?",
            arrayOf(author.id.toString())
        )

        db.close()

        return success
    }

    fun deleteAuthor(author: Author) {
        val db = this.writableDatabase
        db.delete(TABLE_AUTHORS, "$KEY_ID = ?", arrayOf(author.id.toString()))
        db.close()
    }
}
