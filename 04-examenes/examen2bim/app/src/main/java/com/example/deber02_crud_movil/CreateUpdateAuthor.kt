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

class CreateUpdateAuthor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_update_author)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_google_maps)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isCreating = intent.getBooleanExtra("IS_CREATING", true)

        val id = findViewById<EditText>(R.id.input_id_author)
        val nombre = findViewById<EditText>(R.id.input_nombre_author)
        val nacionalidad = findViewById<EditText>(R.id.input_nacionalidad_author)
        val fechaNac = findViewById<EditText>(R.id.input_fec_nac_author)


        val botonCrearActualizar = findViewById<Button>(R.id.btn_crear_actualizar_author)
        botonCrearActualizar.setOnClickListener {
            val db = SQLiteHelperAuthor(this) // Asegúrate de que DatabaseHelper es tu clase de ayuda SQLite

            // Crear un objeto Author con la información ingresada
            val author = Author(
                id = id.text.toString().toInt(), // SQLite automáticamente asignará un id si es 0 para nuevas entradas
                name = nombre.text.toString(),
                nationality = nacionalidad.text.toString(),
                birthdate = fechaNac.text.toString()
            )

            if (isCreating) {
                // Crear un nuevo autor
                db.addAuthor(author)
                Toast.makeText(this, "Autor creado con éxito.", Toast.LENGTH_SHORT).show()
            } else {
                // Actualizar un autor existente
                author.id = intent.getIntExtra("AUTHOR_ID", 0) // Asegúrate de haber pasado el ID
                db.updateAuthor(author)
                Toast.makeText(this, "Autor actualizado con éxito.", Toast.LENGTH_SHORT).show()
            }

            // Cerrar la actividad y volver a la lista de autores
            // Después de crear o actualizar
            setResult(Activity.RESULT_OK)
            finish()
        }

        if (!isCreating) {
            id.setText(intent.getIntExtra("AUTHOR_ID",0).toString())
            nombre.setText(intent.getStringExtra("AUTHOR_NAME"))
            nacionalidad.setText(intent.getStringExtra("AUTHOR_NATIONALITY"))
            fechaNac.setText(intent.getStringExtra("AUTHOR_BIRTHDATE"))
        }

        // Configurar el botón según sea crear o actualizar
        botonCrearActualizar.text = if (isCreating) "Crear" else "Actualizar"
        /*botonCrearActualizar.setOnClickListener {
            // Aquí manejas la lógica de crear o actualizar
        }*/

        // Para retroceso de actividades
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // Habilitar el botón de regreso en la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar el clic del botón de regreso en la Toolbar
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}