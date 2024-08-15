package com.example.deber02_crud_movil

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SQLiteHelperBook(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BooksDatabase"

        private const val TABLE_BOOKS = "books"
        private const val KEY_BOOK_ID = "id"
        private const val KEY_BOOK_TITLE = "title"
        private const val KEY_BOOK_PUBLICATION_DATE = "publication_date"
        private const val KEY_BOOK_GENRE = "genre"
        private const val KEY_BOOK_AUTHOR_ID = "author_id"  // Assume there's a table for authors or pass author ID externally
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_BOOKS_TABLE = ("CREATE TABLE $TABLE_BOOKS(" +
                "$KEY_BOOK_ID INTEGER PRIMARY KEY," +
                "$KEY_BOOK_TITLE TEXT," +
                "$KEY_BOOK_PUBLICATION_DATE TEXT," +
                "$KEY_BOOK_GENRE TEXT," +
                "$KEY_BOOK_AUTHOR_ID INTEGER," +
                "FOREIGN KEY($KEY_BOOK_AUTHOR_ID) REFERENCES authors(id))")  // Update 'authors' to the actual authors table name if exists

        db.execSQL(CREATE_BOOKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    fun addBook(book: Book) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_BOOK_TITLE, book.title)
            put(KEY_BOOK_PUBLICATION_DATE, book.fechaPublicacion)
            put(KEY_BOOK_GENRE, book.genero)
            put(KEY_BOOK_AUTHOR_ID, book.authorid)
        }

        db.insert(TABLE_BOOKS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getBook(bookId: Int): Book? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            arrayOf(KEY_BOOK_ID, KEY_BOOK_TITLE, KEY_BOOK_PUBLICATION_DATE, KEY_BOOK_GENRE, KEY_BOOK_AUTHOR_ID),
            "$KEY_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )
        var book: Book? = null
        if (cursor.moveToFirst()) {
            book = Book(
                id = cursor.getInt(cursor.getColumnIndex(KEY_BOOK_ID)),
                title = cursor.getString(cursor.getColumnIndex(KEY_BOOK_TITLE)),
                fechaPublicacion = cursor.getString(cursor.getColumnIndex(KEY_BOOK_PUBLICATION_DATE)),
                genero = cursor.getString(cursor.getColumnIndex(KEY_BOOK_GENRE)),
                authorid = cursor.getInt(cursor.getColumnIndex(KEY_BOOK_AUTHOR_ID))
            )
        }
        cursor.close()
        db.close()
        return book
    }

    @SuppressLint("Range")
    fun getAllBooksByAuthor(authorId: Int): List<Book> {
        val books = mutableListOf<Book>()
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            // Uso de query() en lugar de rawQuery() para mejorar la legibilidad y seguridad
            cursor = db.query(
                TABLE_BOOKS,
                arrayOf(KEY_BOOK_ID, KEY_BOOK_TITLE, KEY_BOOK_PUBLICATION_DATE, KEY_BOOK_GENRE, KEY_BOOK_AUTHOR_ID),
                "$KEY_BOOK_AUTHOR_ID = ?",
                arrayOf(authorId.toString()),
                null, null, null
            )

            if (cursor.moveToFirst()) {
                do {
                    val book = Book(
                        id = cursor.getInt(cursor.getColumnIndex(KEY_BOOK_ID)),
                        title = cursor.getString(cursor.getColumnIndex(KEY_BOOK_TITLE)),
                        fechaPublicacion = cursor.getString(cursor.getColumnIndex(KEY_BOOK_PUBLICATION_DATE)),
                        genero = cursor.getString(cursor.getColumnIndex(KEY_BOOK_GENRE)),
                        authorid = authorId
                    )
                    books.add(book)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            // Log the exception or handle it as necessary
            Log.e("SQLite", "Error while getting books", e)
        } finally {
            // Asegurarse de que el cursor y la base de datos siempre se cierran
            cursor?.close()
            db.close()
        }

        return books
    }

    @SuppressLint("Range")
    fun getAllBooks(): List<Book> {
        val list = ArrayList<Book>()
        val selectQuery = "SELECT * FROM ${SQLiteHelperBook.TABLE_BOOKS}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    id = cursor.getInt(cursor.getColumnIndex(KEY_BOOK_ID)),
                    title = cursor.getString(cursor.getColumnIndex(KEY_BOOK_TITLE)),
                    fechaPublicacion = cursor.getString(cursor.getColumnIndex(KEY_BOOK_PUBLICATION_DATE)),
                    genero = cursor.getString(cursor.getColumnIndex(KEY_BOOK_GENRE)),
                    authorid = cursor.getInt(cursor.getColumnIndex(KEY_BOOK_AUTHOR_ID))
                )
                list.add(book)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    fun updateBook(book: Book): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_BOOK_TITLE, book.title)
            put(KEY_BOOK_PUBLICATION_DATE, book.fechaPublicacion)
            put(KEY_BOOK_GENRE, book.genero)
        }

        val success = db.update(
            TABLE_BOOKS,
            values,
            "$KEY_BOOK_ID = ?",
            arrayOf(book.id.toString())
        )
        db.close()
        return success
    }

    fun deleteBook(book: Book) {
        val db = this.writableDatabase
        db.delete(TABLE_BOOKS, "$KEY_BOOK_ID = ?", arrayOf(book.id.toString()))
        db.close()
    }

}
