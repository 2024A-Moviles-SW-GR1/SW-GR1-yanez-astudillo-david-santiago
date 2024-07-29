package com.example.deber02_crud_movil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivityBooks : AppCompatActivity() {

    private lateinit var listView: ListView
    private var authorIdFromIntent: Int = -1

    companion object {
        private const val REQUEST_CODE_UPDATE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_books)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tomar el id del autor que se manda por el intent desde MainActivity
        authorIdFromIntent = intent.getIntExtra("AUTHOR_ID", -1)

        //---------------------

        val botonCrearBook = findViewById<Button>(R.id.btn_crear_book)
        botonCrearBook.setOnClickListener {
            irActividad(CreateUpdateBook::class.java, null, true) // true para crear
        }

        listView = findViewById<ListView>(R.id.lv_books)


        registerForContextMenu(listView)
        //setupListViewListener(listView)

        setupCreateButton()

        //---------------------

        // Para retroceso de actividades
        val toolbar: Toolbar = findViewById(R.id.toolbar_main_books)
        setSupportActionBar(toolbar)

        // Habilitar el botón de regreso en la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // ----------------------
        reloadData(authorIdFromIntent) // Actualizar lista de libros
    }

    private fun setupListViewListener(listView: ListView) {
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedBook = listView.adapter.getItem(position) as Book
            irActividad(CreateUpdateBook::class.java, selectedBook, false)
        }
    }

    private fun setupCreateButton() {
        val btnCrear = findViewById<Button>(R.id.btn_crear_book)
        btnCrear.setOnClickListener {
            irActividad(CreateUpdateBook::class.java, null, true)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_books, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        when (item.itemId) {
            R.id.item_editar_book -> {
                // Obtener el libro seleccionado y lanzar la actividad de edición
                val selectedBook = listView.adapter.getItem(info.position) as Book
                irActividad(CreateUpdateBook::class.java, selectedBook, false)
                return true
            }
            R.id.item_eliminar_book -> {
                // Obtener el libro seleccionado
                val adapter = listView.adapter as BookAdapter
                val selectedBook = adapter.getItem(info.position) as Book

                // Crear instancia de DatabaseHelper y eliminar el autor
                val db = SQLiteHelperBook(this)
                db.deleteBook(selectedBook)

                // Actualizar la lista de libros
                reloadData(authorIdFromIntent)  // Asegúrate de que este método actualiza correctamente el adapter del ListView

                Toast.makeText(this, "Libro eliminado", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            // Recargar los datos
            reloadData(authorIdFromIntent)
        }
    }

    private fun reloadData(authorId: Int) {
        val db = SQLiteHelperBook(this)
        val booksList = db.getAllBooksByAuthor(authorId)
        //val booksList = db.getAllBooks()
        val adapter = BookAdapter(this, booksList)
        listView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar el clic del botón de regreso en la Toolbar
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Envía a CreateUpdateBooks
    private fun irActividad(clase: Class<*>, book: Book? = null, isCreating: Boolean) {
        val intent = Intent(this, clase)
        intent.putExtra("IS_CREATING", isCreating)
        if (!isCreating) {
            intent.putExtra("BOOK_ID", book?.id)
            intent.putExtra("BOOK_TITLE", book?.title)
            intent.putExtra("BOOK_FECHAPUB", book?.fechaPublicacion)
            intent.putExtra("BOOK_GENERO", book?.genero)
        }
        startActivityForResult(intent, REQUEST_CODE_UPDATE) // REQUEST_CODE_UPDATE es una constante que define si estás creando o actualizando
    }
}