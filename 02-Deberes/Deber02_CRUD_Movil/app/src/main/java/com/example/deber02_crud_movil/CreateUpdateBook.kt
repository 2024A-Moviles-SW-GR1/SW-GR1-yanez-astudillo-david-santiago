package com.example.deber02_crud_movil

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateUpdateBook : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_book)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isCreating = intent.getBooleanExtra("IS_CREATING", true)

        val idEditText = findViewById<EditText>(R.id.input_id_book)
        val titleEditText = findViewById<EditText>(R.id.input_titulo_book)
        val publicationDateEditText = findViewById<EditText>(R.id.input_fecha_pub_book)
        val genreEditText = findViewById<EditText>(R.id.input_genero_book)

        val createUpdateButton = findViewById<Button>(R.id.btn_crear_actualizar_book)
        createUpdateButton.setOnClickListener {
            val db = SQLiteHelperBook(this)  // Assuming SQLiteHelperBook is your database helper class

            // Create a Book object with the information entered
            val book = Book(
                id = idEditText.text.toString().toInt(),
                title = titleEditText.text.toString(),
                fechaPublicacion = publicationDateEditText.text.toString(),  // Ensure date format is correct
                genero = genreEditText.text.toString()
            )

            if (isCreating) { // VER POR QUE NO SE ESTÁN CREANDO LOS LIBROSSS-----------
                // Create a new book
                db.addBook(book, intent.getIntExtra("AUTHOR_ID", 0))  // Pass the correct author ID
                Toast.makeText(this, "Libro creado con éxito.", Toast.LENGTH_SHORT).show()
            } else {
                // Update an existing book
                book.id = intent.getIntExtra("BOOK_ID", 0)  // Make sure you have passed the ID
                db.updateBook(book)
                Toast.makeText(this, "Libro actualizado con éxito.", Toast.LENGTH_SHORT).show()
            }

            // Close the activity and return to the book list
            setResult(Activity.RESULT_OK)
            finish()
        }

        if (!isCreating) {
            idEditText.setText(intent.getIntExtra("BOOK_ID", -1))
            titleEditText.setText(intent.getStringExtra("BOOK_TITLE"))
            publicationDateEditText.setText(intent.getStringExtra("BOOK_PUBLICATION_DATE"))
            genreEditText.setText(intent.getStringExtra("BOOK_GENRE"))
        }

        // Configurar el botón según sea crear o actualizar
        createUpdateButton.text = if (isCreating) "Crear" else "Actualizar"

        // Set up the action bar for back navigation
        val toolbar: Toolbar = findViewById(R.id.toolbar_create_update_book)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the click of the back button on the Toolbar
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}