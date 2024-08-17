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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    companion object {
        private const val REQUEST_CODE_UPDATE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_google_maps)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonCrearAuthor = findViewById<Button>(R.id.btn_crear)
        botonCrearAuthor.setOnClickListener {
            irActividad(CreateUpdateAuthor::class.java, null, true) // true para crear
        }

        listView = findViewById<ListView>(R.id.lv_authors)


        registerForContextMenu(listView)
        setupListViewListener(listView)
        setupCreateButton()

        // Para retroceso de actividades
        /*val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // Habilitar el botón de regreso en la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)*/

        reloadData()
    }

    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar el clic del botón de regreso en la Toolbar
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }*/

    private fun setupListViewListener(listView: ListView) {
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedAuthor = listView.adapter.getItem(position) as Author
            irActividad(CreateUpdateAuthor::class.java, selectedAuthor, false)
        }
    }

    private fun setupCreateButton() {
        val btnCrear = findViewById<Button>(R.id.btn_crear)
        btnCrear.setOnClickListener {
            irActividad(CreateUpdateAuthor::class.java, null, true)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_author, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        when (item.itemId) {
            R.id.item_editar -> {
                // Obtener el autor seleccionado y lanzar la actividad de edición
                val selectedAuthor = listView.adapter.getItem(info.position) as Author
                irActividad(CreateUpdateAuthor::class.java, selectedAuthor, false)
                return true
            }
            R.id.item_eliminar -> {
                // Obtener el autor seleccionado
                val adapter = listView.adapter as AuthorAdapter
                val selectedAuthor = adapter.getItem(info.position) as Author

                // Crear instancia de DatabaseHelper y eliminar el autor
                val db = SQLiteHelperAuthor(this)
                db.deleteAuthor(selectedAuthor)

                // Actualizar la lista de autores
                reloadData()  // Asegúrate de que este método actualiza correctamente el adapter del ListView

                Toast.makeText(this, "Autor eliminado", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.item_ver_libros -> {
                // Ir a la actividad que muestra los libros del autor
                val selectedAuthor = listView.adapter.getItem(info.position) as Author
                verLibros(MainActivityBooks ::class.java, selectedAuthor)
                return true
            }
            R.id.item_ver_ubicacion -> {
                // Ir a la actividad que muestra la ubicacion
                val selectedAuthor = listView.adapter.getItem(info.position) as Author
                verUbicacion(GoogleMapsActivity ::class.java, selectedAuthor)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            // Recargar los datos
            reloadData()
        }
    }

    private fun reloadData() {
        val db = SQLiteHelperAuthor(this)
        val authorsList = db.getAllAuthors() // Asegúrate de que este método devuelva la lista actualizada de autores
        val adapter = AuthorAdapter(this, authorsList)
        listView.adapter = adapter
    }

    // Envía hacia MainActivityBooks
    private fun verLibros(clase: Class<*>, author: Author) {
        val intent = Intent(this, clase)
        intent.putExtra("AUTHOR_ID", author.id)
        startActivityForResult(intent, REQUEST_CODE_UPDATE)
    }

    private fun verUbicacion(clase: Class<*>, author: Author) {
        val intent = Intent(this, clase)
        intent.putExtra("AUTHOR_LOCATION", author.nationality)
        startActivityForResult(intent, REQUEST_CODE_UPDATE)
    }

    // Envía hacia CreateUpdateAuthor
    private fun irActividad(clase: Class<*>, author: Author? = null, isCreating: Boolean) {
        val intent = Intent(this, clase)
        intent.putExtra("IS_CREATING", isCreating)
        if (!isCreating) {
            intent.putExtra("AUTHOR_ID", author?.id)
            intent.putExtra("AUTHOR_NAME", author?.name)
            intent.putExtra("AUTHOR_NATIONALITY", author?.nationality)
            intent.putExtra("AUTHOR_BIRTHDATE", author?.birthdate)
        }
        startActivityForResult(intent, REQUEST_CODE_UPDATE) // REQUEST_CODE_UPDATE es una constante que define si estás creando o actualizando
    }
}