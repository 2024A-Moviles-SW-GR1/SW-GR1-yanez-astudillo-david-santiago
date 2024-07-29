package com.example.deber02_crud_movil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AuthorAdapter(context: Context, authors: List<Author>) :
    ArrayAdapter<Author>(context, 0, authors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.author_list_item, parent, false)
        }

        val author = getItem(position) ?: return itemView!!

        // Usar llamadas seguras (?.) para evitar NullPointerException
        val idTextView = itemView?.findViewById<TextView>(R.id.tv_author_id)
        val nameTextView = itemView?.findViewById<TextView>(R.id.tv_author_name)
        val nationalityTextView = itemView?.findViewById<TextView>(R.id.tv_author_nationality)
        val birthdateTextView = itemView?.findViewById<TextView>(R.id.tv_author_birthdate)

        // Utiliza el operador Elvis para manejar cualquier TextView que pueda ser nulo
        idTextView?.text = author.id.toString()
        nameTextView?.text = author.name
        nationalityTextView?.text = author.nationality
        birthdateTextView?.text = author.birthdate

        return itemView!!
    }

}