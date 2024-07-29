package com.example.deber02_crud_movil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class BookAdapter(context: Context, books: List<Book>) : ArrayAdapter<Book>(context, 0, books) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false)
        }

        val book = getItem(position) ?: return itemView!!

        val titleTextView = itemView?.findViewById<TextView>(R.id.tv_book_title)
        val dateTextView = itemView?.findViewById<TextView>(R.id.tv_book_publication_date)
        val genreTextView = itemView?.findViewById<TextView>(R.id.tv_book_genre)

        titleTextView?.text = book.title
        dateTextView?.text = book.fechaPublicacion
        genreTextView?.text = book.genero

        return itemView!!
    }
}
